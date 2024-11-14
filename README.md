# Overview

The software is a Password Manager written in Kotlin. It was written to showcase common language features including variables,
conditionals, loops, functions, classes, and file handling. The program is designed to allow users to create a secure account
with a master password, which is then used to encrypt and decrypt their stored passwords. Users can add, view, modify,
or delete saved passwords for various sites. Password data is encrypted using AES encryption, ensuring that sensitive information
is more secure and accessible only through the correct master password.

I wrote this software to familiarize myself with the Kotlin language and to gain experience with implementing encryption techniques
the way they would be used in real-world applications. Through this project, I learned how concepts like data classes, conditionals,
loops, and file handling—come together in the kotlin language. Furthermore, I wanted to learn how encryption algorithms like AES
safeguard sensitive information. I focused specifically on how secure password management systems work. This project allowed me to build
an understanding of secure data handling.

[Password Manager Demo Video](https://youtu.be/kmnVj9G0ms0)

# Development Environment

I used the Kotlin programming language to develop this software, alongside several Java libraries that are compatible with
the Kotlin language. I used the IntelliJIdea compiler when writing and compiling this program, rather than VS Code, because
the compiler was designed by the creators of Kotlin to work with that language, as well as Java.

Kotlin was used for this project in order to familiarize myself with the language, and because of its compatability with Java,
which I also wanted to better familiarize myself with. For encryption, I used Java's javax.crypto library, which included classes
like Cipher and SecretKeyFactory to encrypt and decrypt sensitive data. Additionally, I used Java.io for reading and writing to files,
and Kotlin's standard libraries handled file handling and user interaction.

# Useful Websites

- [Kotlin Playground](http://url.link.goes.hehttps://play.kotlinlang.org/)
- [Kotlin Documentation](http://url.link.goes.herehttps://kotlinlang.org/docs/home.html)
- [Stack Overflow](https://stackoverflow.com/)

# Future Work

- Create a user interface for ease of use and to make the program look nicer.
- Store passwords more securely by changing the storage location from a file, which can be brute forced, to another method.
- Add a feature that will store the date and time a password was created and prompt the user to change the password every six months for their security.
- Add a way to update the master password should the user forget it, and/or enable two-factor authentication.
