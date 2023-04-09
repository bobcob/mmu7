package com.mmu6.mmu6.Controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mmu6.mmu6.Class.User;
import com.mmu6.mmu6.Respiritory.userRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:19006")
public class userController {
    @Autowired
    private userRepository userRepository;

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
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

    @PutMapping("/{id}")
    public String updateUser(@RequestBody User newUser , @PathVariable Long id) {
    	Optional<User> user = userRepository.findById(id);
    	if (user.isPresent()) {
    		newUser.setId(id);
    		userRepository.save(newUser);
    		return "User updated";
    	}
    	else {
    		return ("User not found");
    	}
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
    	userRepository.deleteById(id);
    	return "User deleted";
    }
}

