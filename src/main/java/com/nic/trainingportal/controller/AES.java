package com.nic.trainingportal.controller;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@Component
public class AES {


	private static KeyPair keyPair;

    // 🔹 Generate RSA Key Pair at Startup
    static {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate RSA Key Pair", e);
        }
    }

    // 🔹 Get Public Key (Base64 Encoded) - For Frontend
    public static String getPublicKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    // 🔹 Get Private Key (Base64 Encoded) - NEVER expose this in production
    public static String getPrivateKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    // 🔹 Encrypt Data using RSA Public Key
    public static String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

        String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);
//        System.out.println("🔹 Encrypted Base64: " + encryptedBase64); // Debugging
        return encryptedBase64;
    }

    // 🔹 Decrypt Data using RSA Private Key
    public static String decrypt(String encryptedMessage) throws Exception {
        if (encryptedMessage == null || encryptedMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Encrypted text is null or empty");
        }

        // 🔹 Ensure clean Base64 string
        encryptedMessage = encryptedMessage.replaceAll("\\s+", "");

        try {
//            System.out.println("🔹 Received Base64 for Decryption: " + encryptedMessage); // Debugging

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (javax.crypto.BadPaddingException e) {
            throw new RuntimeException("🚨 Decryption failed: Possible incorrect key or corrupted data.", e);
        }
    }
}
