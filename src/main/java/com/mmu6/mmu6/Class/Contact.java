 package com.mmu6.mmu6.Class;

import javax.persistence.*;
// creating the contact class 
public class Contact {
	// declaring the relevant variables and their data types
	private Long id;
	private String name;
	private String email;
	private String admin;
	private String groupCreator;
	// creating a public constructor to create an object elsewheee
	public Contact(Long id, String name, String email, String admin, String groupCreator) {
		this.groupCreator = groupCreator;
		this.admin = admin;
        this.id = id;
        this.name = name;
        this.email = email;
    }
	// getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getGroupCreator() {
		return groupCreator;
	}

	public void setGroupCreator(String groupCreator) {
		this.groupCreator = groupCreator;
	}
	



	

}

