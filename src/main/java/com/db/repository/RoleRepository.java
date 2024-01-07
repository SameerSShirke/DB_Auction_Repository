package com.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.entity.RolesEntity;
import com.db.model.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<RolesEntity, Long> {
    //Optional<RolesEntity> findByERoleName(ERole eRoleName);
	Optional<RolesEntity> findByRoleName(UserRole roleName);
}
