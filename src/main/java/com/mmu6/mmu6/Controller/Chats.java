package com.mmu6.mmu6.Controller;

import java.util.List;
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
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userRepository;
import com.mmu6.mmu6.Respiritory.chatRespiritory;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "http://localhost:19007")
public class Chats {
	
    @Autowired
    private chatRespiritory chatRespiritory;

    @PostMapping("/")
    public ResponseEntity<String> newChat(@RequestBody Chat newChat) {
    	
    	chatRespiritory.save(newChat);
        Chat swappedChat = new Chat();
		return  null;
        
    	
    }
    
   
    
    	
    
  
}

