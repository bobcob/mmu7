package com.mmu6.mmu6.Respiritory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmu6.mmu6.Class.userContacts;
/*
 * Initialising the Repository for the userContacts class and controller
 * passing through the userContacts class alongside a Long data type
 */
public interface userContactRespiritory extends JpaRepository<userContacts, Long> {
	// Spring boot JPA function to return a list of userContacts based on the user1Id parameter
	List<userContacts> findAllByUser1Id(int user1ID);
	/*
	 *  Spring boot JPA returns a userContacts object which its
	 *  presence can be checked . The object is found via both the user1Id parameters and
	 *   user2Id
	 */
	Optional<userContacts> findByUser1IdAndUser2Id(int user1Id, int user2Id);
	/*
	 *  Spring boot JPA function which deletes a userContacts object 
	 *  based on user1Id and user2Id
	 */
	void deleteByUser1IdAndUser2Id(int user1Id, int user2Id);
}
