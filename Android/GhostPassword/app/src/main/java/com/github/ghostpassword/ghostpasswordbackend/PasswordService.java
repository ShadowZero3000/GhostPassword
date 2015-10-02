package com.github.ghostpassword.ghostpasswordbackend;

import com.github.ghostpassword.ghostpasswordbackend.domain.Password;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.json.simple.parser.ParseException;

/**
 * Service enforcing business rules for CRUD on password objects.
 *
 * @author udeyoje
 */
public class PasswordService
{

    private final String masterPassword;
    private PasswordDao dao;

    public PasswordService(String masterPassword) throws IOException
    {
        this.masterPassword = masterPassword;
        dao = PasswordDao.getInstance();
    }

    public Password savePassword(String friendlyName, String passwordText) throws IOException, IllegalArgumentException
    {
        String key = Utils.calculateKey(friendlyName);
        Password password = new Password(key, friendlyName, passwordText);
        password.setLastAccessed(new Date());

        if (dao.passwordExists(key))
        {
            throw new IllegalArgumentException("Password already exists. You should update the existing password instead.");
        } else
        {
            dao.savePassword(masterPassword, password);
        }
        return password;
    }

    public void updatePassword(Password password) throws IOException
    {
        password.setLastAccessed(new Date());
        dao.savePassword(masterPassword, password);
    }

    public void deletePassword(Password password) throws IOException
    {
        dao.deletePassword(password.getKey());
    }

    public boolean passwordExists()
    {
        return dao.passwordExists(masterPassword);
    }

    public Password getPasswordDecrypted(String key) throws IOException, ParseException, java.text.ParseException
    {
        Password password = dao.readPassword(masterPassword, key);
        password.setLastAccessed(new Date());
        dao.savePassword(masterPassword, password);
        return password;
    }

    public List<Password> getAllPasswordsOrderByRecentlyUsed() throws IOException, ParseException, java.text.ParseException
    {
        Comparator<Password> comparator = new Comparator<Password>()
        {
            @Override
            public int compare(Password o1, Password o2)
            {
                if (o1.getNumberOfTimesUsed() > o2.getNumberOfTimesUsed())
                {
                    return 1;
                } else if (o1.getNumberOfTimesUsed() < o2.getNumberOfTimesUsed())
                {
                    return -1;
                } else
                {
                    return 0;
                }
            }
        };
        List<Password> passwords = dao.getAllPasswords();
        Collections.sort(passwords, comparator);
        return passwords;
    }

    public List<Password> getAllPasswordsOrderByAlphabetical() throws IOException, ParseException, java.text.ParseException
    {
        Comparator<Password> comparator = new Comparator<Password>()
        {
            @Override
            public int compare(Password o1, Password o2)
            {
                return o1.getFriendlyName().compareTo(o2.getFriendlyName());                
            }
        };
        List<Password> passwords = dao.getAllPasswords();
        Collections.sort(passwords, comparator);
        return passwords;
    }

}
