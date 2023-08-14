 package com.mmu6.mmu6.Class;

import javax.persistence.*;
//Creating a table in the database called userContacts
@Entity
@Table(name = "userContacts")
public class userContacts {
	// Initialising the column user_contactsid in the database as primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userContactsID;
    // generating variable user1Id
    private int user1Id;
    /*
     *  generating variable user2Id , the two integers are used to represent
     *  the relationship that has been formed between the two contacts
     */
    private int user2Id;
    // generating variable dateAdded to represent when the relationship was formed
    private String dateAdded;
    // generating relevant getters and setters
	public int getUser1Id() {
		return user1Id;
	}
	public void setUser1Id(int user1Id) {
		this.user1Id = user1Id;
	}
	public int getUser2Id() {
		return user2Id;
	}
	public void setUser2Id(int user2Id) {
		this.user2Id = user2Id;
	}
	public String getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
	


	

}

