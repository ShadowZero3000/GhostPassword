/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ghostpassword.ghostpasswordbackend;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 *
 * @author udeyoje
 */
public class EncryptUtilsTest
{
    
    public EncryptUtilsTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of encryptStringWithAES method, of class EncryptUtils.
     */
    @Test
    public void testEncyptStringWithAES()
    {
        System.out.println("encyptStringWithAES");
        final String salt = EncryptUtils.generateSalt();
        String password = "test";
        String toEncrypt = "thisisatest";
        String cypherText = EncryptUtils.encryptStringWithAES(salt, password, toEncrypt);     
        System.out.println(cypherText);
        Assert.assertNotSame(toEncrypt, cypherText);//make sure that we didn't just get the same string back
        String plainText = EncryptUtils.decryptStringWithAES(salt, password, cypherText);
        assertEquals(plainText, toEncrypt);
    }
    
}
