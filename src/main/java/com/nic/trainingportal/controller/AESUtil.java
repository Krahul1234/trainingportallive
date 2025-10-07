package com.nic.trainingportal.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
public class AESUtil{


    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAohJ8wWYha5/vSJRJI1rM3C5SYM6H8VGA1fsN9TB2bK3yU65bH0VvHdVEADwuwJLlkH4puxL/97fFFW1UgNr4fcgXLUpHqhsbaHOD9DGM5hdK2oIwFqznbM1xsfTdccodK1i5EkUIUDFq/J/OykEB35drh+uDS15KdmQRvG6d1c6zD9FCVXNfg0pfuXkKGEMLdXAgydLIwICZkBKp0sbEDDSfKH1yPOnckDUHFHJLwhxZAlbu4YWHtoUPYNDxutHaAr9vEAqYoENVqGGM4yGK2JgMmAaLEqvo9CK71fWtkf7k9/jyYAbF0BQL47jlSqBU4cKwU6ZVfI+X+I8UwQmNgQIDAQAB";
    private static final String BASE64_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCiEnzBZiFrn+9IlEkjWszcLlJgzofxUYDV+w31MHZsrfJTrlsfRW8d1UQAPC7AkuWQfim7Ev/3t8UVbVSA2vh9yBctSkeqGxtoc4P0MYzmF0ragjAWrOdszXGx9N1xyh0rWLkSRQhQMWr8n87KQQHfl2uH64NLXkp2ZBG8bp3VzrMP0UJVc1+DSl+5eQoYQwt1cCDJ0sjAgJmQEqnSxsQMNJ8ofXI86dyQNQcUckvCHFkCVu7hhYe2hQ9g0PG60doCv28QCpigQ1WoYYzjIYrYmAyYBosSq+j0IrvV9a2R/uT3+PJgBsXQFAvjuOVKoFThwrBTplV8j5f4jxTBCY2BAgMBAAECgf9uDR+piNrTZJvsLMTSZ6sJLRXC+9BTjNnEczUxyOFL6tHMVhiJ+MalpgBcKIGWl35KdLY20YVThaB4wZnKkDsr2IfkY7abS3d7oEokXCvQvFWXtSSc68uO60dzZKekchmx1WKA2rSfv5lmRnL543/iKRpbW7tLnOXzKQA3c5AOyGJ8V7TOe4FhvHZGNO+ZL/B8Ld1YaBMSUP2nhOYlFdnGMy3f4d4XpJy74nQz8SPXl+p/jgvg7cTR6DBKzCtO2m8w2t0AQ5WM1C7BKv8Ax6mVLQTgV9t7KmxBLT1NLbrDxdoNETJmOFAnyopLjhDXFE03vxTex9cPYKkJJe5Gn4sCgYEAyPD2wQIO8vMw7pc9m6LPQlJW3dCQAJFjXT5VCFRTCRt9jttcX84SW1Doz6u+LKiQ+PvRbvn35JINe5YtabVbBwkvRNtJ/QjSU3h4YYaYtOvvqx8U6DTjPhvHqbCEyS/6fNcdnkRveRblkX+Srb9ezMY5wVMlfDNqZxTR2IM6gosCgYEAznsNRnHLivPisS4Cq17vb0urCEaq+BQHyjrmS9ZYPzJriPNzbkRdN4yeKgatuc2adT6GqTzCb7kxnNM/7+9XS6tUr/DSZluuRc6wWQIMpE0cU6aN4vDlCPffISaLG+/hoJhDRyYFrtb47Z+nCRBRxIpn2TsfV63yy6XsaQZCLaMCgYEAnWDk0jwLe0rvex7nOLtmOtqEgIw2DMvSGBiWvsRUmKB4UWgLTFn41J5lCSnI8+8+RFPuL04GUnX1q2LuxIku9y7BX9ukclLzcD69ghzKA4F6n/rjBA9QH7fgRE4lYfF67UYKQ4f0CtHdEXzqY7yrwhRK7dhyVnlqj7ulsz7sl4sCgYBe0tHw5PeV05Tqy3d8XffVg8mC3nlLvl8pd00lzMTwGOabmToMjKbGiPRl1nVX290wNMvA0t90UXdATx+Qv7i2TnNw7UairT3mXxpLdg56MdNtcxK8Aucb7EFzRvEhnlNe3i4fcY2wkpb1AZnDJtR2Tx2L/IPTT1YuQf/E55PiwwKBgQCUlanjloHfpHBIQQY8ktlQ7z6gN000k8vSNJ8VVFZ17R1lE0nmXGKCNBqFSUoZOp1u9oF3pXit1DYdGpt+ccOHl/QEPXojIeKqruUG+fSHtDq7tejtj8hM+zKffoOhDDvprCRjGb3G885Rd1IkwZuOp2G5T9XKDEt2XZYYZJy/5g==";
    //private static final KeyPair keyPair;
    private static final KeyPair keyPair;

    private static final Cache<String, Boolean> processedMessages = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();


    // 🔹 Load RSA KeyPair from hardcoded Base64 strings
    static {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] publicBytes = Base64.getDecoder().decode(BASE64_PUBLIC_KEY);
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(publicBytes);
            PublicKey publicKey = keyFactory.generatePublic(pubSpec);

            byte[] privateBytes = Base64.getDecoder().decode(BASE64_PRIVATE_KEY);
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privateBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privSpec);

            keyPair = new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load fixed RSA Key Pair", e);
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
//    System.out.println("🔹 Encrypted Base64: " + encryptedBase64); // Debugging
        return encryptedBase64;
    }

    // 🔹 Decrypt Data using RSA Private Key
    public static String decrypt(String encryptedMessage) throws Exception {
        if (encryptedMessage == null || encryptedMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Encrypted text is null or empty");
        }
        // 🔹 Ensure clean Base64 string
        encryptedMessage = encryptedMessage.replaceAll("\\s+", "");
        if (processedMessages.getIfPresent(encryptedMessage) != null) {
//        throw new IllegalArgumentException("Unauthorized User");
            return "Unauthorized User";

        }
        try {
//        System.out.println("🔹 Received Base64 for Decryption: " + encryptedMessage); // Debugging

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            String decrypted = new String(decryptedBytes, StandardCharsets.UTF_8).trim();

            // 🔹 Only store after successful decryption
            processedMessages.put(encryptedMessage, true);

            return decrypted;
        } catch (javax.crypto.BadPaddingException e) {
            throw new RuntimeException("🚨 Decryption failed: Possible incorrect key or corrupted data.", e);
        }
    }
}