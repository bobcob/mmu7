package com.mmu6.mmu6.Controller;

import java.util.List;
import java.util.Optional;

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
import com.mmu6.mmu6.Class.Contact;
import com.mmu6.mmu6.Class.userContacts;
import com.mmu6.mmu6.Respiritory.userContactRespiritory;
import com.mmu6.mmu6.Respiritory.userRepository;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class userController {
    @Autowired
    private userRepository userRepository;
    
    @PutMapping("/")
    public ResponseEntity<String> updateUser(@RequestBody User newUser , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException {
    	Authentication(auth , uid);
    	JSONObject data = new JSONObject();
    	Optional<User> user = userRepository.findById(newUser.getId());
    	if (user.isPresent()) {
    		newUser.setAuthToken(auth);
    		userRepository.save(newUser);
    		data.put("success", "user updated");
    		return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    	}
    	else {
    		data.put("error", "user not updated");
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
    	}
    }

    @GetMapping("/{id}")
    public Contact getUserByID(@PathVariable Long id , @RequestHeader("X-Authorization") String auth, @RequestHeader("Uid") Long uid) throws AuthenticationException{
    	Authentication(auth , uid);

        Optional<User> newUser = userRepository.findById(id);
        if (newUser.isPresent()) {
            User user = newUser.get();
            Contact userSummary = new Contact(user.getId(), user.getName(), user.getEmail());
            return userSummary;
        } else {
            return null;
        }
    }
    
    @PostMapping("/")
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        User existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser != null) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email already exists");
        } else {
            userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.OK).body("User added");
        }
    }

    
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
    	userRepository.deleteById(id);
    	return "User deleted";
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


