package com.mmu6.mmu6.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.security.sasl.AuthenticationException;

import org.json.JSONObject;
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
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Class.Authentication;
import com.mmu6.mmu6.Class.Contact;
import com.mmu6.mmu6.Class.EncrpytionClass;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userContactRespiritory;
import com.mmu6.mmu6.Respiritory.userRepository;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class userController {
    @Autowired
    private userRepository userRepository;
    
    @Autowired
    private EncrpytionClass encryptionService;
    
	@Autowired
	private Authentication authenticationService;
    
    // function that updates the user info in the database
    @PutMapping("/")
    public ResponseEntity<String> updateUser(@RequestBody User newUser , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	JSONObject data = new JSONObject();
    	// checks if the user is present
    	Optional<User> user = userRepository.findById(newUser.getId());
    	if (user.isPresent()) {
    		// populates it and gets the relevant paramaters
    		User user2 = user.get();
    		String password = newUser.getPassword();
    		String salt = user2.getSalt();
    		// checks if the password provided mathces the one in the database
    		if (encryptionService.verifyEncryptedString(newUser.getPassword()  , user2.getSalt() , user2.getPassword())){
    		newUser.setAuthToken(auth);
    		newUser.setSalt(salt);
    		newUser.setPassword(user2.getPassword());
    		// updates user and returns message
    		userRepository.save(newUser);
    		
    		data.put("success", "user updated");
    		return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    		}
    		else {
    			// if password does not match throw error
    			System.out.println(newUser.getPassword());
    			System.out.println( user2.getSalt());
    			System.out.println(user2.getPassword());
        		data.put("error", "user not updated");
        		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
        	}
    	}
    	else {
    		// if user not found
    		data.put("error", "user not updated");
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
    	}
    }
    
    // function that gets user by id and returns the object if present
    @GetMapping("/{id}")
    public Contact getUserByID(@PathVariable Long id , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
        Optional<User> newUser = userRepository.findById(id);
        if (newUser.isPresent()) {
            User user = newUser.get();
            Contact userSummary = new Contact(user.getId(), user.getName(), user.getEmail() , "" , "");
            return userSummary;
        } else {
            return null;
        }
    }
    
    // function to add a new user
    @PostMapping("/")
    public ResponseEntity<String> addUser(@RequestBody User newUser){
    	// check if the email is already present in the database
        User existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser != null) {
        	// if exists throw error
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email already exists");
        } else {
        	// else generate relevant parameters and save user
        	String password = newUser.getPassword();
        	String salt = encryptionService.generateSalt();
        	String pepper = "12renruTrevilonaitsirhcmahgnitton0900";
        	String encryptedPassword = encryptionService.encryptStringWithSaltAndPepper(password , salt );
        	newUser.setSalt(salt);
        	newUser.setPassword(encryptedPassword);
        	newUser.setAuthToken(generateAuthToken());
            userRepository.save(newUser);
            System.out.println("Encrypted text: " + newUser.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body("User added");
        }
    }

    // delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id, @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	authenticationService.authenticate(auth, uid);
    	userRepository.deleteById(id);
    	return "User deleted";
    }
    // generate auth token
    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
    

}


