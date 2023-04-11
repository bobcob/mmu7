package com.mmu6.mmu6.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmu6.mmu6.Class.Chat;
import com.mmu6.mmu6.Class.ChatRelationship;
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userRepository;
import com.mmu6.mmu6.Respiritory.chatRespiritory;
import com.mmu6.mmu6.Respiritory.chatRelationshipRespiritory;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/chats/relationships")
@CrossOrigin(origins = "http://localhost:19007")
public class ChatRelationships {
	
    @Autowired
    private chatRelationshipRespiritory chatRelationshipRespiritory;
    
    @GetMapping("/")
    public ResponseEntity<String> getLastChatID() {
    	JSONObject data = new JSONObject();
    	int lastID = chatRelationshipRespiritory.getLastChatId() + 1;
    	data.put("int", lastID);
    	return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    }

    @PostMapping("/")
    public ResponseEntity<String> newChat(@RequestBody Map<String, Object> requestData) {
    	JSONObject data = new JSONObject();
        Long chatId = Long.parseLong(requestData.get("chatId").toString());
        List<Integer> userIds = (List<Integer>) requestData.get("userID");
        List<Integer> chatIds = chatRelationshipRespiritory.findChatIdByUserIds(userIds, userIds.size());
        if (chatIds.isEmpty()) {
        	for (Integer userId : userIds) {
                ChatRelationship chatRelationship = new ChatRelationship();
                chatRelationship.setChatId(chatId);
                chatRelationship.setUserID(userId);
                chatRelationshipRespiritory.save(chatRelationship);
                data.put("chat", chatId);
            }
        	return ResponseEntity.status(HttpStatus.OK).body(data.toString());
        } else {
        	data.put("chat", chatIds);
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data.toString());
        } 
  
    }

    
    
        
  	
    
  
}

