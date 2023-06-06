package com.svgas.main.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

import com.svgas.main.jwttoken.JwtUtils;
import com.svgas.main.models.ERole;
import com.svgas.main.models.Role;
import com.svgas.main.models.User;
import com.svgas.main.payload.request.LoginRequest;
import com.svgas.main.payload.request.SignupRequest;
import com.svgas.main.payload.response.JwtResponse;
import com.svgas.main.payload.response.MessageResponse;
import com.svgas.main.repository.RoleRepository;
import com.svgas.main.repository.UserRepository;
import com.svgas.main.service.UserDetailsImpl;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController 
{
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
	{
		try
		{
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			
			UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
			
			return ResponseEntity.ok(new JwtResponse(jwt,
						userDetails.getId(),
						userDetails.getUsername(),
						userDetails.getEmail(),
						roles));
		}
		catch(Exception et)
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Please check username and password!"));
		}
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest)
	{
		if(userRepository.existsByUsername(signupRequest.getUsername()))
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}		
		if(userRepository.existsByEmail(signupRequest.getEmail()))
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		//create new users account
		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if(strRoles == null)
		{
			Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found."));
			roles.add(userRole);			
		}
		else
		{
			strRoles.forEach(role -> {
				switch(role)
				{
					case "ROLE_ADMIN": Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
							roles.add(adminRole);break;
					case "ROLE_MODERATOR": Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
							roles.add(modRole);break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
						roles.add(userRole);break;
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!!"));
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody User updateRequest)
	{
		System.out.println("in auth update");
		System.out.println("updatereq:"+updateRequest);
		if(userRepository.existsByUsername(updateRequest.getUsername()))
		{
			System.out.println("in u-1");
			//return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
			Optional<User> user_o = userRepository.findByUsername(updateRequest.getUsername());
			if(user_o.isPresent())
			{
				System.out.println("in u-2");
				User u = user_o.get();
				if(updateRequest.getId() == u.getId())
				{
					System.out.println("in u-3:"+u.getPassword()+".");
					System.out.println("in u-3.1:"+updateRequest.getPassword());
					if(updateRequest.getPassword() != null)
					{
						System.out.println("in u-4");
						u.setEmail(updateRequest.getEmail());
						u.setPassword(encoder.encode(updateRequest.getPassword()));
						u.setRoles(updateRequest.getRoles());
						u.setSsdetails(updateRequest.getSsdetails());
						userRepository.save(u);
					}
					else
					{
						System.out.println("in u-5");
						u.setEmail(updateRequest.getEmail());
						//u.setPassword(encoder.encode(updateRequest.getPassword()));
						u.setRoles(updateRequest.getRoles());
						u.setSsdetails(updateRequest.getSsdetails());
						System.out.println(updateRequest.getSsdetails());
						userRepository.save(u);
					}
				}
			}
		}	
		else
		{
			//System.out.println("in u-6");
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username not same!"));
		}
//		if(userRepository.existsByEmail(signupRequest.getEmail()))
//		{
//			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//		}
		//create new users account
//		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
//		Set<Role> strRoles = signupRequest.getRoles();
//		Set<Role> roles = new HashSet<>();
//		if(strRoles == null)
//		{
//			Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found."));
//			roles.add(userRole);			
//		}
//		else
//		{
//			strRoles.forEach(role -> {
//				switch(role)
//				{
//					case "ROLE_ADMIN": Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//							roles.add(adminRole);break;
//					case "ROLE_MODERATOR": Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//							roles.add(modRole);break;
//					default:
//						Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//						roles.add(userRole);break;
//				}
//			});
//		}
//		user.setRoles(roles);
//		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User updated successfully!!"));
	}
}