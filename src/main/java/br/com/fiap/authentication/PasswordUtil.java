package br.com.fiap.authentication;

import org.mindrot.jbcrypt.BCrypt;

/// Utility class for password hashing and verification
public class PasswordUtil {

    /// Hashes a plain text password using BCrypt algorithm.
    public static String hashPassword(String rootPassword){
        return BCrypt.hashpw(rootPassword, BCrypt.gensalt(12));
    }

    /// Verifies a plain text password against a hashed password.
    public static boolean checkPassword(String rootPassword, String hashedPassword){
        return BCrypt.checkpw(rootPassword, hashedPassword);
    }
}
