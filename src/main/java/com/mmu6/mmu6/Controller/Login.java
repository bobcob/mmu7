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
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Respiritory.userRepository;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "http://localhost:19007")
public class Login {
	
    @Autowired
    private userRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<String> login(@RequestBody User currentUser) {
    	JSONObject data = new JSONObject();
    	User existingUser = userRepository.findByEmail(currentUser.getEmail());
    	if (existingUser != null) {
    		String savedUserPass = existingUser.getPassword();
    		if (savedUserPass.equals(currentUser.getPassword())) {
    			String authToken = generateAuthToken();
    			existingUser.setAuthToken(authToken);
    			userRepository.save(existingUser);
    			data.put("auth_token", authToken);
    			data.put("user_id", existingUser.getId());
    			return ResponseEntity.status(HttpStatus.OK).body(data.toString());
    		}
    		else {
    			data.put("error", "incorrect credentials");
    			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
    			}
    		}
    	data.put("error", "incorrect credentials");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data.toString());
        
    }
    

    
    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
    
  
}

