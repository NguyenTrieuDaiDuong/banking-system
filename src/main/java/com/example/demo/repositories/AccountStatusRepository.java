package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.AccountStatuses;

@Repository
public interface AccountStatusRepository extends JpaRepository<AccountStatuses, Long> {
	Optional<AccountStatuses> findByStatusCode(String statusCode);
}