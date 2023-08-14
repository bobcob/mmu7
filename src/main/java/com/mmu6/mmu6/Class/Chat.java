 package com.mmu6.mmu6.Class;

import javax.persistence.*;

 // Chat class that represents a message that has been succesfully sent
 
/*
 * Using spring boots in built functionality it is declared here as an entity and a table
 * As a result the class generates a table called "Chat" in the database with
 * the same properties as the class
 */
@Entity
@Table(name = "Chat")
public class Chat {
	/*
	 * These two lines create a Id primary key in the table 
	 * which auto increments by 1 with every addition
	 */
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // specifically naming a column "chat_id"
    @Column(name = "chat_id")
    // declaring all variables with the appropriate data type and name
    private Long chatId;
    private Long organisationId;
	private Long userId;
    private String message;
    private String time;
    private String name;
    private String title;
    // generating relevant getters and setters
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
	public Long getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Long organisationId) {
		this.organisationId = organisationId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    

}
