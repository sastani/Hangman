package com.models;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Created by sinaastani on 4/24/18.
 */

public class Game {

    private final String gameId;
    private final String word;
    private String guessedWord;
    private Set<Character> guessedChars;
    private GameStatus status;
    private int incorrectGuesses;
    private static final int MAX_TRIES = 7;
    //contructor for making a game instance
    public Game(ArrayList<String> wlist){
        this.gameId = createId();
        this.word = chooseWord(wlist);
        this.incorrectGuesses = 0;
        this.guessedWord = initGameString(this.word.length());
    }


    //getters
    public String getId(){
        return gameId;
    }

    @JsonIgnore
    public String getWord(){
        return word;
    }

    public String getGuessed_word(){
        return guessedWord;
    }

    public int getIncorrect_guesses(){
        return incorrectGuesses;
    }

    public GameStatus getStatus(){
        return status;
    }

    public Set<Character> getGuessedChars() {
        return guessedChars;
    }

    //setters
    public void setStatus(){
        if(word.equals(guessedWord)) {
            this.status = GameStatus.WON;
        }
        else{
            if(incorrectGuesses < MAX_TRIES) {
                this.status = GameStatus.ACTIVE;
            }
            if(incorrectGuesses == MAX_TRIES){
                this.status = GameStatus.LOST;
            }
        }


    }

    public void incIncorrect_guesses(){
        this.incorrectGuesses++;
    }

    public void setGuessedChars(Character c){
        this.guessedChars.add(c);
    }

    public void setGuessedWord(Character c, ArrayList<Integer> indices) {
        String newGuessedWord;
        StringBuilder sb = new StringBuilder();
        int numChars = getGuessed_word().length();
        for (int i = 0; i < numChars; i++) {
            if (indices.contains(i)) {
                sb.append(c);
            } else {
                sb.append("_");
            }
        }
        newGuessedWord = sb.toString();
        this.guessedWord = newGuessedWord;
    }

    //choose word from word list
    private static String chooseWord(ArrayList<String> wlist){
        String chosen_word;
        int num_words = wlist.size();
        Random r = new Random(System.currentTimeMillis());
        int ran_int = r.nextInt(num_words);
        chosen_word = wlist.get(ran_int);
        return chosen_word;
    }

    private static String initGameString(int word_len){
        String w;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < word_len; i++) {
            sb.append("_");
        }
        w = sb.toString();
        return w;
    }
    //generate id for game
    private static String createId(){
        String alphabet= "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        String id;
        Random r = new Random(System.currentTimeMillis());
        //generate random sequence of 6 chars
        int id_len = 6;
        char ran_char;
        for(int i=0; i < id_len; i++){
            //choose random letter in alphabet
            ran_char = alphabet.charAt(r.nextInt(26));
            sb.append(ran_char);
        }
        id = sb.toString();
        return id;
    }
}
