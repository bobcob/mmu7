package com.mmu6.mmu6.Class;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu6.mmu6.Respiritory.userRepository;
/* Annotation at class level which provides pre built spring functionalities */
@Service
public class Authentication {
	/* Automatic dependency injection taking the userRepository and its functionality */
    @Autowired
    private userRepository userRepository;
/* Function authenticate taking two paramaters to check accordingly
 * It also throws an AuthenticationException in the case of any errors meaning that anyone trying to gain
 * unlawful access will be denied
 * */
    public void authenticate(String authToken, Long id) throws AuthenticationException {
    	/* using a function of the userRepository to try and find /return a User object based on
    	 * the auth token provided
    	 */
        User findUser = userRepository.findByauthToken(authToken);
        if (findUser == null) {
        	/*
        	 * If findUser returns null a error is thrown
        	 */
            throw new AuthenticationException("User not found with authentication token");
        }
        if (!findUser.getId().equals(id)) {
        	/* If the user is found a cross check is done between the auth token and the user id
        	 * if they do not match a error is thrown
        	 */
            throw new AuthenticationException("Invalid authentication token");
        }
    }
}
