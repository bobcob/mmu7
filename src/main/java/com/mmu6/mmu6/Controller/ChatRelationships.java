package com.mmu6.mmu6.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import com.mmu6.mmu6.Class.AdminCheck;
import com.mmu6.mmu6.Class.ChatRelationship;
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userRepository;
import com.mmu6.mmu6.Respiritory.chatRespiritory;
import com.mmu6.mmu6.Respiritory.chatRelationshipRespiritory;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/chats/relationships")
@CrossOrigin(origins = "*")
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
    	Integer groupCreator = (Integer) requestData.get("creatorID");
    	
    	String title = requestData.get("title").toString();
        Long chatId = Long.parseLong(requestData.get("chatId").toString());
        List<Integer> userIds = (List<Integer>) requestData.get("userID");
        Long chatIds = chatRelationshipRespiritory.findChatIdByUserIds(userIds, userIds.size());
        if (chatIds == null) {
        	for (Integer userId : userIds) {
                ChatRelationship chatRelationship = new ChatRelationship();
                chatRelationship.setChatName(title);
                chatRelationship.setChatId(chatId);
                chatRelationship.setUserID(userId);
                if (groupCreator.equals(userId)) {
                	chatRelationship.setAdmin("true");
                	chatRelationship.setGroupCreator("true");
                }else {
                	chatRelationship.setAdmin("false");
                	chatRelationship.setGroupCreator("false");
                }
                chatRelationshipRespiritory.save(chatRelationship);
                data.put("chat", chatId);
            }
        	return ResponseEntity.status(HttpStatus.OK).body(data.toString());
        } else {
        	data.put("chat", chatIds.toString());
        	return ResponseEntity.status(HttpStatus.FOUND).body(data.toString());
        } 
  
    }
    
    @PostMapping("/addUserToChat")
    public ResponseEntity<String> addUserToChat(@RequestBody ChatRelationship chatRelationship) {
    	
    	JSONObject data = new JSONObject();
    	Optional<ChatRelationship> chatRelationshipOptional = chatRelationshipRespiritory.findByChatIdAndUserID(
                chatRelationship.getChatId(),
                chatRelationship.getUserID()
        );
    	 if (chatRelationshipOptional.isPresent()) {
    		 data.put("error", "user already in chat");
    		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString()); 		 
    	 }
    	 else {
    		 chatRelationshipRespiritory.save(chatRelationship);
    		 data.put("chat", chatRelationship.getChatId());;
    		 return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    	 }
    	
    	
    
    	
    }
    
    @PostMapping("/admin/check")
    public ResponseEntity<String> newAdmin(@RequestBody AdminCheck request) {
        JSONObject data = new JSONObject();
        Optional<ChatRelationship> chatRelationshipOptional = chatRelationshipRespiritory.findByChatIdAndUserID(request.getChatID() ,request.getId());
        if (chatRelationshipOptional.isPresent()) {
            ChatRelationship checkRelationship = chatRelationshipOptional.get();
            if (checkRelationship.getAdmin().equals("true") && checkRelationship.getGroupCreator().equals("true")) {
                data.put("user", "Creator");
                return ResponseEntity.status(HttpStatus.OK).body(data.toString());
            }
            if (checkRelationship.getAdmin().equals("true")) {
                data.put("user", "Admin");
                return ResponseEntity.status(HttpStatus.OK).body(data.toString());
            } else {
                data.put("user", "User");
                return ResponseEntity.status(HttpStatus.OK).body(data.toString());
            }
        } else {
            data.put("error", "Relationship not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
    }

    
    
    
    
    
    @PostMapping("/admin")
    public ResponseEntity<String> newAdmin(@RequestBody ChatRelationship chatRelationship) {
        JSONObject data = new JSONObject();
        ChatRelationship newAdmin = chatRelationshipRespiritory.getByChatIdAnduserID(chatRelationship.getChatId(), chatRelationship.getUserID());
        if (newAdmin == null) {
            data.put("message", "Chat relationship not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
        newAdmin.setAdmin("true");
        chatRelationshipRespiritory.save(newAdmin);
        data.put("message", "success");
        return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    }
    
    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<String> removeUserFromChat(@RequestBody ChatRelationship chatRelationship) {
        JSONObject data = new JSONObject();
        Optional<ChatRelationship> chatRelationshipOptional = chatRelationshipRespiritory.findByChatIdAndUserID(
                chatRelationship.getChatId(),
                chatRelationship.getUserID()
        );

        if (chatRelationshipOptional.isPresent()) {
            chatRelationshipRespiritory.delete(chatRelationshipOptional.get());
            data.put("message", "Success");
            return ResponseEntity.status(HttpStatus.OK).body(data.toString());
        } else {
            data.put("message", "Chat relationship not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
    }

    
    
        
  	
    
  
}

