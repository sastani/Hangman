package com.web;

import com.models.Game;
import com.exceptions.*;
import com.models.GameStatus;
import com.models.StartedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
/**
 * Created by sinaastani on 4/26/18.
 *
 */

@RestController
class GamesController {
    private ArrayList<String> wordList;
    private @Autowired ServletContext servletContext;

    @PostConstruct
    public void init(){
        GamesInit init_games = new GamesInit();
        this.wordList = init_games.getWordList();
    }
    //get list of games player is playing or has played during session
    @RequestMapping("/games")
    public List<Game> getGameList(HttpSession session) {
        List<Game> games = (List<Game>) session.getAttribute("games");
        return games;
    }

    //POST
    //create new game
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public StartedGame startGame(HttpSession session){
        List<Game> games = getCurrentGames(session);
        Game newGame = new Game(wordList);
        games.add(newGame);
        return new StartedGame(newGame);
    }
    //POST
    //make guess
    @RequestMapping(value = "/guess/{game, guess}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Game makeGuess(@PathVariable String id, Character c, HttpSession session) throws GameDoesNotExistException, InvalidCharacterException{
        Game g = getGame(id,session);

        String s = c.toString();
        String gameId = g.getId();
        if(gameId.equals(id) && s.length() > 0) {
            boolean correct = compareWords(c, g);
            if(!correct){
                g.incIncorrect_guesses();
            }
            g.setStatus();
        }
        else{
            if(!gameId.equals(id)) {
                throw new GameDoesNotExistException(id);
            }
            else{
                throw new InvalidCharacterException(c.toString());
            }
        }
        return g;
    }

    // Find an existing game
    private Game getGame(String id, HttpSession session) throws GameDoesNotExistException{
        List<Game> games = (List<Game>) session.getAttribute("games");
        Game g = null;
        for(int i = 0; i < games.size(); i++){
            g = games.get(i);
            if(g.getId().equals(id)){
                break;
            }
        }
        if (g == null) {
            throw new GameDoesNotExistException(id);
        }
        return g;
    }
    //Get games in session
    private List<Game> getCurrentGames(HttpSession session) {
        List<Game> games = (List<Game>) session.getAttribute("games");

        if (games == null) {
            games = new ArrayList<>();
            session.setAttribute("games", games);
        }
        return games;
    }

    private boolean compareWords(Character c, Game g){
        //automatically turn letter to lowercase
        String guess = c.toString().toLowerCase();
        g.setGuessedChars(c);
        String word = g.getWord();
        boolean correct;
        //check if word contains given char
        if(word.contains(guess)){
            correct = true;
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
            correct = false;
        }
        return correct;

    }

}
