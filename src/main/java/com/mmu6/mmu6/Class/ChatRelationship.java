 package com.mmu6.mmu6.Class;

import javax.persistence.*;

@Entity
@Table(name = "ChatRelationship")
public class ChatRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    private Integer userID;
    
    private String chatName;
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


	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

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

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userId2) {
		this.userID = userId2;
	}


}
