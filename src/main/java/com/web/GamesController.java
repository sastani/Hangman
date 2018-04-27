package com.web;

import com.models.Game;
import com.models.Request;
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
import javax.annotation.Resource;
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
    @RequestMapping(value = "/guess", method = RequestMethod.POST)
    @ResponseBody
    public Game makeGuess(@RequestBody final Request req, HttpSession session) throws GameDoesNotExistException, InvalidCharacterException{
        String game = req.getGame();
        String guess = req.getGuess();
        Game g = getGame(game,session);

        String gameId = g.getId();
        if(gameId.equals(game) && guess.length() > 0) {
            boolean correct = compareWords(guess, g);
            if(!correct){
                g.incIncorrect_guesses();
            }
            g.setStatus();
        }
        else{
            if(!gameId.equals(game)) {
                throw new GameDoesNotExistException(game);
            }
            else{
                throw new InvalidCharacterException(guess);
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

    private boolean compareWords(String c, Game g){
        //automatically turn letter to lowercase
        String guess = c.toLowerCase();
        Character ch = guess.charAt(0);
        g.setGuessedChars(ch);
        String word = g.getWord();
        boolean correct;
        //check if word contains given char
        if(word.contains(guess)){
            correct = true;
            ArrayList<Integer> charInd = new ArrayList<>();
            //find all indices where guessed character is located in word
            for(int i=0; i < word.length(); i++){
                Character wc = word.charAt(i);
                if(wc.equals(ch)){
                    charInd.add(i);
                }
            }
            //change game's guessed word
            g.setGuessedWord(ch, charInd);
        }
        //increase incorrect guesses if it does not
        else{
            correct = false;
        }
        return correct;

    }

}
