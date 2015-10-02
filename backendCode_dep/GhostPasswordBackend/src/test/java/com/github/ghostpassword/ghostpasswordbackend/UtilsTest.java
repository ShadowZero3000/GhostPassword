/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ghostpassword.ghostpasswordbackend;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author udeyoje
 */
public class UtilsTest
{

    public UtilsTest()
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
     * Test of calculateKey method, of class Utils.
     */
    @Test
    public void testCalculateKey()
    {
        System.out.println("calculateKey");
        String friendlyName = "Amazon Books    ";
        String expResult = "amazon-books";
        String result = Utils.calculateKey(friendlyName);
        assertEquals(expResult, result);

        friendlyName = "Amazon Books!    ";
        expResult = "amazon-books_";
        result = Utils.calculateKey(friendlyName);
        assertEquals(expResult, result);

        friendlyName = "           Amazon Books!    ";
        expResult = "amazon-books_";
        result = Utils.calculateKey(friendlyName);
        assertEquals(expResult, result);

        friendlyName = "Amazon Books!#$%";
        expResult = "amazon-books____";
        result = Utils.calculateKey(friendlyName);
        assertEquals(expResult, result);
        
        friendlyName = "Amazon*Books";
        expResult = "amazon_books";
        result = Utils.calculateKey(friendlyName);
        assertEquals(expResult, result);
    }

}
