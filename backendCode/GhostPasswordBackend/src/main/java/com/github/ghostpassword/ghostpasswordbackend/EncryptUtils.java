package com.github.ghostpassword.ghostpasswordbackend;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 *
 * @author udeyoje
 */
public class EncryptUtils
{

    public static String encryptStringWithAES(String salt, String password, String toEncrypt)
    {        
        TextEncryptor encryptor = Encryptors.text(password, salt);
        String encryptedText = encryptor.encrypt(toEncrypt);
        return encryptedText;
    }
    
    public static String decryptStringWithAES(String salt, String password, String toDecrypt)
    {        
        TextEncryptor decryptor = Encryptors.text(password, salt);
        String decryptedText = decryptor.decrypt(toDecrypt);
        return decryptedText;
    }
}
