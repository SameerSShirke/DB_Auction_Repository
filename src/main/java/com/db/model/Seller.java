package com.db.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Seller {
	
	private Long sellerId;
	
	@NotBlank
    @Size(max = 30)
	private String sellerName;
	
	@NotBlank
    @Size(max = 30)
	@Email
	private String emailId;
	
	@NotBlank
	private String password;

}