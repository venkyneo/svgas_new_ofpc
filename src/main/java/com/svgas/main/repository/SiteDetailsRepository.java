package com.svgas.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svgas.main.models.sitedetails;

public interface SiteDetailsRepository extends JpaRepository<sitedetails, Long> {
	List<sitedetails> findAll();
}
