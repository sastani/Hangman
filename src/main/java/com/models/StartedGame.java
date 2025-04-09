package com.models;

public class StartedGame {
    private String gameId;
    private String word;

    public StartedGame(Game g){
        gameId = g.getId();
        word = g.getGuessed_word();
    }

    public String getGameId() {
        return gameId;
    }

    public String getWord() {
        return word;
    }
}

