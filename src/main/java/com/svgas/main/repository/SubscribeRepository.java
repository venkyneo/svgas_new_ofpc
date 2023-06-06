package com.svgas.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svgas.main.models.subscribetopics;

public interface SubscribeRepository extends JpaRepository<subscribetopics, Long> {
	List<subscribetopics> findBySubsitedetailsId(long ssid);
}
