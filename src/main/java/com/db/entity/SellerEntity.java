package com.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "SELLER")//, uniqueConstraints = { @UniqueConstraint(columnNames = "EMAIL_ID") })
@Data
public class SellerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SELLER_ID", nullable = false)
	private Long sellerId;

	@NotBlank
	@Size(max = 30)
	@Column(name = "SELLER_NAME", nullable = false)
	private String sellerName;

	@NotBlank
	@Size(max = 30)
	@Email
	@Column(name = "EMAIL_ID", nullable = false)
	private String emailId;

	@NotBlank
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	

}
