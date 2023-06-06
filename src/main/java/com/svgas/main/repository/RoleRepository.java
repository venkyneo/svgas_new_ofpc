package com.svgas.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svgas.main.models.ERole;
import com.svgas.main.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

		Optional<Role> findByName(ERole name);
}
