package com.mmu6.mmu6.Respiritory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mmu6.mmu6.Class.User;

public interface userRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	User findByauthToken(String authToken);
}
