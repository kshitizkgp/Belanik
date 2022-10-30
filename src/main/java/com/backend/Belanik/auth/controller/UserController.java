package com.backend.Belanik.auth.controller;

import com.backend.Belanik.auth.config.CurrentUser;
import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.auth.dto.SignUpRequest;
import com.backend.Belanik.auth.dto.UpdateUserRequest;
import com.backend.Belanik.auth.service.UserService;
import com.backend.Belanik.auth.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
		return ResponseEntity.ok(GeneralUtils.buildUserInfo(user));
	}

	@PostMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> updateCurrentUser(@CurrentUser LocalUser user, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
		return ResponseEntity.ok(GeneralUtils.buildUserInfo(userService.updateExistingUser(user, updateUserRequest)));
	}
}