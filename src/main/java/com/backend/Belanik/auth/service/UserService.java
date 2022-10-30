package com.backend.Belanik.auth.service;

import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.auth.dto.SignUpRequest;
import com.backend.Belanik.auth.dto.UpdateUserRequest;
import com.backend.Belanik.auth.exception.UserAlreadyExistAuthenticationException;
import com.backend.Belanik.auth.model.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;

/**
 * @author Chinna
 * @since 26/3/18
 */
public interface UserService {

	public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

	User findUserByEmail(String email);

	Optional<User> findUserById(Long id);

	LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

	public User updateExistingUser(LocalUser user, UpdateUserRequest updateUserRequest);
}
