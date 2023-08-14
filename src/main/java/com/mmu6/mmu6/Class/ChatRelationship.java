 package com.mmu6.mmu6.Class;

import javax.persistence.*;
// Creating a table in the database called ChatRelationship
@Entity
@Table(name = "ChatRelationship")
public class ChatRelationship {
	// creating the primary key with a auto incrementing value called id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // creating a column specifically named chat_id
    @Column(name = "chat_id")
    private Long chatId;
    // declaring all variables with the appropriate data type and name
    @Column(name = "userid")
    private Integer userId;
    private Integer organisationId;
    private String chatName;
    private String admin;
    private String groupCreator;
    // generating the relevant getters and setters for the class
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getChatId() {
		return chatId;
	}
	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
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
