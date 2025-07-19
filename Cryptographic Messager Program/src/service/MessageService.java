package service;

import model.Message;
import model.User;
import util.CryptoUtil;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for sending, saving, and loading encrypted messages between users.
 */
public class MessageService {

    /**
     * Sends an encrypted message from sender to recipient.
     *
     * @param sender   the sender user
     * @param recipient the recipient user
     * @param content  the plaintext message content
     * @throws Exception if encryption or saving fails
     */
    public void sendMessage(User sender, User recipient, String content) throws Exception {
        // Generate AES key
        SecretKey aesKey = CryptoUtil.generateAESKey();

        // Encrypt the message with AES
        byte[] encryptedMessage = CryptoUtil.encryptAES(content, aesKey);

        // Encrypt AES key with recipient's RSA public key
        PublicKey recipientKey = recipient.getPublicKey();
        byte[] encryptedAESKey = CryptoUtil.encryptRSA(aesKey.getEncoded(), recipientKey);

        // Create and store the message
        Message message = new Message(encryptedMessage, encryptedAESKey, sender.getUsername());
        recipient.addMessage(message);
        saveMessages(recipient); // Save inbox
    }

    /**
     * Returns the inbox (list of messages) for the given user.
     *
     * @param user the user
     * @return the list of messages
     */
    public List<Message> getInbox(User user) {
        return user.getInbox();
    }

    /**
     * Saves the user's inbox to a file.
     *
     * @param user the user
     */
    public void saveMessages(User user) {
        String filename = "inbox_" + user.getUsername() + ".dat";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(user.getInbox());
        } catch (IOException e) {
            System.err.println("Failed to save inbox: " + e.getMessage());
        }
    }

    /**
     * Loads the user's inbox from a file.
     *
     * @param user the user
     */
    @SuppressWarnings("unchecked")
    public void loadMessages(User user) {
        String filename = "inbox_" + user.getUsername() + ".dat";
        File file = new File(filename);

        if (!file.exists()) {
            user.getInbox().clear(); // Start fresh if no inbox exists
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Message> messages = (List<Message>) ois.readObject();
            user.getInbox().clear();
            user.getInbox().addAll(messages);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load inbox: " + e.getMessage());
        }
    }
}