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
