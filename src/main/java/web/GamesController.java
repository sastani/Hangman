package web;

import models.Game;
import exceptions.*;
import models.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Created by sinaastani on 4/26/18.
 */
@RestController
@RequestMapping(value = "/", method = RequestMethod.GET)
class GamesController {
    private final GamesInit init_games;
    private final ArrayList<String> word_list;
    private Game game;

    @Autowired
    GamesController(){
        this.init_games = new GamesInit();
        this.word_list = init_games.getWordList();
        this.game = null;
    }
    //POST
    //create new game
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public void createGame(){
        this.game = new Game(word_list);
    }
    //POST
    //make guess
    @RequestMapping(value = "/guess {game, guess}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void makeGuess(String id, Character c) throws GameDoesNotExistException, InvalidCharacterException{
        Game g = this.game;

        String s = c.toString();
        String gameId = g.getId();
        if(gameId.equals(id) && s.length() > 0) {
            compareWords(c, g);
        }
        else{
            if(!gameId.equals(id)) {
                throw new GameDoesNotExistException(id);
            }
            else{
                throw new InvalidCharacterException(c.toString());
            }
        }
    }

    //GET
    //get current game data
    @RequestMapping(value = "/guess {gameId, guess, incorrect, status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void returnResult(){
        Game g = this.game;
        String id = g.getId();
        String guessedWord = g.getGuessed_word();
        int incorrGuesses = g.getIncorrect_guesses();
        GameStatus gs = g.getStatus();

    }

    //DELETE
    //destroy current game
    @RequestMapping(value="/{gameId}", method = RequestMethod.DELETE)
    public void destroyGame(){

    }

    private void compareWords(Character c, Game g){
        //automatically turn letter to lowercase
        String guess = c.toString().toLowerCase();
        g.setGuessedChars(c);
        String word = g.getWord();
        //check if word contains given char
        if(word.contains(guess)){
            ArrayList<Integer> charInd = new ArrayList<>();
            //find all indices where guessed character is located in word
            for(int i=0; i < word.length(); i++){
                Character ch = word.charAt(i);
                if(ch.equals(c)){
                    charInd.add(i);
                }
            }
            //change game's guessed word
            g.setGuessedWord(c, charInd);
        }
        //increase incorrect guesses if it does not
        else{
            g.incIncorrect_guesses();
        }

    }



}
