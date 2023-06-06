package com.svgas.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.alarmtype;
import com.svgas.main.repository.AlarmTypeRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/atype")
public class AlarmTypeController 
{
	@Autowired
	private AlarmTypeRepository atr;
	
	@GetMapping("/getallatypes")
	private List<alarmtype> getAllAlarmTypes()
	{
		return atr.findAll();
	}
}