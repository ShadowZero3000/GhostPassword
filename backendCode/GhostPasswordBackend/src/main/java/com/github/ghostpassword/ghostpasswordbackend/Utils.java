package com.github.ghostpassword.ghostpasswordbackend;

/**
 * General utils.
 * @author udeyoje
 */
public class Utils
{
    /**
     * Calculates a key name from a file name.
     * @param friendlyName
     * @return a file system friendly name for this password key
     */
    public static String calculateKey(String friendlyName){
        friendlyName = friendlyName.trim().toLowerCase();
        friendlyName = friendlyName.replaceAll("\\Q \\E", "-");
        friendlyName = friendlyName.replaceAll("[!@#$%^&*()+\"]", "_");
        return friendlyName;
    }
}
