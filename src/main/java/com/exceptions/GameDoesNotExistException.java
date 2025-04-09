package com.exceptions;

public final class GameDoesNotExistException extends Exception{
    public GameDoesNotExistException(String id){
        super(String.format("Game with id: %s does not exist.", id));
    }
}

