package com.backend.Belanik.auth.util;

import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.auth.dto.SocialProvider;
import com.backend.Belanik.auth.dto.UserInfo;
import com.backend.Belanik.auth.model.Role;
import com.backend.Belanik.auth.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @author Chinna
 *
 */
public class GeneralUtils {

	public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	public static SocialProvider toSocialProvider(String providerId) {
		for (SocialProvider socialProvider : SocialProvider.values()) {
			if (socialProvider.getProviderType().equals(providerId)) {
				return socialProvider;
			}
		}
		return SocialProvider.LOCAL;
	}

	public static UserInfo buildUserInfo(LocalUser localUser) {
		User user = localUser.getUser();
		return buildUserInfo(user);
		}

	public static UserInfo buildUserInfo(User user) {
		return new UserInfo(user.getId().toString(), user.getDisplayName(), user.getEmail(), user.getProfilePictureUrl(),
				user.getContactNumber(), user.getBio(), user.getGender(), user.getDateOfBirth());
	}
}
