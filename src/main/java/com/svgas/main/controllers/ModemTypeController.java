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

import com.svgas.main.models.modemtype;
import com.svgas.main.repository.ModemTypeRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/device")
public class ModemTypeController 
{
	@Autowired
	private ModemTypeRepository mtrepo;
	
	@GetMapping("/getalldt")
	public List<modemtype> GetAllDeviceTypes()
	{
		return mtrepo.findAll();
	}
	
	@PostMapping("/addnewdt")
	public modemtype addNewDevice(@RequestBody modemtype mt)
	{
		return mtrepo.saveAndFlush(mt);
	}
	
	@PostMapping("/updatedt")
	public void updateSelectedDevice(@RequestBody modemtype mtype)
	{
		Optional<modemtype> mt_op = mtrepo.findById(mtype.getId());
		if(mt_op.isPresent())
		{
			modemtype mt = mt_op.get();
			mt.setModemname(mtype.getModemname());
			//mt.setSsd(mtype.getSsd());
			
			mtrepo.saveAndFlush(mt);
		}
	}
	
	@PostMapping("/removedt")
	public void removeSelectedDevice(@RequestBody modemtype mtype)
	{
		mtrepo.deleteById(mtype.getId());
	}
	
	@GetMapping("/getdtwrtid/{mid}")
	public modemtype getDeviceById(@PathVariable long id)
	{
		modemtype mt = null;
		Optional<modemtype> mt_o = mtrepo.findById(id);
		if(mt_o.isPresent())
		{
			mt = mt_o.get();
		}
		
		return mt;
	}
}
