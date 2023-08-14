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

/*
 * Initialising the Repository for the ChatRelationship class and controller
 * passing through the ChatRelationship class alongside a Long data type
 */
public interface chatRelationshipRespiritory extends JpaRepository<ChatRelationship, Long> {
	// Native sql execution linked to a function
    @Query("SELECT MAX(chatId) FROM ChatRelationship ")
    Integer getLastChatId();

    /*
     *  native sql statment which selects a chat id by user ids in a group which is
     *  not an organisation. The amount of userids has to be specific and match those in the
     *   relatiionship exactly
     */
    @Query(value = "SELECT cr.chat_id FROM chat_relationship cr " +
            "WHERE cr.chat_id NOT IN ( " +
                "SELECT chat_id FROM chat_relationship " +
                "WHERE userid NOT IN (?1) " +
            ") AND (organisation_id IS NULL) " +
            "GROUP BY cr.chat_id " +
            "HAVING COUNT(DISTINCT cr.userid) = ?2", nativeQuery = true)
Long findChatIdByUserIds(List<Integer> userIds, int numberOfUsers);

    // Spring boot JPA function to return a list of the ChatRelationship class based on organisationId
    List<ChatRelationship>findAllByorganisationId(Integer id);
    // Spring boot JPA function to return a list of the ChatRelationship class based on userID
	List<ChatRelationship>findAllByuserId(Integer i);

	// Native sql execution linked to a function
	@Query(value = "SELECT chat_name FROM chat_relationship WHERE chat_id = ?1 LIMIT 1", nativeQuery = true)
	/*
	 *  The function itself returns a string containing the chat name based on the chatId
	 *  the ?1 in the query is replaced by the id parameter
	 */
	String getChatNameByChatId(Long id);
	 // Spring boot JPA function to return a ChatRelationship object finding the first chatRelationship based on chatId
	ChatRelationship findFirstByChatId(Long chatId);
	
	 // Spring boot JPA function to return a list of  all ChatRelationship objects by chatId
	List<ChatRelationship> findAllBychatId(long id);
	
	/*
	 *  Spring boot JPA function to return a optional object that can be determined as present or not
	 *  The object is found by chatId and UserID
	 */
	Optional<ChatRelationship> findByChatIdAndUserId(Long chatId, Integer userId);
	// Native sql execution linked to a function
	@Query(value = "SELECT * from chat_relationship WHERE chat_id = ?1 AND userid = ?2", nativeQuery = true)
	/*
	 * A ChatRelationship object is returned based on ChatId and userID
	 * With the parameters ?1 and ?2 replaced with the two parameters passed
	 */
	ChatRelationship getByChatIdAnduserId(Long chatId, Integer userID);
	// Spring boot JPA function that returns a list of ChatRelationship objects based on organisationId and UserID
	List<ChatRelationship> findAllByorganisationIdAndUserId(Integer organisationId , Integer userID);
	// Native sql execution linked to a function
    @Query(value = "SELECT DISTINCT chat_id FROM chat_relationship WHERE organisation_id = ?1", nativeQuery = true)
    // Returns a list of distinct chat ids based on organisation id
    List<Long> findDistinctChatIdsByOrganisationId(Integer organisationId);


 
}
