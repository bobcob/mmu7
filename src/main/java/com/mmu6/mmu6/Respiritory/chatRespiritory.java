package com.mmu6.mmu6.Respiritory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mmu6.mmu6.Class.Chat;


public interface chatRespiritory extends JpaRepository<Chat, Long> {
	List<Chat> findAllByChatId(Long id);
	
	@Query(value = "SELECT * FROM chat WHERE chat_id = (?1) ORDER BY time DESC LIMIT 1", nativeQuery = true)
    Chat findLatestMessageInChat(Long id);

}
