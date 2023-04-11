package com.mmu6.mmu6.Respiritory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mmu6.mmu6.Class.Chat;
import com.mmu6.mmu6.Class.ChatRelationship;
import com.mmu6.mmu6.Class.userContacts;


public interface chatRelationshipRespiritory extends JpaRepository<ChatRelationship, Long> {
    @Query("SELECT MAX(c.chatId) FROM ChatRelationship c")
    Integer getLastChatId();

	//Optional<ChatRelationship> findByChatIdAndUserID(Long chatId, Long userId);
    // make consistent
    
    @Query(value = "SELECT chat_id FROM chat_relationship WHERE userid IN (?1) GROUP BY chat_id HAVING COUNT(DISTINCT userid) = ?2", nativeQuery = true)
    List<Long> findChatIdByUserIds(List<Long> userIds, int numberOfUsers);

 
}
