package com.mmu6.mmu6.Respiritory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmu6.mmu6.Class.userContacts;

public interface userContactRespiritory extends JpaRepository<userContacts, Long> {
	List<userContacts> findAllByUser1ID(int user1ID);
	Optional<userContacts> findByUser1IDAndUser2ID(int user1id, int user2id);
}
