 package com.mmu6.mmu6.Class;

import javax.persistence.*;

public class Contact {
	
	private Long id;
	private String name;
	private String email;
	public Long getId() {
		return id;
	}
	public Contact(Long id, String name, String email) {
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

