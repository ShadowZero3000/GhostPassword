package rocks.codethat.ghostpassword;

/**
 * Created by Sparrow on 12/4/2016.
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
