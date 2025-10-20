package com.example.demo.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Accounts;
import com.example.demo.entities.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

	List<Transactions> findByAccountsByFromAccountIdOrAccountsByToAccountId(Accounts fromAccount, Accounts toAccount);

	List<Transactions> findByAccountsByFromAccountId_AccountNumber(String accountsByFromAccountId);

	List<Transactions> findByAccountsByToAccountId_AccountNumber(String accountsByToAccountId);

	@Query("SELECT t FROM Transactions t WHERE t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
	List<Transactions> findTransactionsByDateRange(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	List<Transactions> findByAccountsByFromAccountId(Accounts fromAccount);

	List<Transactions> findByAccountsByToAccountId(Accounts toAccount);

}
