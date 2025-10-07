package com.nic.trainingportal.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

    // Generates a random 16-byte salt
    public static String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash the password with the salt using PBKDF2 with 512-bit key length
    public static String hashPassword(String password, byte[] salt) throws Exception {
        int iterations = 10000;
        int keyLength = 512; // bits (64 bytes)
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }



    // Generate salt + hash password and return both as Base64 strings
    public static String hashPasswordWithSalt(String value, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String input = value + salt;
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Hashing error", e);
        }
    }

    // Verify input password against stored hash and salt
    public static boolean verifyPassword(String inputPassword, String storedHash, String storedSaltBase64) throws Exception {
        byte[] salt = Base64.getDecoder().decode(storedSaltBase64);
        String hashOfInput = hashPassword(inputPassword, salt);
        return hashOfInput.equals(storedHash);
    }

    // Simple container class for holding salt and hash
    public static class HashSalt {
        public final String salt;
        public final String hash;
        public HashSalt(String salt, String hash) {
            this.salt = salt;
            this.hash = hash;
        }
    }





    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Hash password
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    // Verify password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }





    // Example usage
  public void example(){
      String password = "mySecretPassword";
//
//        // Generate salt and hash
//        HashSalt result = generateHashAndSalt(password);
//        System.out.println("Salt (Base64): " + result.salt);
//        System.out.println("Hashed Password (Base64): " + result.hash);
//
//        // Verify password (should be true)
//        boolean isValid = verifyPassword("mySecretPassword", result.hash, result.salt);
//        System.out.println("Password verified: " + isValid);
//
//        // Verify with wrong password (should be false)
//        boolean isInvalid = verifyPassword("wrongPassword", result.hash, result.salt);
//        System.out.println("Password verified with wrong password: " + isInvalid);
  }
}


