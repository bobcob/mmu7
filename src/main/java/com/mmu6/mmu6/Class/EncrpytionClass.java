package com.mmu6.mmu6.Class;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
// using spring boot annotations to declare it as a service to be used elsewhere
@Service
public class EncrpytionClass {
	// initialising a spring security interface to use later 
    private final PasswordEncoder passwordEncoder;
    // constructor which allows new objects of the class to be created
    public EncrpytionClass() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    /*
     *  generating the salt , usually to be stored in the database alongside
     *  sensitive information
     */
    public String generateSalt() {
    	// creating a SecureRandom object , generating a random sequence of bytes
        SecureRandom random = new SecureRandom();
        // byte array of length 16 to hold the salt
        byte[] salt = new byte[16];
        // generates random bytes to put into the array
        random.nextBytes(salt);
        // converts the array into a string and returns it
        return Base64.getEncoder().encodeToString(salt);
    }
    // function which can be called to encrypt a string with a salt and pepper
    public String encryptStringWithSaltAndPepper(String text, String salt) {
    	/*
    	 *  locally stored pepper which is used in the encryption process to 
    	 *  further obfuscate the string
    	 */
    	String pepper = "12renruTrevilonaitsirhcmahgnitton0900";
    	// returning the encoded text , salt and pepper
        return passwordEncoder.encode(text + salt + pepper);
    }
    // function to verify if the raw string passed through is a match with a encrypted version
    public boolean verifyEncryptedString(String plainText, String salt, String encryptedText) {
    	// declaring the pepper once again
    	String pepper = "12renruTrevilonaitsirhcmahgnitton0900";
    	// returning the boolean value of if the text is a match with the stored encrypted version
        return passwordEncoder.matches(plainText + salt + pepper, encryptedText);
    }
}

