package com.svgas.main.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.parametertypes;
import com.svgas.main.repository.ParamTypesRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/paramtype")
public class ParamTypesController 
{
	@Autowired
	private ParamTypesRepository ptrepo;
	
	@GetMapping("/allptypes")
	public List<parametertypes> getAllPtype()
	{
		//do some logic to check the client is DEVELOPER and  NOT ADMIN or USER
		return ptrepo.findAll();
	}
	
	@GetMapping("/ptypeswrtid/{pid}")
	public parametertypes getParameterTypesById(@PathVariable long pid)
	{
		parametertypes pt = null;
		Optional<parametertypes> opt = ptrepo.findById(pid);
		if(opt.isPresent())
		{
			pt = opt.get();
		}
		return pt;
	}
	
	@PostMapping("/addptypes")
	public parametertypes addPType(@RequestBody parametertypes ptype)
	{
		return ptrepo.saveAndFlush(ptype);
	}
	
	@PostMapping("/removeptypes")
	public void removePType(@RequestBody parametertypes ptype)
	{
		ptrepo.deleteById(ptype.getId());
	}
	
	@PostMapping("/updateptypes")
	public void updatePType(@RequestBody parametertypes pts)
	{
		Optional<parametertypes> pto = ptrepo.findById(pts.getId());
		if(pto.isPresent())
		{
			parametertypes ptt = pto.get();
			ptt.setDescription(pts.getDescription());
			ptt.setParamtype(pts.getParamtype());
//			ptt.setParamunit(pts.getParamunit());
			
			ptrepo.saveAndFlush(ptt);
		}
	}
}