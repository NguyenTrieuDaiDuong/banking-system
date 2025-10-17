package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.TransactionTypes;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionTypes, Long> {
	Optional<TransactionTypes> findByTypeCode(String typeCode);
}
