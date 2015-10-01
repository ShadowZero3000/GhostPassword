package com.github.ghostpassword.ghostpasswordbackend;

import com.github.ghostpassword.ghostpasswordbackend.domain.Password;
import java.io.InputStream;
import java.util.List;
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
public class PasswordDaoTest
{

    private static final String TEST_PASS = "test";

    public PasswordDaoTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
        PasswordDao.BASE_DIR.delete();
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
     * Test of getInstance method, of class PasswordDao.
     */
    @Test
    public void testGetInstance() throws Exception
    {
        System.out.println("getInstance");
        PasswordDao result = PasswordDao.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of savePassword method, of class PasswordDao.
     */
    @Test
    public void testSavePassword() throws Exception
    {
        System.out.println("savePassword");
        String masterPassword = TEST_PASS;
        Password password = new Password("thisisakey", "This is a key", "this is a secret!!!");
        PasswordDao instance = PasswordDao.getInstance();
        
        //check that it exists
        boolean exists = instance.passwordExists(password.getKey());
        assertEquals(false, exists);//it shouldn't
        
        //save it
        instance.savePassword(masterPassword, password);
        
        //check that it exists again
        exists = instance.passwordExists(password.getKey());
        assertEquals(true, exists);//now it should
        
    }

    /**
     * Test of passwordExists method, of class PasswordDao.
     */
    @Test
    public void testPasswordExists()
    {
        System.out.println("passwordExists");
        String key = "";
        PasswordDao instance = null;
        boolean expResult = false;
        boolean result = instance.passwordExists(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readPassword method, of class PasswordDao.
     */
    @Test
    public void testReadPassword() throws Exception
    {
        System.out.println("readPassword");
        String masterPassword = "";
        String key = "";
        PasswordDao instance = null;
        Password expResult = null;
        Password result = instance.readPassword(masterPassword, key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deletePassword method, of class PasswordDao.
     */
    @Test
    public void testDeletePassword()
    {
        System.out.println("deletePassword");
        String key = "";
        PasswordDao instance = null;
        instance.deletePassword(key);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllPasswords method, of class PasswordDao.
     */
    @Test
    public void testGetAllPasswords() throws Exception
    {
        System.out.println("getAllPasswords");
        PasswordDao instance = null;
        List<Password> expResult = null;
        List<Password> result = instance.getAllPasswords();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readFileAsString method, of class PasswordDao.
     */
    @Test
    public void testReadFileAsString() throws Exception
    {
        System.out.println("readFileAsString");
        String filename = "";
        String charSet = "";
        String expResult = "";
        String result = PasswordDao.readFileAsString(filename, charSet);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readInputStreamAsString method, of class PasswordDao.
     */
    @Test
    public void testReadInputStreamAsString() throws Exception
    {
        System.out.println("readInputStreamAsString");
        InputStream in = null;
        String charSet = "";
        String expResult = "";
        String result = PasswordDao.readInputStreamAsString(in, charSet);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
