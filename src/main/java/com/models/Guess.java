package com.models;

/**
 * Created by sinaastani on 4/27/18.
 */
public class Guess {
    private String game;
    private String guess;

    public String getGame() {
        return game;
    }
    public String getGuess(){
        return guess;
    }
    public void setGuess(String guess){
        this.guess = guess;
    }
    public void setGame(String game){
        this.game = game;
    }
}
