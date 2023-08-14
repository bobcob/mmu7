package com.mmu6.mmu6.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.security.sasl.AuthenticationException;

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
import com.mmu6.mmu6.Class.Authentication;
import com.mmu6.mmu6.Class.ChatRelationship;
import com.mmu6.mmu6.Class.Organisations;
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Class.Contact;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userRepository;
import com.mmu6.mmu6.Respiritory.chatRespiritory;
import com.mmu6.mmu6.Respiritory.chatRelationshipRespiritory;
import com.mmu6.mmu6.Respiritory.organisationRespiritory;
import org.json.JSONObject;


@RestController
@RequestMapping("/api/organisations")
@CrossOrigin(origins = "*")
public class OrganisationsController {
	
    @Autowired
    private chatRelationshipRespiritory chatRelationshipRespiritory;
    @Autowired
    private organisationRespiritory organisationRespiritory;
    @Autowired
    private userRepository userRepository;
    @Autowired
    private chatRespiritory chatRespiritory;
	@Autowired
	private Authentication authenticationService;
    
    // function that returns last chat id
    @GetMapping("/")
    public ResponseEntity<String> getLastChatID(@RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
        JSONObject data = new JSONObject();
        int lastID;

        try {
            lastID = organisationRespiritory.getLastOrganisationId();
            lastID += 1;
        } catch (Exception e) {
            lastID = 1;
        }
        
        data.put("int", lastID);
        return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    }

    /*
     *  returns a list of all organisations by user id
     *  Effectively returning a list of all groups the user is a part of
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Organisations>> getAllOrganisations(@PathVariable Integer id , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	List<Organisations> orgs = organisationRespiritory.findAllByuserId(id);
    	return ResponseEntity.status(HttpStatus.OK).body(orgs);
    	
    }
    // function which returns a list of users in a given group
    @GetMapping("/group/{id}/users")
    public ResponseEntity<List<Contact>> getGroupUsers(@PathVariable Long id, @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	List<Contact> userList = new ArrayList<>();
    	// returns a list of organisations based on organisation id
    	List<Organisations> userContactsList =  organisationRespiritory.findAllByorganisationId(id);
    	// loop through the list of contacts
    	for (Organisations userContact : userContactsList) {
    		// if user exists
    		 Optional<User> userOptional = userRepository.findById((long) userContact.getUserId());
    		 if (userOptional.isPresent()) {
    			 // add relevant details to class and add it to the list
    			 User user = userOptional.get();
    			 Contact userSummary = new Contact(user.getId(), user.getName(), user.getEmail() , userContact.getAdmin() , userContact.getOrganisationsCreator());
    			 userList.add(userSummary);
    		 };
    		        
    	}
    	// return list of contacts in chat 
    	return ResponseEntity.status(HttpStatus.OK).body(userList);
    	
    	
    }
    
    // get message previews in particular group
    @GetMapping("/group/{id}/messages/{userID}")
    public ResponseEntity<String> getGroupMessages(@PathVariable Integer id, @PathVariable Integer userID, @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	JSONObject data = new JSONObject();
    	// find list of chats in given group id
        List<Long> chatIds = chatRelationshipRespiritory.findDistinctChatIdsByOrganisationId(id);
        List<Chat> chats = new ArrayList<>();
        if (!chatIds.isEmpty()) {
        	// iterate through the list
            for (Long chatId : chatIds) {
            	// logic to determine if the user is in the given group and how it should be rendered
            	Optional<ChatRelationship> chatRelationshipOptional = chatRelationshipRespiritory.findByChatIdAndUserId(chatId , userID);
    			if (chatRelationshipOptional.isPresent()) {
    				// if they are in group return the latest message
                Chat chat = chatRespiritory.findLatestMessageInChat(chatId);
                if (chat != null) {
                	chats.add(chat);
                }
    			}
            }
            // add data to the json object
            data.put("chats", chats);
            // return it
            return ResponseEntity.status(HttpStatus.OK).body(data.toString());
        } else {
        	// logic for if user is not in group , as they may have previously sent messages
        	data.put("chats", "");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(data.toString());
        }
    }

    // function to get users in a group
    public List<Integer> getUsersInGroup(Long id) {
    	JSONObject data = new JSONObject();
    	List<Integer> userIDs = new ArrayList<>();
    	// find all relationships within group
    	List<Organisations> organisations = organisationRespiritory.findAllByorganisationId(id);
    	for (Organisations org : organisations) {
    		// iterate through them and add to list of userIds
    		userIDs.add(org.getUserId());	
    	}
    	// return all userIds in group
    	data.put("users in group", userIDs);
    	return userIDs;
    	
    	
    }

    // function to create new organisation
    @PostMapping("/")
    public ResponseEntity<String> newOrganisation(@RequestBody Map<String, Object> requestData , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	JSONObject data = new JSONObject();
    	// pull relevant data form the incoming request
    	Integer groupCreator = (Integer) requestData.get("creatorID");
    	String name = requestData.get("Name").toString();
        Long orgID = Long.parseLong(requestData.get("orgID").toString());
    	List<Integer> userIds = (List<Integer>) requestData.get("userIDs");
    	List<Integer> usersInGroup = getUsersInGroup(orgID);
    	// iterate through the list of user ids sent
    	for (Integer userId : userIds) {
    		// create the new organisation relationships
    		Organisations organisation = new Organisations();
    		organisation.setName(name);
    		organisation.setOrganisationId(orgID);
    		organisation.setUserId(userId);
    		if (groupCreator.equals(userId)) {
    		organisation.setOrganisationsCreator("true");
    		organisation.setAdmin("true");
    		}
    		else {
    			organisation.setOrganisationsCreator("false");
    		}
    		organisationRespiritory.save(organisation);
    	}   
        data.put("message", "success");
        return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    }
    // function which allows adding a user to the group
    @PostMapping("/addUserToGroup")
    public ResponseEntity<String> addUserToChat(@RequestBody Organisations organisations, @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	JSONObject data = new JSONObject();
    	// checks if user is already in chat
    	Optional<Organisations> organisationsOptional = organisationRespiritory.findOrgByOrganisationIdAndUserId(organisations.getUserId() , organisations.getOrganisationId());
    	if (organisationsOptional.isPresent()) {
    		data.put("error", "user already in group");
   		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString()); 		 
   	 }
   	 else {
   		 // saves user in group
   		organisationRespiritory.save(organisations);
   		 data.put("message", "success");;
   		 return ResponseEntity.status(HttpStatus.OK).body(data.toString());
   	 }
   	
   	
    	
    }
    	
    	
    
    // fucntion which checks user credentials in given group
    @GetMapping("/admin/{id}/check/{id2}")
    public ResponseEntity<String> newAdmin(@PathVariable Integer id , @PathVariable Long id2, @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
        JSONObject data = new JSONObject();
        Optional<Organisations> organisationOptional = organisationRespiritory.findOrgByOrganisationIdAndUserId(id, id2);
        if (organisationOptional.isPresent()) {
        	Organisations checkRelationship = organisationOptional.get();
            if (checkRelationship.getOrganisationsCreator().equals("true")) {
                data.put("user", "Creator");
                return ResponseEntity.status(HttpStatus.OK).body(data.toString());
            } 
            if (checkRelationship.getAdmin().equals("true")) {
                data.put("user", "Admin");
                return ResponseEntity.status(HttpStatus.OK).body(data.toString());
                
            }else {
                data.put("user", "User");
                return ResponseEntity.status(HttpStatus.OK).body(data.toString());
            }
        } else {
            data.put("error", "Relationship not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
    }

    
    
    
    
    // function which makes user a admin
    @PostMapping("/admin")
    public ResponseEntity<String> newAdmin(@RequestBody Organisations organisation, @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
        JSONObject data = new JSONObject();
        Optional<Organisations> organisationOptional = organisationRespiritory.findOrgByOrganisationIdAndUserId(
                organisation.getUserId(),
                organisation.getOrganisationId()
        );
        // if present set admin paramater
        if (organisationOptional.isPresent()) {
            Organisations newAdmin = organisationOptional.get();
            newAdmin.setAdmin("true");
            organisationRespiritory.save(newAdmin);
            data.put("message", "success");
            return ResponseEntity.status(HttpStatus.OK).body(data.toString());
        } else {
            data.put("message", "Group relationship not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
    }
    // function which allows user to be removed from group
    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<String> removeUserFromChat(@RequestBody Organisations organisation, @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
        JSONObject data = new JSONObject();
        Optional<Organisations> organisationOptional = organisationRespiritory.findOrgByOrganisationIdAndUserId(
        		organisation.getUserId(),
        		organisation.getOrganisationId()
        );
        // logic which handles if a admin attempts to remove a creator
        if (organisationOptional.isPresent()) {
        	if (organisationOptional.get().getOrganisationsCreator().equals("true")) {
        		data.put("message", "Cannot remove Creator");
        		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(data.toString());
            }
        	else {
        		// deletes user
        		organisationRespiritory.delete(organisationOptional.get());
            data.put("message", "Success");
        	}
    
         return ResponseEntity.status(HttpStatus.OK).body(data.toString());
        } else {
        	// error handling
            data.put("message", "Organisation not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
    }

    
    
        
  	
    
  
}

