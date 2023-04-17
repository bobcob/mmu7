package com.mmu6.mmu6.Controller;

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
import com.mmu6.mmu6.Respiritory.chatRelationshipRespiritory;
import com.mmu6.mmu6.Respiritory.chatRespiritory;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "*")
public class Chats {
	
    @Autowired
    private chatRespiritory chatRespiritory;
    @Autowired
    private userRepository userRepository;
    @Autowired
    private chatRelationshipRespiritory chatRelationshipRespiritory;
    
    @GetMapping("/{id}")
    public ResponseEntity<List<Chat>> getChat(@PathVariable Long id) {
    	
        List<Chat> chats = chatRespiritory.findAllByChatId(id);
        return ResponseEntity.ok(chats);
    }
    
    @GetMapping("/{id}/title")
    public ResponseEntity<String> getChatTitle(@PathVariable Long id) {
    	JSONObject data = new JSONObject();
        String chatName = chatRelationshipRespiritory.getChatNameByChatId(id);
        data.put("title", chatName);
        return ResponseEntity.ok(data.toString());
    }

    @PostMapping("/")
    public ResponseEntity<String> newChat(@RequestBody Chat newChat) {
        JSONObject data = new JSONObject();
        Optional<User> tempUserCheck = userRepository.findById(newChat.getUserID());
        if (tempUserCheck.isPresent()) {
            User tempUser = tempUserCheck.get();
            newChat.setName(tempUser.getName());
            chatRespiritory.save(newChat);
            data.put("success", "chat saved");
            return ResponseEntity.ok(data.toString());
        } else {
            data.put("error", "user not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data.toString());
        }
    }
    
   
    
    	
    
  
}

