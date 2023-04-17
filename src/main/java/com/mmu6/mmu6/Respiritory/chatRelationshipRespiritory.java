package com.mmu6.mmu6.Respiritory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mmu6.mmu6.Class.Chat;
import com.mmu6.mmu6.Class.ChatRelationship;
import com.mmu6.mmu6.Class.Contact;
import com.mmu6.mmu6.Class.userContacts;


public interface chatRelationshipRespiritory extends JpaRepository<ChatRelationship, Long> {
    @Query("SELECT MAX(c.chatId) FROM ChatRelationship c")
    Integer getLastChatId();

	//Optional<ChatRelationship> findByChatIdAndUserID(Long chatId, Long userId);
    // make consistent
    
    @Query(value = "SELECT cr.chat_id FROM chat_relationship cr " +
            "WHERE cr.chat_id NOT IN ( " +
                "SELECT chat_id FROM chat_relationship " +
                "WHERE userid NOT IN (?1) " +
            ") " +
            "GROUP BY cr.chat_id " +
            "HAVING COUNT(DISTINCT cr.userid) = ?2", nativeQuery = true)
Long findChatIdByUserIds(List<Integer> userIds, int numberOfUsers);



	List<ChatRelationship>findAllByuserID(Integer i);

	
	@Query(value = "SELECT chat_name FROM chat_relationship WHERE chat_id = ?1 LIMIT 1", nativeQuery = true)
	String getChatNameByChatId(Long id);

	List<ChatRelationship> findAllBychatId(long id);

	Optional<ChatRelationship> findByChatIdAndUserID(Long chatId, Integer userId);

	@Query(value = "SELECT * from chat_relationship WHERE chat_id = ?1 AND userid = ?2", nativeQuery = true)
	ChatRelationship getByChatIdAnduserID(Long chatId, Integer userID);
	

 
}
