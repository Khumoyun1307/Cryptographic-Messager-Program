package app;

import view.ConsoleUI;

/**
 * Entry point for the CryptoMessenger application.
 */
public class Main {
    /**
     * Main method to start the application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI();
        consoleUI.start();
    }
}