package com.github.ghostpassword.ghostpasswordbackend;

import com.github.ghostpassword.ghostpasswordbackend.domain.Password;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
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
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
        try
        {
            FileUtils.deleteDirectory(PasswordDao.BASE_DIR);
        } catch (NullPointerException | IOException e)
        {
            //nada; can't clean it up; wasn't created
        }
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
        String plaintext = "this is a secret!!!";
        Password password = new Password("thisisakey", "This is a key", plaintext);
        PasswordDao instance = PasswordDao.getInstance();

        //check that it exists
        boolean exists = instance.passwordExists(password.getKey());
        assertEquals(false, exists);//it shouldn't

        //save it
        instance.savePassword(masterPassword, password);

        //check that it exists again
        exists = instance.passwordExists(password.getKey());
        assertEquals(true, exists);//now it should

        //fetch it
        Password result = instance.readPassword(masterPassword, password.getKey());
        //check it
        assertNotNull(result);
        assertEquals(password.getKey(), result.getKey());
        assertEquals(password.getFriendlyName(), result.getFriendlyName());
        assertEquals(password.getNumberOfTimesUsed(), result.getNumberOfTimesUsed());
        assertEquals(password.getLastAccessed(), result.getLastAccessed());
        assertNotSame(plaintext, result.getPasswordText());
        assertNotSame(password.getPasswordText(), result.getPasswordText());

        List<Password> passwords = instance.getAllPasswords();
        assertTrue(passwords.size() == 1);

        //delete it
        instance.deletePassword(password.getKey());

        //check that it exists again
        exists = instance.passwordExists(password.getKey());
        assertEquals(false, exists);//it shouldn't

        passwords = instance.getAllPasswords();
        assertTrue(passwords.isEmpty());
    }

}
