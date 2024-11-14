import java.io.File
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.system.exitProcess

// Main function, starts the manager
fun main() {
    println("Welcome to the Password Manager!")
    while (true) {
        println("Choose an option: \n1. Login \n2. Create User \n3. Quit")
        when (readln().trim()) {
            "1" -> login()
            "2" -> createUser()
            "3" -> exitProcess(0)
            else -> println("Invalid option.")
        }
    }
}

// Function to create a new user
fun createUser() {
    println("Enter a new username:")
    val username = readln().trim()
    println("Enter a new master password:")
    val masterPassword = readPassword()

    //If the user exists, we don't need to create a file for them
    val userFile = File("$username.dat")
    if (userFile.exists()) {
        println("User already exists. Try logging in.")
        return
    }

    // Save an empty encrypted file for this new user
    savePasswords(userFile, masterPassword, emptyList())
    println("User created successfully! You can now log in.")
}

// Function for users to log in
fun login() {
    println("Enter username:")
    val username = readln().trim()
    println("Enter master password:")
    val masterPassword = readPassword()

    // Look for the file when they log in.
    val userFile = File("$username.dat")
    // If the file doesn't exist, notify the user, and let them create a new profile.
    if (!userFile.exists()) {
        println("User not found. Do you want to create a new user? (y/n)")
        if (readln().trim().lowercase() == "y") {
            createUser()
        }
        return
    }

    // Check the master password and let the user in, or notify them the password is incorrect.
    try {
        val passwords = loadPasswords(userFile, masterPassword)
        passwordManagerMenu(userFile, masterPassword, passwords)
    } catch (e: Exception) {
        println("Login failed. Incorrect password or data corrupted.")
    }
}

// Function storing the menu for the password manager
fun passwordManagerMenu(userFile: File, masterPassword: String, passwords: MutableList<PasswordEntry>) {
    while (true) {
        println("Choose an option: \n1. Add New Password \n2. View Passwords \n3. Modify Password \n4. Suggest Strong Password \n5. Logout")
        when (readln().trim()) {
            "1" -> {
                println("Enter site name:")
                val site = readln().trim()
                println("Enter username:")
                val siteUsername = readln().trim()
                println("Enter password:")
                val sitePassword = readPassword()

                passwords.add(PasswordEntry(site, siteUsername, sitePassword))
                savePasswords(userFile, masterPassword, passwords)
                println("Password saved successfully!")
            }
            "2" -> {
                println("Stored Passwords:")
                passwords.forEach { println("Site: ${it.site}, Username: ${it.username}, Password: ${it.password}") }
            }
            "3" -> modifyPassword(passwords, userFile, masterPassword)
            "4" -> println("Suggested Strong Password: ${generateStrongPassword()}")
            "5" -> return
            else -> println("Invalid option.")
        }
    }
}

// Function to modify a password if the user wants to update a password.
fun modifyPassword(passwords: MutableList<PasswordEntry>, userFile: File, masterPassword: String) {
    println("Enter the site name for the password to modify:")
    val site = readln().trim()
    val entry = passwords.find { it.site == site }

    // Logic to change the password for that site
    if (entry != null) {
        println("Enter new password:")
        val newPassword = readPassword()
        entry.password = newPassword
        savePasswords(userFile, masterPassword, passwords)
        println("Password updated successfully!")
    // Logic for situations when the site does not exist within the database
    } else {
        println("No password found for the site: $site.")
    }
}

// Function to generate a random strong password the user can use.
fun generateStrongPassword(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*()_-+=<>?"
    return (1..16)
        .map { chars.random() }
        .joinToString("")
}

// A data class for each password entry to go into the database
data class PasswordEntry(val site: String, val username: String, var password: String)

// Read password securely without echoing
fun readPassword(): String {
    return System.console()?.readPassword()?.joinToString("") ?: readln()
}

// Encryption is used to ensure the passwords being saved are secure

// Encrypt and save passwords
fun savePasswords(file: File, masterPassword: String, passwords: List<PasswordEntry>) {
    val cipher = getCipher(Cipher.ENCRYPT_MODE, masterPassword)
    val encryptedData = cipher.doFinal(passwords.joinToString("\n") { "${it.site},${it.username},${it.password}" }.toByteArray())
    file.writeBytes(encryptedData)
}

// Load and decrypt passwords
fun loadPasswords(file: File, masterPassword: String): MutableList<PasswordEntry> {
    val cipher = getCipher(Cipher.DECRYPT_MODE, masterPassword)
    val decryptedData = cipher.doFinal(file.readBytes())
    return decryptedData.decodeToString().lines().filter { it.isNotEmpty() }.map {
        val parts = it.split(",")
        PasswordEntry(parts[0], parts[1], parts[2])
    }.toMutableList()
}

// Get Cipher instance with AES encryption and a key derived from the master password
fun getCipher(mode: Int, password: String): Cipher {
    val salt = "someSaltValue".toByteArray() // use a secure salt for production
    val keySpec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
    val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val secretKey = secretKeyFactory.generateSecret(keySpec)
    val secret = SecretKeySpec(secretKey.encoded, "AES")
    return Cipher.getInstance("AES").apply { init(mode, secret) }
}

