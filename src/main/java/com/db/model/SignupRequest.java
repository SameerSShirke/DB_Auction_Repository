package com.db.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

	@NotBlank
	@Size(min = 3, max = 20)
	private String userName;

	@NotBlank
	@Size(max = 50)
	@Email
	private String emailId;

	private String userRole;

	@NotBlank
	@Size(min = 8, max = 40)
	private String password;

}
