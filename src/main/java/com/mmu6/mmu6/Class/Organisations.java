 package com.mmu6.mmu6.Class;

import javax.persistence.*;
//Creating a table in the database called Organisations
@Entity
@Table(name = "Organisations")
public class Organisations {
	// creating the primary key with a auto incrementing value called id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // linking the name of the table to the variable
    @Column(name = "organisation_id")
    private Long organisationId;

    // declaring the userId stored as a Integer
    @Column(name = "userid")
    private Integer userId;
    // declaring the name of the organisation
    private String name;
    // value declaring if the user is the creator or not
    private String organisationsCreator;
    // value declaring if the user is the admin or not
    private String admin;
    // generating the relevant getters and setters for the class
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Long organisationId) {
		this.organisationId = organisationId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganisationsCreator() {
		return organisationsCreator;
	}
	public void setOrganisationsCreator(String organisationsCreator) {
		this.organisationsCreator = organisationsCreator;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}

    



}
