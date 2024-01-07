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
@Table(name = "BUYER")//, uniqueConstraints = { @UniqueConstraint(columnNames = "emailId") })
@Data
public class BuyerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BUYER_ID", nullable = false)
	private Long buyerId;

	@NotBlank
	@Size(max = 30)
	@Column(name = "BUYER_NAME", nullable = false)
	private String buyerName;

	@NotBlank
	@Size(max = 30)
	@Email
	@Column(name = "EMAIL_ID", nullable = false)
	private String emailId;

	@NotBlank
	@Column(name = "PASSWORD", nullable = false)
	private String password;

}
