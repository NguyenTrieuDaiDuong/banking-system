package com.example.demo.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Accounts;
import com.example.demo.entities.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
	// tìm tài khoản ( tài khoản gửi , tài khoản nhận) theo Id
	List<Transactions> findByAccountsByFromAccountIdOrAccountsByToAccountId(Accounts fromAccount, Accounts toAccount);

	List<Transactions> findByAccountsByFromAccountId_AccountNumber(String fromAccountNumber);

	List<Transactions> findByAccountsByToAccountId_AccountNumber(String toAccountNumber);

	@Query("SELECT t FROM Transactions t WHERE t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
	List<Transactions> findTransactionsByDateRange(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	List<Transactions> findByAccountsByFromAccountId(Accounts fromAccount);

	List<Transactions> findByAccountsByToAccountId(Accounts toAccount);

	// tìm tài khoản giao dịch trong 1 khoảng thời gian
	@Query("SELECT t FROM Transactions t WHERE (t.accountsByFromAccountId = :account OR t.accountsByToAccountId = :account ) AND "
			+ "t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
	List<Transactions> findByAccountAndDateRange(@Param("account") Accounts account,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	Optional<Transactions> findByTransactionCode(String transactionCode);

	// tìm giao dịch gần đây ( có giới hạn)
	List<Transactions> findTop5ByAccountsByFromAccountIdOrAccountsByToAccountIdOrderByCreatedAtDesc(
			Accounts fromAccount, Accounts toAccount);

	@Query("SELECT t FROM Transactions t WHERE t.accountsByFromAccountId.users.username = :username OR t.accountsByToAccountId.users.username =:username ORDER BY t.createdAt DESC")
	List<Transactions> findByUserUsername(@Param("username") String username);

	@Query("SELECT COALESCE(SUM(t.amount) , 0) FROM Transactions t WHERE t.transactionStatuses.statusCode = 'COMPLETED'")
	BigDecimal getTotalRevenue();

	@Query("SELECT COUNT(t) FROM Transactions t WHERE DATE(t.createdAt) = CURRENT_DATE")
	Long getTodayTransactionCount();

}
