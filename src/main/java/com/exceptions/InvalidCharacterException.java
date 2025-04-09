package com.exceptions;
public final class InvalidCharacterException extends Exception{
    public InvalidCharacterException(String s){
        super(String.format("Guessed character %s is invalid.", s));
    }
}
