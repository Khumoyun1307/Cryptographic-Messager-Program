package view;

import model.Message;
import model.User;
import service.MessageService;
import service.UserService;

import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the CryptoMessenger application.
 * Handles user registration, login, sending messages, and viewing inbox.
 */
public class ConsoleUI {

    private final UserService userService;
    private final MessageService messageService = new MessageService();
    private final Scanner scanner;
    private User currentUser;

    /**
     * Constructs a new ConsoleUI instance.
     */
    public ConsoleUI() {
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main loop of the console UI.
     */
    public void start() {
        System.out.println("Welcome to CryptoMessenger!");

        while (true) {
            mainMenu();
        }
    }

    private void mainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Select option: ");
        String input = scanner.nextLine();

        try {
            switch (input) {
                case "1":
                    handleRegister();
                    break;
                case "2":
                    handleLogin();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️  " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ An error occurred. Please try again.");
        }
    }

    private void handleRegister() throws Exception {
        System.out.println("\n--- Register ---");
        String username = prompt("Enter username: ");
        String password = prompt("Enter password: ");
        User user = userService.register(username, password);
        System.out.println("✅ User registered successfully.");
    }

    private void handleLogin() throws Exception {
        System.out.println("\n--- Login ---");
        String username = prompt("Enter username: ");
        String password = prompt("Enter password: ");
        User user = userService.login(username, password);
        this.currentUser = user;
        System.out.println("✅ Login successful. Welcome, " + user.getUsername() + "!");
        messageService.loadMessages(user);

        userDashboard(); // Next screen


    }

    private void userDashboard() {
        while (true) {
            System.out.println("\n=== Dashboard ===");
            System.out.println("1. Send Message");
            System.out.println("2. View Inbox");
            System.out.println("3. Logout");
            System.out.print("Select option: ");
            String input = scanner.nextLine();

            try {
                switch (input) {
                    case "1":
                        handleSendMessage();
                        break;
                    case "2":
                        handleInbox();
                        break;
                    case "3":
                        System.out.println("Logging out...");
                        return; // go back to main menu
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    private void handleSendMessage() throws Exception {
        System.out.println("\n--- Send Message ---");

        String recipientName = prompt("Recipient username: ");

        // Prevent sending to self
        if (recipientName.equals(currentUser.getUsername())) {
            System.out.println("❌ You cannot send a message to yourself.");
            return;
        }

        try {
            PublicKey recipientKey = userService.getUserPublicKey(recipientName);

            // Create a lightweight User object with public key only
            User recipient = new User(recipientName, null);
            recipient.setPublicKey(recipientKey);

            messageService.loadMessages(recipient); // Load recipient's inbox

            String messageText = prompt("Message: ");
            messageService.sendMessage(currentUser, recipient, messageText);
            messageService.saveMessages(recipient); // Save recipient's inbox

            System.out.println("✅ Message sent to " + recipientName + "!");
        } catch (FileNotFoundException e) {
            System.out.println("❌ Recipient not found.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void handleInbox() throws Exception {
        System.out.println("\n--- Inbox ---");
        List<Message> inbox = messageService.getInbox(currentUser);

        if (inbox.isEmpty()) {
            System.out.println("📭 Inbox is empty.");
            return;
        }

        for (int i = 0; i < inbox.size(); i++) {
            Message msg = inbox.get(i);
            String decrypted = msg.decrypt(currentUser.getPrivateKey());
            System.out.println("\nMessage #" + (i + 1));
            System.out.println("From: " + msg.getSenderUsername());
            System.out.println("Time: " + msg.getTimestamp());
            System.out.println("Content: " + decrypted);
        }
    }

    private String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}