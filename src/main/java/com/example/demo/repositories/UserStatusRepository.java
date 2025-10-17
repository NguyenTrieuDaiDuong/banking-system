package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.UserStatuses;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatuses, Long> {
	Optional<UserStatuses> findByStatusCode(String statusCode);

	boolean existsByStatusCode(String statusCode);
}
