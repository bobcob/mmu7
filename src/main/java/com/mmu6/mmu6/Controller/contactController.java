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

import com.mmu6.mmu6.Class.Authentication;
import com.mmu6.mmu6.Class.ChatRelationship;
import com.mmu6.mmu6.Class.Contact;
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.chatRelationshipRespiritory;
import com.mmu6.mmu6.Respiritory.userContactRespiritory;
import com.mmu6.mmu6.Respiritory.userRepository;
//utilising spring annotations to make this a rest controller
@RestController
//mapping the url to give request access to the class functionality
@RequestMapping("/api/contacts")
//allowing all requests
@CrossOrigin(origins = "*")
public class ContactController {
	// using the autowired annotation , other repository's are imported with their functionality
    @Autowired
    private userContactRespiritory userContactRespiritory;
    @Autowired
    private userRepository userRepository;
    @Autowired
    private chatRelationshipRespiritory chatRelationshipRespiritory;
    @Autowired
	private Authentication authenticationService;
    
    @GetMapping("/{id}")
    // declaring a fucntion which returns a list of all contacts 
    public   ResponseEntity<List<Contact>> getAllContacts(@PathVariable int id , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	List<Contact> userList = new ArrayList<>();
    	List<userContacts> userContactsList =  userContactRespiritory.findAllByUser1Id(id);
    	// returning a list of all contacts based on the user1 ids
    	for (userContacts userContact : userContactsList) {
    		// optional object which returns optional user based on id2
    		 Optional<User> userOptional = userRepository.findById((long) userContact.getUser2Id());
    		 // if it is present get it
    		 if (userOptional.isPresent()) {
    			 User user = userOptional.get();
    			 // add the needed paramaters to the user sumarry object
    			 Contact userSummary = new Contact(user.getId(), user.getName(), user.getEmail() , ""  , "");
    			 // add it to the list of contacts
    			 userList.add(userSummary);
    		 }

    		        
    	}
    	// return list of contacts for rendering
    	return ResponseEntity.status(HttpStatus.OK).body(userList);
    	
    }
    
    // function that returns a list of contacts that are in the chat id given
    @GetMapping("/inchat/{id}")
    public   ResponseEntity<List<Contact>> getContactsInChat(@PathVariable long id , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	List<Contact> userList = new ArrayList<>();
    	// find all chat relationships based on chat id
    	List<ChatRelationship> contactsInChat =  chatRelationshipRespiritory.findAllBychatId(id);
    	// loop through all chat relationships
    	for (ChatRelationship chatRelationships : contactsInChat) {
    		// optional user object user id based on the user id in chat relationship 
    		 Optional<User> userOptional = userRepository.findById((long) chatRelationships.getUserId());
    		 if (userOptional.isPresent()) {
    			 // if present get the object 
    			 User user = userOptional.get();
    			 // take needed details and add to list
    			 Contact userSummary = new Contact(user.getId(), user.getName(), user.getEmail() ,chatRelationships.getAdmin() , chatRelationships.getGroupCreator() );
    			 userList.add(userSummary);
    		 };
    		        
    	}
    	// return data and status code
    	return ResponseEntity.status(HttpStatus.OK).body(userList);
    	
    }
    
    // function that creates a relationship between two contacts making them contacts with each other
    @PostMapping("/")
    public ResponseEntity<String> addContact(@RequestBody userContacts newContact , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	JSONObject data = new JSONObject();
    	// check that sees if the two users are already existing contacts
    	Optional<userContacts> existingContact = userContactRespiritory.findByUser1IdAndUser2Id(newContact.getUser1Id(), newContact.getUser2Id());
    	if (existingContact.isPresent()) {
    		// if they are throw error
    		data.put("error", "Users already contacts");
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
    	}
    	
    	
    	// if not create two way relationship between the two 
    	userContactRespiritory.save(newContact);
        userContacts swappedContact = new userContacts();
        swappedContact.setUser1Id(newContact.getUser2Id());
        swappedContact.setUser2Id(newContact.getUser1Id());
        swappedContact.setDateAdded(newContact.getDateAdded());
        userContactRespiritory.save(swappedContact);
        //
        
        data.put("success", "Contact added");
        return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    }
    
    // function that deletes relationship between the two contacts
    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<String> deleteContact (@RequestBody userContacts deleteContact , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	JSONObject data = new JSONObject();
    	Optional<userContacts> existingContact = userContactRespiritory.findByUser1IdAndUser2Id(deleteContact.getUser1Id(), deleteContact.getUser2Id());
    	// if the relationship is existing already delete it
    	if (existingContact.isPresent()) {
    		userContactRespiritory.deleteByUser1IdAndUser2Id(deleteContact.getUser1Id(), deleteContact.getUser2Id());
    		userContactRespiritory.deleteByUser1IdAndUser2Id(deleteContact.getUser2Id(), deleteContact.getUser1Id());
    		data.put("success", "contact deleted");
    		return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    	}
    	// if not throw error
    	data.put("error", "user could not be deleted");
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
    	
    	
   
    }
    
    
    
}

