package com.mmu6.mmu6.Controller;

import java.util.List;
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
import com.mmu6.mmu6.Class.EncrpytionClass;
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Respiritory.userRepository;
import org.json.JSONObject;
//utilising spring annotations to make this a rest controller
@RestController
//mapping the url to give request access to the class functionality
@RequestMapping("/api/login")
//allowing all requests
@CrossOrigin(origins = "*")
public class Login {
	// using the autowired annotation , other repository's are imported with their functionality
	@Autowired
    private EncrpytionClass encryptionService;
	
    @Autowired
    private userRepository userRepository;
    @Autowired
	private Authentication authenticationService;
    // function that handles a post request  , handling the login with the users details
    @PostMapping("/")
    public ResponseEntity<String> login(@RequestBody User currentUser){
    	JSONObject data = new JSONObject();
    	// check to see if the user actually exists
    	User existingUser = userRepository.findByEmail(currentUser.getEmail());
    	// if teh user does exist
    	if (existingUser != null) {
    		// verify the provided password alongside the salt against the encrypted password in the database
    		if (encryptionService.verifyEncryptedString(currentUser.getPassword(), existingUser.getSalt(), existingUser.getPassword())) {
    			// generate and save auth token
    			String authToken = generateAuthToken();
    			existingUser.setAuthToken(authToken);
    			userRepository.save(existingUser);
    			// create object with auth token and user id for the front end to process
    			data.put("auth_token", authToken);
    			data.put("user_id", existingUser.getId());
    			// return the data
    			return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    		}
    		else {
    			// if the check does not pass return error
    			data.put("error", "incorrect credentials");
    			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
    			}
    		}
    	// error if user does not exist
    	data.put("error", "no user found with credentials");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
        
    }
    

    // function to generate auth token
    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
    
  
}

