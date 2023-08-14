package com.mmu6.mmu6.Respiritory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mmu6.mmu6.Class.User;
/*
 * Initialising the Repository for the user class and controller
 * passing through the User class alongside a Long data type
 */
public interface userRepository extends JpaRepository<User, Long> {
	// spring boot JPA function to return a user based on the email
	User findByEmail(String email);
	// spring boot JPA function to return a user auth token
	User findByauthToken(String authToken);
}
