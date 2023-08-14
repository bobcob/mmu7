package com.mmu6.mmu6.Class;
/** Class to hold the parameters sent in the relevant request */
public class AdminCheck {
    Integer id;
    /** Store id for later reference  */
    Long chatId;
    /** Store chatId */

    /** getId allows the classes id variable to be returned */
    public Integer getId() {
        return id;
    }
    /** setId allows the id variable to be set externally when the class is called */
    public void setId(Integer id) {
        this.id = id;
    }
    /** getChatID allows the classes chatID variable to be returned */
    public Long getChatId() {
        return chatId;
    }
    /** setChatID allows the id variable to be set externally when the class is called */
    public void setChatID(Long chatId) {
        this.chatId = chatId;
    }
}