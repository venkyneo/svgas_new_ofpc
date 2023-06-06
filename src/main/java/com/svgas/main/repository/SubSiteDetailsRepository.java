package com.svgas.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svgas.main.models.subsitedetails;

public interface SubSiteDetailsRepository extends JpaRepository<subsitedetails, Long> {

	List<subsitedetails> findBySitedetailsId(long i);
	void deleteById(int id);
	//List<subsitedetails> findByGroupid(groupdetails gid);
//	List<subsitedetails> findBySitedetailsGroupid(groupdetails gid);
}
