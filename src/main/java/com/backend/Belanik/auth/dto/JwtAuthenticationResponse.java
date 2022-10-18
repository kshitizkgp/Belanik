package com.backend.Belanik.auth.dto;

import lombok.Value;

@Value
public class JwtAuthenticationResponse {
	private String accessToken;
	private UserInfo user;
}