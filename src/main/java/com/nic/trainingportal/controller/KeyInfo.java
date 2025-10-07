package com.nic.trainingportal.controller;


public class KeyInfo {
    private final String keyId;
    private final String publicKey;

    public KeyInfo(String keyId, String publicKey) {
        this.keyId = keyId;
        this.publicKey = publicKey;
    }

    public String getKeyId() { return keyId; }
    public String getPublicKey() { return publicKey; }
}

