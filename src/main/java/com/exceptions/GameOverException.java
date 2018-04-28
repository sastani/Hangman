package com.exceptions;
/**
 * Created by sinaastani on 4/27/18.
 */

public class GameOverException extends Exception
{
    public GameOverException() {
        super(String.format("Game is already complete"));
    }
}