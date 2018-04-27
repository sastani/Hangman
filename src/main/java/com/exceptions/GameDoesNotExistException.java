package com.exceptions;

/**
 * Created by sinaastani on 4/26/18.
 */
public final class GameDoesNotExistException extends Exception{
    public GameDoesNotExistException(String id){
       super(String.format("Game with id: %s does not exist.", id));
    }
}

