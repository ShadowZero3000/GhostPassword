package com.github.ghostpassword.ghostpasswordbackend;

/**
 * Created by usouzj2 on 10/20/15.
 */
public class GhostPasswordException extends Exception {
    private static final long serialVersionUID = 18234641987231L;
    public GhostPasswordException(){

    }
    public GhostPasswordException(String message){
        super(message);
    }
    public GhostPasswordException(Throwable cause)
    {
        super(cause);
    }
    public GhostPasswordException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
