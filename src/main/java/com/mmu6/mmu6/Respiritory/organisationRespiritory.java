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
import com.mmu6.mmu6.Class.Organisations;
import com.mmu6.mmu6.Class.userContacts;

/*
 * Initialising the Repository for the Organisations class and controller
 * passing through the Organisations class alongside a Long data type
 */
public interface organisationRespiritory extends JpaRepository<Organisations, Long> {
    @Query(value = "SELECT MAX(organisation_id) FROM organisations" , nativeQuery = true)
    Integer getLastOrganisationId();

    /*
     *  native sql statment which selects a chat id by user ids in a group which is
     *  not an organisation. The amount of userids has to be specific and match those in the
     *   relatiionship exactly
     */
    @Query(value = "SELECT chat_id FROM chat_relationship " +
            "WHERE chat_id NOT IN ( " +
                "SELECT chat_id FROM chat_relationship " +
                "WHERE userid NOT IN (?1) " +
            ") AND (organisation_id IS NULL)" +
            "GROUP BY chat_id " +
            "HAVING COUNT(DISTINCT userid) = ?2", nativeQuery = true)
Long findChatIdByUserIds(List<Integer> userIds, int numberOfUsers);


    // Spring boot JPA function to return a list Organisations by user ID 
	List<Organisations>findAllByuserId(Integer i);
	// Spring boot JPA function to return a list Organisations by organisation id 
	List<Organisations>findAllByorganisationId(Long i);
	// Native sql execution linked to a function
	@Query(value = "SELECT chat_name FROM chat_relationship WHERE chat_id = ?1 LIMIT 1", nativeQuery = true)
	// function which gets chat name by organsiation id , where ?1 is the parameter passed
	String getChatNameByOrganisation_id(Long id);
	
	// Spring boot JPA function to return a list of ChatRelationship by organisation id
	List<ChatRelationship> organisationId(long id);

	Optional<ChatRelationship> findByOrganisationIdAndUserId(Long organisationId, Integer userId);
	// Native sql execution linked to a function
	@Query(value = "SELECT * FROM organisations WHERE userid = ?1 AND organisation_id = ?2", nativeQuery = true)
	// function which finds optional organisation based on organisation and userId
	Optional<Organisations> findOrgByOrganisationIdAndUserId(Integer integer , Long organisationId);
	// Native sql execution linked to a function
	@Query(value = "SELECT * from chat_relationship WHERE chat_id = ?1 AND userid = ?2", nativeQuery = true)
	// function which finds ChatRelationship based on chatid and userId
	ChatRelationship getByorganisationIdAnduserId(Long chatId, Integer userID);
	

 
}
