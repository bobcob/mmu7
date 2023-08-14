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
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userRepository;
import com.mmu6.mmu6.Respiritory.chatRespiritory;
import com.mmu6.mmu6.Respiritory.organisationRespiritory;
import com.mmu6.mmu6.Respiritory.chatRelationshipRespiritory;
import org.json.JSONObject;
// utilising spring annotations to make this a rest controller
@RestController
//mapping the url to give request access to the class functionality
@RequestMapping("/api/chats/relationships")
// allowing all requests
@CrossOrigin(origins = "*")
public class ChatRelationships {
	
	// using the autowired annotation , other repository's are imported with their functionality
    @Autowired
    private userRepository userRepository;
    @Autowired
    private chatRelationshipRespiritory chatRelationshipRespiritory;
    @Autowired
    private organisationRespiritory organisationRespiritory;
	@Autowired
	private Authentication authenticationService;
    
    // mapping the url added to the original one declared , and the method in this case get
    @GetMapping("/")
    // function which returns the last chatID + 1
    public ResponseEntity<String> getLastChatID(@RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	// declariug a empty json object to manipulate and output
    	JSONObject data = new JSONObject();
    	// empty Integer variable
    	Integer lastID;
    	try { 
    		// using the repository to get the last id and add one
            lastID = chatRelationshipRespiritory.getLastChatId();
            lastID += 1;
        } catch (Exception e) {
            lastID = 1;
        }
    	// putting the data into the json object
    	data.put("int", lastID);
    	// returning it alongside a status code for the front end to proccess
    	return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    }

    // declaring the url alongside the request method
    @PostMapping("/")
    /*
     *  declaring the function alongside its return value "ResponseEntity<String>"
     *  It also can accept a string and a object in the incoming request
     */
    public ResponseEntity<String> newChat(@RequestBody Map<String, Object> requestData , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	// new json object
        JSONObject data = new JSONObject();
        // pulling the relevant paramaters from the request
        Integer groupCreator = (Integer) requestData.get("creatorID");
        Integer groupID = (Integer) requestData.get("groupID");
        String title = requestData.get("title").toString();
        Long chatId = Long.parseLong(requestData.get("chatId").toString());
        // getting a list of chatids that are sent in the post request body
        List<Integer> userIds = (List<Integer>) requestData.get("userID");
        // doing a check which sees if there is already a existing chat with the specific users in
        Long chatIds = chatRelationshipRespiritory.findChatIdByUserIds(userIds, userIds.size());
        // if there is no chat previously or if there is a group id present the code carries on
        if (chatIds == null || groupID != null) {
        	// looping through the list of userIds as user id
            for (Integer userId : userIds) {
            	// checking if the user ids attempting to form a relationship actually exist
        		if (!userRepository.findById(userId.longValue()).isPresent()) {     
        			// if they dont throw an error
                    data.put("error", "User " + userId + " does not exist");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data.toString());
                }
            }
            // if group id is present a check is done to see if the user being added to chat is in the group
            if (groupID != null) {
            	// getting a list of all the users in the group
                List<Integer> usersInGroup = getUsersInGroup(groupID.longValue());
                // looping through them
                for (Integer userId : userIds) {
                	// if the user id is not in the group
                    if (!usersInGroup.contains(userId)) {
                    	// throw an error
                        data.put("error", "User " + userId + " not in group");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data.toString());
                    }
                }
            }
            // if all the previous checks are subsequently passed , create the chat relationship
            for (Integer userId : userIds) {
            	// for each userID create new chat relationship with all the sent data
                data = createChatRelationship(data, groupCreator, groupID, title, chatId, userId);
            }
            // return status ok with the chat id number
            return ResponseEntity.status(HttpStatus.OK).body(data.toString());
        } else {
        	/*
        	 *  here the code is caught if a chat already exists 
        	 *  the chat id of the previously formed chat is returned
        	 */
            data.put("chat", chatIds.toString());
            return ResponseEntity.status(HttpStatus.FOUND).body(data.toString());
        }
    }


    // private function which creates a chat relationship , taking in all the paramaters
    private JSONObject createChatRelationship(JSONObject data, Integer groupCreator, Integer groupID, String title, Long chatId, Integer userId) {
    	// if user is present in the database
        if (userRepository.findById(userId.longValue()).isPresent()) {
        	// create the chat relationship and set the paramaters
            ChatRelationship chatRelationship = new ChatRelationship();
            chatRelationship.setOrganisationId(groupID);
            chatRelationship.setChatName(title);
            chatRelationship.setChatId(chatId);
            chatRelationship.setUserId(userId);
            setAdminAndGroupCreator(chatRelationship, groupCreator, userId);
            // save it and put the chatid into the json response
            chatRelationshipRespiritory.save(chatRelationship);
            data.put("chat", chatId);
        }
        // return data
        return data;
    }
    // private function that sets the creator and admin tiers
    private void setAdminAndGroupCreator(ChatRelationship chatRelationship, Integer groupCreator, Integer userId) {
    	// if the user is the group creator set both admin paramaters
        if (groupCreator.equals(userId)) {
            chatRelationship.setAdmin("true");
            chatRelationship.setGroupCreator("true");
        } else {
        	// else set them to false
            chatRelationship.setAdmin("false");
            chatRelationship.setGroupCreator("false");
        }
    }

    // function which returns a list of all users in group for reference
    public List<Integer> getUsersInGroup(Long id) {
    	// setting empty lists to store data
    	JSONObject data = new JSONObject();
    	List<Integer> userIDs = new ArrayList<>();
    	// returns a list of organisations by the organisationId
    	List<Organisations> organisations = organisationRespiritory.findAllByorganisationId(id);
    	for (Organisations org : organisations) {
    		// adding the respective id's to the list
    		userIDs.add(org.getUserId());	
    	}
    	// putting the data into a json array 
    	data.put("users in group", userIDs);
    	// returning it
    	return userIDs;
    	
    	
    }
    
    // declaring the request type and url 
    @PostMapping("/addUserToChat")
    public ResponseEntity<String> addUserToChat(@RequestBody ChatRelationship chatRelationship , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	JSONObject data = new JSONObject();
    	// optional object potentially returning a ChatRelationship based on the chatid and user id
    	Optional<ChatRelationship> chatRelationshipOptional = chatRelationshipRespiritory.findByChatIdAndUserId(
                chatRelationship.getChatId(),
                chatRelationship.getUserId()
        );
    	// if the organisation id of the chat relationship sent in the request dosent equal null
    	if (chatRelationship.getOrganisationId() != null) {
    		// get list of users in group
    		List <Integer> usersInGroup = getUsersInGroup(chatRelationship.getOrganisationId().longValue());
    		// if the user is not in group
    		 if (!usersInGroup.contains(chatRelationship.getUserId())) {
    			 // return error
    			 data.put("error", "user not in group");
        		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString()); 
    		 }
    		
    		
    	}
    	// if the user is already in the chat
    	 if (chatRelationshipOptional.isPresent()) {
    		 data.put("error", "user already in chat");
    		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString()); 		 
    	 }
    	 else {
    		 // else save the new relationship
    		 chatRelationshipRespiritory.save(chatRelationship);
    		 data.put("chat", chatRelationship.getChatId());;
    		 return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    	 }
    	
    	
    
    	
    }
    
    // declaring the url and request type
    @PostMapping("/admin/check")
    public ResponseEntity<String> newAdmin(@RequestBody AdminCheck request , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
        JSONObject data = new JSONObject();
        // optional chat relationship object based on chat id and user id
        Optional<ChatRelationship> chatRelationshipOptional = chatRelationshipRespiritory.findByChatIdAndUserId(request.getChatId() ,request.getId());
        // if the relationship is present
        if (chatRelationshipOptional.isPresent()) {
        	// get the object
            ChatRelationship checkRelationship = chatRelationshipOptional.get();
            // conditional checks that see what credientials the user has and returns accordingly
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
        	// error handling
            data.put("error", "Relationship not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
    }

    
    
    
    
    // Declaring the request method and url
    @PostMapping("/admin")
    public ResponseEntity<String> newAdmin(@RequestBody ChatRelationship chatRelationship ,@RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
        JSONObject data = new JSONObject();
        // getting the existing chat relationship
        ChatRelationship newAdmin = chatRelationshipRespiritory.getByChatIdAnduserId(chatRelationship.getChatId(), chatRelationship.getUserId());
        if (newAdmin == null) {
        	// if not present return error
            data.put("message", "Chat relationship not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
        // else set admin and save
        newAdmin.setAdmin("true");
        chatRelationshipRespiritory.save(newAdmin);
        data.put("message", "success");
        return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    }
    
    // setting the request type and url
    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<String> removeUserFromChat(@RequestBody ChatRelationship chatRelationship , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
        JSONObject data = new JSONObject();
        // getting an optional object by chatid and userid
        Optional<ChatRelationship> chatRelationshipOptional = chatRelationshipRespiritory.findByChatIdAndUserId(
                chatRelationship.getChatId(),
                chatRelationship.getUserId()
        );
        // if object is present
        if (chatRelationshipOptional.isPresent()) {
        	// delete it
            chatRelationshipRespiritory.delete(chatRelationshipOptional.get());
            data.put("message", "Success");
            return ResponseEntity.status(HttpStatus.OK).body(data.toString());
        } else {
        	// throw error
            data.put("message", "Chat relationship not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data.toString());
        }
    }

    
    
        
  	
    
  
}

