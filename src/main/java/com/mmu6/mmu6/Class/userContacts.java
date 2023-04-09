 package com.mmu6.mmu6.Class;

import javax.persistence.*;

@Entity
@Table(name = "userContacts")
public class userContacts {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userContactsID;
    private int user1ID;
    private int user2ID;
    private String dateAdded;
	public int getUserContactsID() {
		return userContactsID;
	}
	public void setUserContactsID(int userContactsID) {
		this.userContactsID = userContactsID;
	}
	public int getUser1ID() {
		return user1ID;
	}
	public void setUser1ID(int user1id) {
		user1ID = user1id;
	}
	public int getUser2ID() {
		return user2ID;
	}
	public void setUser2ID(int user2id) {
		user2ID = user2id;
	}
	public String getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
    

	

}

