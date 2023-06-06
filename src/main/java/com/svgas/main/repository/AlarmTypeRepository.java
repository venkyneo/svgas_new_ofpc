package com.svgas.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svgas.main.models.alarmtype;

public interface AlarmTypeRepository extends JpaRepository<alarmtype, Long> {

}
