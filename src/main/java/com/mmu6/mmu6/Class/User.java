 package com.mmu6.mmu6.Class;

import javax.persistence.*;
//Creating a table in the database called projectUsers
@Entity
@Table(name = "projectUsers")
public class User {
	// creating the primary key with a auto incrementing value called id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // generating variable with the name of the user
    private String name;
    // generating variable with users email
    private String email;
    // generating variable with the name of the password
    private String password;
    // generating variable with the value of the salt 
    private String salt;
    // generating relevant getters and setters
    public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	private String authToken;
    
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
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    

	

}

