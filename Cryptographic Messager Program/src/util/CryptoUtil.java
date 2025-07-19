package util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Utility class for cryptographic operations (AES and RSA).
 */
public class CryptoUtil {

    private static final int AES_KEY_SIZE = 192;
    private static final int GCM_IV_LENGTH = 12;

    /**
     * Generates a new AES key.
     * @return the generated SecretKey
     * @throws java.security.NoSuchAlgorithmException if AES is not supported
     */
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE);
        return keyGen.generateKey();
    }

    /**
     * Encrypts plaintext using AES-GCM.
     * @param plainText the plaintext to encrypt
     * @param key the AES key
     * @return the encrypted data (IV + ciphertext)
     * @throws Exception if encryption fails
     */
    public static byte[] encryptAES(String plainText, SecretKey key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        byte[] iv = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        GCMParameterSpec spec = new GCMParameterSpec(128,iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        // Combine IV and ciphertext
        byte[] encrypted = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(cipherText, 0, encrypted, iv.length, cipherText.length);

        return encrypted;
    }

    /**
     * Decrypts AES-GCM encrypted data.
     * @param encrypted the encrypted data (IV + ciphertext)
     * @param key the AES key
     * @return the decrypted plaintext
     * @throws Exception if decryption fails
     */
    public static String decryptAES(byte[] encrypted, SecretKey key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] cipherText = new byte[encrypted.length - 12];
        System.arraycopy(encrypted, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(encrypted, GCM_IV_LENGTH , cipherText, 0, cipherText.length);

        GCMParameterSpec spec = new GCMParameterSpec(128,iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plainBytes = cipher.doFinal(cipherText);
        return new String(plainBytes);

    }

    /**
     * Encrypts data using RSA public key.
     * @param data the data to encrypt
     * @param publicKey the RSA public key
     * @return the encrypted data
     * @throws Exception if encryption fails
     */
    public static byte[] encryptRSA(byte[] data, PublicKey publicKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);

    }

    /**
     * Decrypts data using RSA private key.
     * @param encrypted the encrypted data
     * @param privateKey the RSA private key
     * @return the decrypted data
     * @throws Exception if decryption fails
     */
    public static byte[] decryptRSA(byte[] encrypted, PrivateKey privateKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypted);

    }

    /**
     * Restores an AES key from its byte array representation.
     * @param keyBytes the key bytes
     * @return the SecretKey
     */
    public static SecretKey restoreAESKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, "AES");
    }

}