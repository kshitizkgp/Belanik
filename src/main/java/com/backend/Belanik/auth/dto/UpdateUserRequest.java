package com.backend.Belanik.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class UpdateUserRequest {

    private String bio;

    private String contactNumber;

    private String dateOfBirth;

    private String gender;
}
