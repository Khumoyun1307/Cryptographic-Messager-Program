package model;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the CryptoMessenger application.
 * Stores username, password hash, RSA keys, and inbox.
 */
public class User implements Serializable {
    private String username;
    private String passwordHash;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private List<Message> inbox;

    /**
     * Constructs a new User.
     *
     * @param username the username
     * @param passwordHash the hashed password
     */
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.inbox = new ArrayList<>();
    }

    // Getters and setters

    /**
     * Gets the username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password hash.
     * @return the password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Gets the public key.
     * @return the public key
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the public key.
     * @param publicKey the public key
     */
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Gets the private key.
     * @return the private key
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Sets the private key.
     * @param privateKey the private key
     */
    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * Gets the inbox (list of messages).
     * @return the inbox
     */
    public List<Message> getInbox() {
        return inbox;
    }

    /**
     * Adds a message to the inbox.
     * @param message the message to add
     */
    public void addMessage(Message message) {
        this.inbox.add(message);
    }
}