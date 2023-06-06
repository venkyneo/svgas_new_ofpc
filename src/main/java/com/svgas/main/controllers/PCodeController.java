package com.svgas.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.pcode;
import com.svgas.main.repository.PCodeRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/pcode")
public class PCodeController 
{
	@Autowired
	private PCodeRepository pcr;
	
	@GetMapping("/getallpcodes")
	private List<pcode> getAllPCodeList()
	{
		return pcr.findAll();
	}
	
}