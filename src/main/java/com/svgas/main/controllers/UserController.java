package com.svgas.main.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.jwttoken.JwtUtils;
import com.svgas.main.models.ERole;
import com.svgas.main.models.Role;
import com.svgas.main.models.User;
import com.svgas.main.models.subsitedetails;
import com.svgas.main.repository.RoleRepository;
import com.svgas.main.repository.SubSiteDetailsRepository;
import com.svgas.main.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/usermanager")
public class UserController {
	@Autowired
	UserRepository usrepo;
	
	@Autowired
	private RoleRepository rrepo;
	
	@Autowired
	JwtUtils jwtUtil;
	
	@Autowired
	private SubSiteDetailsRepository ssdrepo;

	@GetMapping("/allusers")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getAllUsers() {		
		List<Role> role_list = rrepo.findAll();
		for(Role rl : role_list)
		{
			if(rl.getName() == ERole.ROLE_ADMIN)
			{
				role_list.remove(rl);
				break;
			}
		}
//		role_list.add("ROLE_USER");
//		role_list.add("ROLE_MODERATOR");
		List<User> user_list = usrepo.findByRolesIn(role_list);
		if(!user_list.isEmpty())
		{
			//System.out.println("ulist not empty");
			for(User u : user_list)
			{
				System.out.println("username: "+u.getUsername());
			}
		}

		return user_list;
	}

	@PostMapping("/adduser")
	// @PreAuthorize("hasRole('ADMIN')")
	public User addNewUser(@RequestBody User userdetails) {
		System.out.println("in add");
		return usrepo.saveAndFlush(userdetails);
	}

	@GetMapping("/userwrtid/{uid}")
	@PreAuthorize("hasRole('ADMIN')")
	public User getParticularUserwrtID(@PathVariable long uid) {
		User us = null;
		Optional<User> ous = usrepo.findById(uid);
		if (ous.isPresent()) {
			us = ous.get();
		}
		return us;
	}

	@PostMapping("/updateuser")
	@PreAuthorize("hasRole('ADMIN')")
	public void updateParticularUser(@RequestBody User userdetails) {
		System.out.println("in updateuser");
		Optional<User> user = usrepo.findById(userdetails.getId());
		if (user.isPresent()) {
			User us = user.get();
			us.setEmail(userdetails.getEmail());
			us.setPassword(userdetails.getPassword());
			us.setRoles(userdetails.getRoles());
			us.setUsername(userdetails.getUsername());
			//us.setSsdetails(userdetails.getSsdetails());

			usrepo.saveAndFlush(us);
		}
	}

	@PostMapping("/removeuser")
	@PreAuthorize("hasRole('ADMIN')")
	public void removeParticularUser(@RequestBody User userdetails) {
		Optional<User> user = usrepo.findById(userdetails.getId());
		System.out.println("in del0");
		if (user.isPresent()) {
			User us = user.get();
			System.out.println("in del1");
//			if ((us.getUsername() == userdetails.getUsername()) && (us.getEmail() == userdetails.getEmail())
//					 && (us.getRoles() == userdetails.getRoles())) {
				System.out.println("in del2");
				usrepo.deleteById(userdetails.getId());
//			}
			// else send message in response
		}
	}

	@GetMapping("/sswrtuser")
	public List<subsitedetails> getSubsitesWRTUser(@RequestHeader(name = "Authorization") String AuthData) {
		String token = "";
		List<subsitedetails> ssdlist = null;
		if (AuthData.contains("Bearer")) {
			token = AuthData.substring(7);
			String username = jwtUtil.getUsernameFromJwtToken(token);
			Optional<User> ud = usrepo.findByUsername(username);
			
			if (ud.isPresent()) {
				User u = ud.get();
				if(hasSelectedRole(u,ERole.ROLE_ADMIN))
				{
					ssdlist = ssdrepo.findAll();
				}
				else
				{
					ssdlist = (List<subsitedetails>) u.getSsdetails();	
				}
			}
		}

		return ssdlist;
	}
	
	public boolean hasSelectedRole(User user, ERole er)
	{
		boolean resp=false;

		for(Role role : user.getRoles())
		{
			if(role.getName() == er)
			{
				resp=true;
				break;
			}
		}
		
		return resp;
	}
}
