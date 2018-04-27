package com.models;

public class StartedGame {
    private String gameId;
    private String guessedWord;

    public StartedGame(Game g){
        gameId = g.getId();
        guessedWord = g.getGuessed_word();
    }

    public String getGameId() {
        return gameId;
    }

    public String getGuessedWord() {
        return guessedWord;
    }
}
