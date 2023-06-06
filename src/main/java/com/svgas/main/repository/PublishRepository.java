package com.svgas.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svgas.main.models.publishtopics;

public interface PublishRepository extends JpaRepository<publishtopics, Long> {
	List<publishtopics> findBySubsitedetailsId(long subsiteid);
}
