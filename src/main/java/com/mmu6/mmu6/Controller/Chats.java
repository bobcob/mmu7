package com.mmu6.mmu6.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.security.sasl.AuthenticationException;

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

import com.mmu6.mmu6.Class.Authentication;
import com.mmu6.mmu6.Class.Chat;
import com.mmu6.mmu6.Class.ChatRelationship;
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userRepository;
import com.mmu6.mmu6.Respiritory.chatRelationshipRespiritory;
import com.mmu6.mmu6.Respiritory.chatRespiritory;
import org.json.JSONObject;
//utilising spring annotations to make this a rest controller
@RestController
//mapping the url to give request access to the class functionality
@RequestMapping("/api/chats")
//allowing all requests
@CrossOrigin(origins = "*")
public class Chats {
	// using the autowired annotation , other repository's are imported with their functionality
    @Autowired
    private chatRespiritory chatRespiritory;
    @Autowired
    private userRepository userRepository;
    @Autowired
    private chatRelationshipRespiritory chatRelationshipRespiritory;
    @Autowired
	private Authentication authenticationService;
    // mapping the url added to the original one declared , and the method in this case get
    @GetMapping("/{id}")
    public ResponseEntity<List<Chat>> getChat(@PathVariable Long id) {
    	// returns a list of chat objects based on chat id
        List<Chat> chats = chatRespiritory.findAllByChatId(id);
        return ResponseEntity.ok(chats);
    }
    
    @GetMapping("/{id}/title")
    public ResponseEntity<ChatRelationship> getChatTitle(@PathVariable Long id , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	// returning the chat info based on the first chatid
        ChatRelationship chatinfo = chatRelationshipRespiritory.findFirstByChatId(id);
        return ResponseEntity.ok(chatinfo);
    }

    @PostMapping("/")
    public ResponseEntity<String> newChat(@RequestBody Chat newChat , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	// initiating new chat
        JSONObject data = new JSONObject();
        // checking if the target user is real
        Optional<User> tempUserCheck = userRepository.findById(newChat.getUserId());
        if (tempUserCheck.isPresent()) {
        	// get the object if present
            User user = tempUserCheck.get();
            // set the name
            newChat.setName(user.getName());
            // save the chat
            chatRespiritory.save(newChat);
            data.put("success", "chat saved");
            return ResponseEntity.ok(data.toString());
        } else {
        	// throw error if user does not exist
            data.put("error", "user not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data.toString());
        }
    }
      
  
}

