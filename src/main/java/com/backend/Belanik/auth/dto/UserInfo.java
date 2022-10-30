package com.backend.Belanik.auth.dto;

import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class UserInfo {
	private String id, displayName, email, imageUrl, contactNumber, bio, gender;
	private Date dob;
}