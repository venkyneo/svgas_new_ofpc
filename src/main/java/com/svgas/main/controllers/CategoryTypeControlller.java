package com.svgas.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svgas.main.models.CategoryType;
import com.svgas.main.repository.CategoryTypeRepository;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/ctype")
public class CategoryTypeControlller 
{
	@Autowired
	private CategoryTypeRepository ctr;
	
	@GetMapping("/getallctypes")
	private List<CategoryType> getAllCategoryTypes()
	{
		return ctr.findAll();
	}
}