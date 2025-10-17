package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.TransactionStatuses;

@Repository
public interface TransactionStatusRepository extends JpaRepository<TransactionStatuses, Long> {
	Optional<TransactionStatuses> findByStatusCode(String statusCode);
}
