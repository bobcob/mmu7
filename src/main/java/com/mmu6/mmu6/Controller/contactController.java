package com.mmu6.mmu6.Controller;

import java.util.List;
import java.util.Optional;

import javax.security.sasl.AuthenticationException;

import java.util.ArrayList;
import org.json.JSONObject;
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
import com.mmu6.mmu6.Class.Contact;
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userContactRespiritory;
import com.mmu6.mmu6.Respiritory.userRepository;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:19007")
public class contactController {
    @Autowired
    private userContactRespiritory userContactRespiritory;
    @Autowired
    private userRepository userRepository;
    
    @GetMapping("/{id}")
    public   ResponseEntity<List<Contact>> getAllContacts(@PathVariable int id , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
        Authentication(auth , uid);
    	List<Contact> userList = new ArrayList<>();
    	List<userContacts> userContactsList =  userContactRespiritory.findAllByUser1ID(id);
    	for (userContacts userContact : userContactsList) {
    		 Optional<User> userOptional = userRepository.findById((long) userContact.getUser2ID());
    		 userOptional.ifPresent(user -> {
    			 Contact userSummary = new Contact(user.getId(), user.getName(), user.getEmail());
    			 userList.add(userSummary);
    		 });
    		        
    	}
    	return ResponseEntity.status(HttpStatus.OK).body(userList);
    	
    }
    
    @PostMapping("/")
    public ResponseEntity<String> addContact(@RequestBody userContacts newContact , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	JSONObject data = new JSONObject();
    	Authentication(auth , uid);
    	Optional<userContacts> existingContact = userContactRespiritory.findByUser1IDAndUser2ID(newContact.getUser1ID(), newContact.getUser2ID());
    	if (existingContact.isPresent()) {
    		data.put("error", "Users already contacts");
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
    	}
    	
    	
    	//succesfull request
    	userContactRespiritory.save(newContact);
        userContacts swappedContact = new userContacts();
        swappedContact.setUser1ID(newContact.getUser2ID());
        swappedContact.setUser2ID(newContact.getUser1ID());
        swappedContact.setDateAdded(newContact.getDateAdded());
        userContactRespiritory.save(swappedContact);
        //
        
        data.put("success", "Contact added");
        return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    }
    
    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<String> deleteContact (@RequestBody userContacts deleteContact , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	JSONObject data = new JSONObject();
    	Authentication(auth , uid);
    	Optional<userContacts> existingContact = userContactRespiritory.findByUser1IDAndUser2ID(deleteContact.getUser1ID(), deleteContact.getUser2ID());
    	if (existingContact.isPresent()) {
    		userContactRespiritory.deleteByUser1IDAndUser2ID(deleteContact.getUser1ID(), deleteContact.getUser2ID());
    		userContactRespiritory.deleteByUser1IDAndUser2ID(deleteContact.getUser2ID(), deleteContact.getUser1ID());
    		data.put("success", "contact deleted");
    		return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    	}
    	
    	data.put("error", "user could not be deleted");
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
    	
    	
   
    }
    
    public String Authentication(String auth , Long id) throws AuthenticationException {
    	// passing auth enables access to api , needs to be more secure
    	User findUser = userRepository.findByauthToken(auth);
    	if (findUser.getId().equals(id)) {
    		return null;
    	}
    	else {
    		throw new AuthenticationException("Invalid authentication token");
    	}
    	
    
    }
    
    
}

