package model;

import util.CryptoUtil;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.security.PrivateKey;
import java.time.LocalDateTime;

/**
 * Represents an encrypted message sent between users.
 */
public class Message implements Serializable {

    private byte[] encryptedContent;
    private byte[] encryptedAESKey;
    private String senderUsername;
    private LocalDateTime timestamp;

    /**
     * Constructs a new Message.
     *
     * @param encryptedContent the encrypted message content
     * @param encryptedAESKey the AES key encrypted with recipient's public key
     * @param senderUsername the sender's username
     */
    public Message(byte[] encryptedContent, byte[] encryptedAESKey, String senderUsername) {
        this.encryptedContent = encryptedContent;
        this.senderUsername = senderUsername;
        this.encryptedAESKey = encryptedAESKey;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Gets the encrypted message content.
     * @return the encrypted content
     */
    public byte[] getEncryptedContent() {
        return this.encryptedContent;
    }

    /**
     * Gets the encrypted AES key.
     * @return the encrypted AES key
     */
    public byte[] getEncryptedAESKey() {
        return encryptedAESKey;
    }

    /**
     * Gets the sender's username.
     * @return the sender's username
     */
    public String getSenderUsername() {
        return senderUsername;
    }

    /**
     * Gets the timestamp when the message was sent.
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Decrypts the message using the recipient's private key.
     *
     * @param privateKey the recipient's private RSA key
     * @return the decrypted message content
     * @throws Exception if decryption fails
     */
    public String decrypt(PrivateKey privateKey) throws Exception {
        // Decrypt AES key with recipient's private RSA key
        byte[] aesKeyBytes = CryptoUtil.decryptRSA(this.encryptedAESKey, privateKey);
        SecretKey aesKey = CryptoUtil.restoreAESKey(aesKeyBytes);

        // Decrypt message content with AES key
        return CryptoUtil.decryptAES(this.encryptedContent, aesKey);
    }

}
