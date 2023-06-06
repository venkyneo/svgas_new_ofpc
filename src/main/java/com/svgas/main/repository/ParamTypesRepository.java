package com.svgas.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svgas.main.models.parametertypes;

public interface ParamTypesRepository extends JpaRepository<parametertypes, Long>
{
	
}