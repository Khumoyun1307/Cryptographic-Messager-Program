# CryptoMessenger

A simple console-based secure messaging application implemented in Java, enabling users to register, log in, and exchange encrypted messages using RSA key pairs for key exchange and AES-GCM for message confidentiality and integrity.

## Features

- **User Registration & Login**: Create and authenticate accounts with unique RSA key pairs.
- **End-to-End Encryption**: Messages are encrypted with AES-GCM; AES keys are securely exchanged using RSA.
- **Console UI**: Interactive command-line interface for sending and receiving messages.
- **Persistent Storage**: User credentials, key files, and message inboxes are saved to disk.

## Prerequisites

- **Java Development Kit (JDK) 8 or higher**
- **Maven** or a Java-capable IDE (optional)

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Khumoyun1307/Cryptographic-Messager-Program.git
   cd Cryptographic-Messager-Program
   ```

2. **Compile the source files**
   ```bash
   javac -d out \
     src/app/*.java \
     src/model/*.java \
     src/service/*.java \
     src/util/*.java \
     src/view/*.java
   ```

3. **Run the application**
   ```bash
   java -cp out app.Main
   ```

## Usage

1. **Register a new user**: Choose option `1` and enter a username. A new RSA key pair (`<username>_public.key` and `<username>_private.key`) will be created and saved.
2. **Login**: Choose option `2` and enter your username. Existing key files and user data will be loaded.
3. **Send a message**: After logging in, choose option `1` in the menu, enter the recipient’s username and your message. The application will:
   - Generate a one-time AES key.
   - Encrypt the message with AES-GCM.
   - Encrypt the AES key with the recipient’s RSA public key.
   - Store the encrypted message in the recipient’s inbox file.
4. **View inbox**: Choose option `2` to load and decrypt all messages addressed to you using your RSA private key.
5. **Logout / Exit**: Choose option `3` to return to the main menu or `0` to exit the application.

## Key Generation & Management

- **Key Files**: Upon registration, two files are created in the working directory:
  - `<username>_private.key` – Your RSA private key (keep secure!)
  - `<username>_public.key` – Your RSA public key (shared with others)
- **User Data**: Registered usernames are stored in `users.dat`.
- **Inbox Files**: Each user’s messages are serialized in `<username>_inbox.dat`.

## Project Structure

```text
CryptoMessenger/
├── LICENSE                 # GPLv3 License
├── users.dat               # Serialized user registry
├── <username>_public.key   # RSA public key for each user
├── <username>_private.key  # RSA private key for each user
├── src/
│   ├── app/
│   │   └── Main.java       # Application entry point
│   ├── model/
│   │   ├── User.java       # User data model
│   │   └── Message.java    # Encrypted message model
│   ├── service/
│   │   ├── UserService.java    # User registration/login and persistence
│   │   └── MessageService.java # Sending, saving, and loading messages
│   ├── util/
│   │   ├── KeyUtil.java    # RSA key generation & serialization
│   │   └── CryptoUtil.java # AES & RSA encryption/decryption utilities
│   └── view/
│       └── ConsoleUI.java  # Console-based user interface
```

## Class Overview

- **app.Main**: Launches the Console UI.
- **view.ConsoleUI**: Handles menus and user input/output.
- **model.User**: Stores user info including username and key references.
- **model.Message**: Encapsulates encrypted message data and decryption logic.
- **service.UserService**: Manages users, key loading, and persistence.
- **service.MessageService**: Implements message encryption, decryption, and storage.
- **util.KeyUtil**: Generates and reads RSA key pairs to/from files.
- **util.CryptoUtil**: Provides methods for AES-GCM encryption/decryption and RSA wrapping.

## Contributing

Contributions are welcome! Please fork the repository, create a feature branch, and submit a pull request. Ensure all new code is properly documented and tested.

## License

This project is licensed under the GNU General Public License v3.0 – see the [LICENSE](LICENSE) file for details.
