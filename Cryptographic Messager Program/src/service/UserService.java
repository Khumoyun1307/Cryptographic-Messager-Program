package service;

import java.io.*;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import model.User;
import util.KeyUtil;

/**
 * Service for managing user registration, login, and user data persistence.
 */
public class UserService {

    private final Map<String, User> userStore = new HashMap<>();
    private static final String USER_DATA_FILE = "users.dat";

    public UserService() {
        loadUsers();
    }

    /**
     * Registers a new user with the given username and password.
     * Generates RSA key pair and saves user data.
     *
     * @param username the username
     * @param password the password
     * @return the registered User object
     * @throws Exception if registration fails
     */
    public User register(String username, String password) throws Exception {
        if (userStore.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        String passwordHash = hashPassword(password);
        User user = new User(username, passwordHash);

        // Generate RSA key pair
        KeyPair keyPair = KeyUtil.generateRSAKeyPair();
        user.setPrivateKey(keyPair.getPrivate());
        user.setPublicKey(keyPair.getPublic());

        // Save keys to file
        KeyUtil.saveKeyToFile(user.getPublicKey(), username + "_public.key");
        KeyUtil.saveKeyToFile(user.getPrivateKey(), username + "_private.key");

        userStore.put(username, user);
        saveUsers();

        return user;
    }

    /**
     * Logs in a user with the given username and password.
     * Loads user's key pair from files.
     *
     * @param username the username
     * @param password the password
     * @return the logged-in User object
     * @throws Exception if login fails
     */
    public User login(String username, String password) throws Exception {
        User user = userStore.get(username);

        if (user == null || !user.getPasswordHash().equals(hashPassword(password))) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        // Load user's key pair from files
        user.setPublicKey((java.security.PublicKey) KeyUtil.loadKeyFromFile(username + "_public.key"));
        user.setPrivateKey((java.security.PrivateKey) KeyUtil.loadKeyFromFile(username + "_private.key"));

        return user;
    }

    private String hashPassword(String password) throws Exception {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(userStore);
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<String, User> loaded = (Map<String, User>) ois.readObject();
            userStore.putAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load users: " + e.getMessage());
        }
    }

    /**
     * Gets the public key of a user by username.
     *
     * @param username the username
     * @return the public key
     * @throws Exception if the key cannot be loaded
     */
    public PublicKey getUserPublicKey(String username) throws Exception {
        return (PublicKey) KeyUtil.loadKeyFromFile(username + "_public.key");
    }

}