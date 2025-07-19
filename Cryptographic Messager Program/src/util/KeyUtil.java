package util;

import java.io.*;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for generating, saving, and loading cryptographic keys.
 */
public class KeyUtil {

    /**
     * Generates a new RSA key pair.
     * @return the generated KeyPair
     * @throws java.security.NoSuchAlgorithmException if RSA is not supported
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.genKeyPair();

    }

    /**
     * Saves a key to a file.
     * @param key the key to save
     * @param filePath the file path
     * @throws java.io.IOException if saving fails
     */
    public static void saveKeyToFile(Key key, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(key);
        }
    }

    /**
     * Loads a key from a file.
     * @param filePath the file path
     * @return the loaded Key
     * @throws IOException if loading fails
     * @throws ClassNotFoundException if the key class is not found
     */
    public static Key loadKeyFromFile(String filePath) throws IOException, ClassNotFoundException {

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Key file not found: " + filePath);
        }


        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Key) ois.readObject();
        }
    }

}
