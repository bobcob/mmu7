 package com.mmu6.mmu6.Class;

import javax.persistence.*;

@Entity
@Table(name = "ChatPreview")
public class ChatPreview {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    private String Name;
    public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	private String lastMessage;
    private String time;
	public Long getChatId() {
		return chatId;
	}
	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

    
	
    

	

}

