package com.mmu6.mmu6.Respiritory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mmu6.mmu6.Class.Chat;

/*
 * Initialising the Repository for the chatRespiritory class and controller
 * passing through the Chat class alongside a Long data type
 */
public interface chatRespiritory extends JpaRepository<Chat, Long> {
	// Spring boot JPA function to return a list of the Chat class based on chatId
	List<Chat> findAllByChatId(Long id);
	// Native sql execution linked to a function
	@Query(value = "SELECT * FROM chat WHERE chat_id = (?1) ORDER BY time DESC LIMIT 1", nativeQuery = true)
	/*
	 *  The function itself returns the most recent chat object saved in a conversation
	 *  with the ?1 being the placeholder for the variable passed through the function
	 */
    Chat findLatestMessageInChat(Long id);

}
