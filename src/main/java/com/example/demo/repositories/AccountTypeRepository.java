package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.AccountTypes;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountTypes, Long> {
	Optional<AccountTypes> findByTypeCode(String typeCode);
}
