package exceptions;

/**
 * Created by sinaastani on 4/26/18.
 */
public final class InvalidCharacterException extends Exception{
    public InvalidCharacterException(String s){
        super(String.format("Guessed character %s is invalid.", s));
    }
}
