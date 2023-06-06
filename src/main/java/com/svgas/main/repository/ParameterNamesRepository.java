package com.svgas.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.svgas.main.models.parameternames;

public interface ParameterNamesRepository extends JpaRepository<parameternames, Long> {
	List<parameternames> findBySubsitedetailsId(long ssid);
	List<parameternames> findBySubsitedetailsIdAndParametertypesId(int subsiteid, int ptypeid);
	List<parameternames> findAll();
	
	@Query(value="select max(paramcolnum) from paramternames where subsiteid = :ssid", nativeQuery=true)
	int findMaxParameternamesBySubsiteNative(@Param("ssid") long subsiteid);
	
	@Query(value="select max(datetime) from paramternames where subsiteid = :ssid", nativeQuery=true)
	String findMaxDateParameternamesBySubsiteNative(@Param("ssid") long subsiteid);
}
