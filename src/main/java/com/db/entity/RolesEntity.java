package com.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.db.model.UserRole;

import lombok.Data;

@Entity
@Table(name = "ROLES")
@Data
public class RolesEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
    
    @Enumerated(EnumType.STRING)
	@Column(name = "ROLE_NAME", length = 20, nullable = false)
    private UserRole roleName;

}
