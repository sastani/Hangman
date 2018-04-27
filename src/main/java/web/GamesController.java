package web;

import models.Game;
import exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public void createGame(){
        Game g = new Game(word_list);
        this.game = g;
    }

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

    @RequestMapping(value = "/guess {gameId, guess, incorrect, status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void returnResult(){

    }

    public void compareWords(Character c, Game g){
        //automatically turn letter to lowercase
        String guess = c.toString().toLowerCase();
        Game g = this.game;
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
