package com.svgas.main.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.ERole;
import com.svgas.main.models.Role;
import com.svgas.main.repository.RoleRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/roles")
public class RoleController 
{
	@Autowired
	private RoleRepository rrepo;
	
	@GetMapping("/getallroles")
	public List<Role> getAllRoles()
	{
		List<Role> role_list = rrepo.findAll();
		for(Role rl : role_list)
		{
			if(rl.getName() == ERole.ROLE_ADMIN)
			{
				role_list.remove(rl);
				break;
			}
		}
		return role_list;
	}
	
	@GetMapping("/getprole/{id}")
	public Optional<Role> getParticularRole(@PathVariable Long id)
	{
		return rrepo.findById(id);
	}
}