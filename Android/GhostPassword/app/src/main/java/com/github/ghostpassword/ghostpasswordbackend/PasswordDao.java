package com.github.ghostpassword.ghostpasswordbackend;

import android.content.Context;
import android.os.Environment;

import com.github.ghostpassword.ghostpasswordbackend.domain.Password;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * File helper class. Reads and writes encrypted files. Singleton.
 *
 * @author udeyoje
 */
public class PasswordDao
{

    /**
     * An instance of this object.
     */
    private static PasswordDao instance;


    /**
     * Base directory for this app.
     */
    public static final File BASE_DIR = new File(Environment.getDataDirectory(), "ghostpass");

    /**
     * Salt file for this app.
     */
    public static final File SALT_FILE = new File(BASE_DIR, ".salt");

    /**
     * Test file for this app.
     */
    public static final File TEST_FILE = new File(BASE_DIR, ".passTest");

    /**
     * Default character set. TODO: make this settable.
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * Salt string.
     */
    private static String salt;

    /**
     * Gets an instance of this object.
     *
     * @return An instance of this object.
     * @throws IOException If there is a problem reading or writing from the
     * file system.
     */
    public static PasswordDao getInstance() throws IOException
    {
        if (instance != null)
        {
            return instance;
        } else
        {
            return new PasswordDao();
        }
    }

    /**
     * Private constructor.
     *
     * @throws IOException If there is a problem reading or writing from the
     * file system.
     */
    private PasswordDao() throws IOException
    {
        boolean regenSalt = false;
        if (!BASE_DIR.exists())
        {
            BASE_DIR.mkdirs();
            regenSalt = true;
        }
        if (!regenSalt && SALT_FILE.exists())
        {//if the salt file exists, we need to read from that.
            String saltFileContents = readFileAsString(SALT_FILE);//get the salt file
            if (!saltFileContents.trim().isEmpty())
            {// if its not empty, use it
                salt = saltFileContents;
                regenSalt = false;
            } else
            {
                System.err.println("WARNING: SALT FILE WAS EMPTY! REGENNING");// if the file is empty, regen, and warn; this is "bad"
                regenSalt = true;
            }
        } else
        {
            regenSalt = true;
        }
        if (regenSalt)
        {
            salt = EncryptUtils.generateSalt();
            SALT_FILE.delete();//delete it if it existed
            SALT_FILE.createNewFile();//create it fresh
            writeStringToFile(SALT_FILE, salt);
        }

    }

    public static boolean isInited()
    {
        return TEST_FILE.exists();
    }

    public static void init(String newMaster) throws IOException, NoSuchAlgorithmException
    {
        System.out.println("BASE_DIR: " + BASE_DIR.getAbsolutePath());
        if (!BASE_DIR.exists())
        {
            BASE_DIR.mkdirs();
        }
        TEST_FILE.createNewFile();
        writeStringToFile(TEST_FILE, EncryptUtils.hashPassword(newMaster));
    }

    public static boolean checkPassword(String masterPassword) throws IOException, NoSuchAlgorithmException
    {
        String passHash = EncryptUtils.hashPassword(masterPassword);
        String fromFile = readFileAsString(TEST_FILE).trim();
        return passHash.equals(fromFile);
    }

    public boolean passwordExists(String key)
    {
        File f = new File(BASE_DIR, key);
        return f.exists();
    }

    public void savePassword(String masterPassword, Password password) throws IOException
    {
        String encyptedPassword = EncryptUtils.encryptStringWithAES(salt, masterPassword, password.getPasswordText());
        password.setPasswordText(encyptedPassword);
        writeStringToFile(new File(BASE_DIR, password.getKey()), deserializePasswordObjectToJSON(password));
    }

    public Password readPassword(String masterPassword, String key) throws IOException, ParseException, java.text.ParseException
    {
        String jsonFromFS = readFileAsString(new File(BASE_DIR, key));
        Password password = serializeJSONToPasswordObject(jsonFromFS);
        password.setPasswordText(EncryptUtils.decryptStringWithAES(salt, masterPassword, password.getPasswordText()));
        return password;
    }

    public void deletePassword(String key)
    {
        File f = new File(BASE_DIR, key);
        f.delete();
    }

    public List<Password> getAllPasswords() throws IOException, ParseException, java.text.ParseException
    {
        ArrayList<Password> toReturn = new ArrayList<>();
        File[] allFiles = BASE_DIR.listFiles();
        for (File f : allFiles)
        {
            if (!f.getName().equals(".salt") && !f.getName().equals(".passTest"))
            {
                toReturn.add(serializeJSONToPasswordObject(readFileAsString(f)));
            }
        }
        return toReturn;
    }

    private String deserializePasswordObjectToJSON(Password password)
    {
        JSONObject object = new JSONObject();
        if (password.getKey() != null);
        {
            object.put("key", password.getKey());
        }
        if (password.getFriendlyName() != null);
        {
            object.put("friendlyName", password.getFriendlyName());
        }
        if (password.getPasswordText() != null);
        {
            object.put("passwordText", password.getPasswordText());
        }
        object.put("numberOfTimesUsed", password.getNumberOfTimesUsed());
        if (password.getLastAccessed() != null);
        {
            object.put("lastAccessed", sdf.format(password.getLastAccessed()));
        }
        return object.toJSONString();
    }

    private Password serializeJSONToPasswordObject(String json) throws ParseException, java.text.ParseException
    {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        Password toReturn = new Password((String) jsonObject.get("key"), (String) jsonObject.get("friendlyName"), (String) jsonObject.get("passwordText"));
        if (jsonObject.get("numberOfTimesUsed") != null)
        {
            toReturn.setNumberOfTimesUsed((long) jsonObject.get("numberOfTimesUsed"));
        }
        if (jsonObject.get("lastAccessed") != null)
        {
            toReturn.setLastAccessed(sdf.parse((String) jsonObject.get("lastAccessed")));
        }
        return toReturn;
    }

    private synchronized static void writeStringToFile(File f, String string) throws IOException
    {
        System.out.println("Writing: '" + string + "' to " + f.getName());
        OutputStream os = null;
        try
        {
            os = new FileOutputStream(f);
            IOUtils.write(string, os);
        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

    /**
     * Read the contents of a given file name as a string using UTF-8 encoding
     *
     * @param file The file to read
     * @return The string contents of the file
     * @throws IOException
     */
    private synchronized static String readFileAsString(File file) throws IOException
    {
        return readFileAsString(file.getAbsolutePath(), DEFAULT_CHARSET);
    }

    /**
     * Read the contents of a given file name as a string using a provided
     * character encoding
     *
     * @param filename the file to read
     * @param charSet The character set of the file
     * @return The string contents of the file
     * @throws IOException
     */
    public static synchronized String readFileAsString(String filename, String charSet) throws IOException
    {
        FileInputStream fin = new FileInputStream(filename);
        try
        {
            return readInputStreamAsString(fin, charSet);
        } finally
        {
            IOUtils.closeQuietly(fin);
        }
    }

    /**
     * Read a given InputStream as a string using a provided encoding
     *
     * @param in The InputStream to read
     * @param charSet The character set to use while reading the stream
     * @return A string representation of the stream
     * @throws IOException
     */
    public static String readInputStreamAsString(InputStream in, String charSet) throws IOException
    {
        return IOUtils.toString(in, charSet);
    }
}
