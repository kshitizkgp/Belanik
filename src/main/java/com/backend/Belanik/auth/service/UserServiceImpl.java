package com.backend.Belanik.auth.service;

import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.auth.dto.SignUpRequest;
import com.backend.Belanik.auth.dto.SocialProvider;
import com.backend.Belanik.auth.dto.UpdateUserRequest;
import com.backend.Belanik.auth.exception.OAuth2AuthenticationProcessingException;
import com.backend.Belanik.auth.exception.UserAlreadyExistAuthenticationException;
import com.backend.Belanik.auth.model.Role;
import com.backend.Belanik.auth.model.User;
import com.backend.Belanik.auth.repo.RoleRepository;
import com.backend.Belanik.auth.repo.UserRepository;
import com.backend.Belanik.auth.security.oauth2.user.OAuth2UserInfo;
import com.backend.Belanik.auth.security.oauth2.user.OAuth2UserInfoFactory;
import com.backend.Belanik.auth.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Chinna
 * @since 26/3/18
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(value = "transactionManager")
	public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
		if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
			throw new UserAlreadyExistAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
		} else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
		}
		User user = buildUser(signUpRequest);
		Date now = Calendar.getInstance().getTime();
		user.setCreatedTimestamp(now);
		user.setModifiedTimestamp(now);
		user = userRepository.save(user);
		userRepository.flush();
		return user;
	}

	private User buildUser(final SignUpRequest formDTO) {
		User user = new User();
		user.setDisplayName(formDTO.getDisplayName());
		user.setEmail(formDTO.getEmail());
		user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
		user.setProfilePictureUrl(formDTO.getImageUrl());
		final HashSet<Role> roles = new HashSet<Role>();
		roles.add(roleRepository.findByName(Role.ROLE_USER));
		user.setRoles(roles);
		user.setProvider(formDTO.getSocialProvider().getProviderType());
		user.setEnabled(true);
		user.setProviderUserId(formDTO.getProviderUserId());
		return user;
	}

	@Override
	public User findUserByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
		System.out.println("kkrprocessUserRegistration: " + oAuth2UserInfo.getImageUrl());
		if (StringUtils.isEmpty(oAuth2UserInfo.getName())) {
			throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
		} else if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}
		SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
		User user = findUserByEmail(oAuth2UserInfo.getEmail());
		if (user != null) {
			if (!user.getProvider().equals(registrationId) && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
				throw new OAuth2AuthenticationProcessingException(
						"Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
			}
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			user = registerNewUser(userDetails);
		}

		return LocalUser.create(user, attributes, idToken, userInfo);
	}

	@Override
	@Transactional
	public User updateExistingUser(LocalUser localUser, UpdateUserRequest updateUserRequest) {
		User user = findUserByEmail(localUser.getUser().getEmail());
		if(user == null) {
			throw new OAuth2AuthenticationProcessingException("User doesn't exit.");
		}
		return updateUserModifiableFields(user, updateUserRequest);
	}

	private User updateUserModifiableFields(User existingUser, UpdateUserRequest updateUserRequest) {
		existingUser.setBio(updateUserRequest.getBio());
		existingUser.setContactNumber(updateUserRequest.getContactNumber());
		try {
			Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(updateUserRequest.getDateOfBirth());
			existingUser.setDateOfBirth(dob);
		} catch (ParseException ex) {
			// TODO (kshitizkr): Add exception logs.
			System.out.println("Unable to parse DOB. Skipping this now and updating the rest of the fields.");
		}
		existingUser.setGender(updateUserRequest.getGender());
		return userRepository.save(existingUser);
	}

	private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
		existingUser.setDisplayName(oAuth2UserInfo.getName());
		existingUser.setProfilePictureUrl(oAuth2UserInfo.getImageUrl());
		return userRepository.save(existingUser);
	}

	private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
		return SignUpRequest.getBuilder().addProviderUserID(oAuth2UserInfo.getId()).addDisplayName(oAuth2UserInfo.getName()).addEmail(oAuth2UserInfo.getEmail())
				.addSocialProvider(GeneralUtils.toSocialProvider(registrationId)).addPassword("changeit").addImageUrl("https://lh3.googleusercontent.com/a/ALm5wu1GFy85_ZclPuO2MIf1C-5pDg8OIbAlr-4yeb5q=s96-c").build();
	}

	@Override
	public Optional<User> findUserById(Long id) {
		return userRepository.findById(id);
	}
}
