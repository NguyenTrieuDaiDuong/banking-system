package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Accounts;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {

	Optional<Accounts> findByAccountNumber(String accountNumber);

	@Query("SELECT a FROM Accounts a WHERE a.users.id = :userId")
	List<Accounts> findByUserId(@Param("userId") Long userId);

	@Query("SELECT a FROM Accounts a WHERE a.users.id = :userId AND a.accountStatuses.statusCode = :statusCode")
	List<Accounts> findByUserIdAndStatusCode(@Param("userId") Long userId, @Param("statusCode") String statusCode);

	@Query("SELECT a FROM Accounts a WHERE a.users.id = :userId AND a.accountTypes.typeCode = :typeCode")
	List<Accounts> findByUserIdByTypeCode(@Param("userId") Long userId, @Param("typeCode") String typeCode);
}
