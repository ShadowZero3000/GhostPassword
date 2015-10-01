package com.github.ghostpassword.ghostpasswordbackend.domain;

import java.util.Date;

/**
 * Domain object that represents a password
 *
 * @author udeyoje
 */
public class Password
{

    /**
     * Key for referencing this password object.
     */
    private String key;
    /**
     * Friendly Name for this password to display to the user.
     */
    private String friendlyName;

    /**
     * Password plaintext or ciphertext.
     */
    private String passwordText;

    /**
     * Number of times this password has been used.
     */
    private long numberOfTimesUsed;

    /**
     * Date that this password was last accessed.
     */
    private Date lastAccessed;

    public Password(String key, String friendlyName, String passwordText)
    {
        this.key = key;
        this.friendlyName = friendlyName;
        this.passwordText = passwordText;
        this.numberOfTimesUsed = 0;
        this.lastAccessed = new Date(0);//never accessed
    }

    public Password(String key, String friendlyName, String passwordText, long numberOfTimesUsed, Date lastAccessed)
    {
        this.key = key;
        this.friendlyName = friendlyName;
        this.passwordText = passwordText;
        this.numberOfTimesUsed = numberOfTimesUsed;
        this.lastAccessed = lastAccessed;
    }

    /**
     * Key for referencing this password object.
     *
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Key for referencing this password object.
     *
     * @param key the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * Friendly Name for this password to display to the user.
     *
     * @return the friendlyName
     */
    public String getFriendlyName()
    {
        return friendlyName;
    }

    /**
     * Friendly Name for this password to display to the user.
     *
     * @param friendlyName the friendlyName to set
     */
    public void setFriendlyName(String friendlyName)
    {
        this.friendlyName = friendlyName;
    }

    /**
     * Password plaintext or ciphertext.
     *
     * @return the passwordText
     */
    public String getPasswordText()
    {
        return passwordText;
    }

    /**
     * Password plaintext or ciphertext.
     *
     * @param passwordText the passwordText to set
     */
    public void setPasswordText(String passwordText)
    {
        this.passwordText = passwordText;
    }

    /**
     * Number of times this password has been used.
     * @return the numberOfTimesUsed
     */
    public long getNumberOfTimesUsed()
    {
        return numberOfTimesUsed;
    }

    /**
     * Number of times this password has been used.
     * @param numberOfTimesUsed the numberOfTimesUsed to set
     */
    public void setNumberOfTimesUsed(long numberOfTimesUsed)
    {
        this.numberOfTimesUsed = numberOfTimesUsed;
    }

    /**
     * Date that this password was last accessed.
     * @return the lastAccessed
     */
    public Date getLastAccessed()
    {
        return lastAccessed;
    }

    /**
     * Date that this password was last accessed.
     * @param lastAccessed the lastAccessed to set
     */
    public void setLastAccessed(Date lastAccessed)
    {
        this.lastAccessed = lastAccessed;
    }

}
