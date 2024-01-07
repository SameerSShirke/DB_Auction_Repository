package com.db.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "USERS") // , uniqueConstraints = { @UniqueConstraint(columnNames = "USERNAME"),
// @UniqueConstraint(columnNames = "EMAIL_ID") })
@Data
public class UserEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", nullable = false)
	private Long userId;

	@NotBlank
	@Size(max = 20)
	@Column(name = "USER_NAME", nullable = false)
	private String userName;

	@JsonIgnore
	@NotBlank
	@Size(max = 60)
	@Email
	@Column(name = "EMAIL_ID", nullable = false)
	private String emailId;

	@NotBlank
	@JsonIgnore
	@Size(max = 120)
	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	@JsonIgnore
	private Set<RolesEntity> userRoles = new HashSet<RolesEntity>();
	
	@Column(name = "ROLE_TYPE", nullable = false)
	private String roleType;

	public UserEntity() {
	}

	public UserEntity(@NotBlank @Size(max = 20) String userName, @NotBlank @Size(max = 50) @Email String emailId,
			@NotBlank @Size(max = 120) String password , String roleType ) {
		this.userName = userName;
		this.emailId = emailId;
		this.password = password;
		this.roleType = roleType;
	}
}
