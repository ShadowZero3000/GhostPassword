package com.github.ghostpassword.ghostpasswordbackend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * Encryption helper class.
 *
 * @author udeyoje
 */
public class EncryptUtils
{

    /**
     * Generates salt for encryption and decryption. Use only once per crypto
     * store. Do not lose this; ever.
     *
     * @return Salt string
     */
    public static String generateSalt()
    {
        return KeyGenerators.string().generateKey();
    }

    /**
     * For hashing the master password
     * @param password
     * @return 
     */
    public static String hashPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    /**
     * Encrypts a string with 256 bit AES
     *
     * @param salt Salt to use
     * @param password Password to use
     * @param toEncrypt Plaintext to encrypt.
     * @return Cypertext.
     */
    public static String encryptStringWithAES(String salt, String password, String toEncrypt)
    {
        TextEncryptor encryptor = Encryptors.text(password, salt);
        String encryptedText = encryptor.encrypt(toEncrypt);
        return encryptedText;
    }

    /**
     * Decrypts a string that was encrypted with 256 bit AES.
     *
     * @param salt Salt to use
     * @param password Password to use
     * @param toDecrypt CyperText to convert to plaintext.
     * @return Plaintext.
     */
    public static String decryptStringWithAES(String salt, String password, String toDecrypt)
    {
        TextEncryptor decryptor = Encryptors.text(password, salt);
        String decryptedText = decryptor.decrypt(toDecrypt);
        return decryptedText;
    }
}
