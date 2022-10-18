package com.backend.Belanik.auth.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserInfo {
	private String id, displayName, email, imageUrl;
	private List<String> roles;
}