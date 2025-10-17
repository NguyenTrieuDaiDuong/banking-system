package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

	Users findByUsername(String username);

	Optional<Users> findByEmail(String email);

	Optional<Users> findByIdentityCard(String identityCard);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByIdentityCard(String identityCard);

	List<Users> getByUserRoles_RoleCode(String roleCode);

	@Query("SELECT DISTINCT u FROM Users u JOIN FETCH u.userRoles JOIN FETCH u.userStatuses")
	List<Users> findAllWithRelations();
}
