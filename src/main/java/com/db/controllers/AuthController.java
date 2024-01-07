package com.db.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.entity.RolesEntity;
import com.db.entity.UserEntity;
import com.db.model.JwtResponse;
import com.db.model.LoginRequest;
import com.db.model.MessageResponse;
import com.db.model.SignupRequest;
import com.db.model.UserRole;
import com.db.repository.RoleRepository;
import com.db.repository.UserRepository;
import com.db.security.JwtUtils;
import com.db.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/db/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	
	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUserName(signupRequest.getUserName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmailId(signupRequest.getEmailId())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email already in use!"));
		}

		// Create new user`s account
		UserEntity user = new UserEntity(signupRequest.getUserName(), signupRequest.getEmailId(),
				passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getUserRole() );

		String strRoles = signupRequest.getUserRole();
		Set<RolesEntity> roles = new HashSet<>();
		

		if (strRoles == null) {
			RolesEntity userRole = roleRepository.findByRoleName(UserRole.ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
				switch (strRoles) {
				case "BUYER":
					RolesEntity adminRole = roleRepository.findByRoleName(UserRole.BUYER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(adminRole);
					break;
					
				case "SELLER":
					RolesEntity modRole = roleRepository.findByRoleName(UserRole.SELLER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(modRole);

					break;
					
				
				default:
					throw new RuntimeException("Error: Role is not found.");
					
				}
		}
		user.setUserRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully"));
	}
}
