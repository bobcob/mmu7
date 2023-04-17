 package com.mmu6.mmu6.Class;

import javax.persistence.*;

public class Contact {
	
	private Long id;
	private String name;
	private String email;
	private String Admin;
	private String GroupCreator;
	public String getAdmin() {
		return Admin;
	}
	public void setAdmin(String admin) {
		Admin = admin;
	}
	public String getGroupCreator() {
		return GroupCreator;
	}
	public void setGroupCreator(String groupCreator) {
		GroupCreator = groupCreator;
	}
	public Long getId() {
		return id;
	}
	public Contact(Long id, String name, String email, String Admin, String GroupCreator) {
		this.GroupCreator = GroupCreator;
		this.Admin = Admin;
        this.id = id;
        this.name = name;
        this.email = email;
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



	

}

