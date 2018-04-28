package com.web;
import com.models.Game;
import com.models.Guess;
import com.exceptions.*;
import com.models.GameStatus;
import com.models.StartedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
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



    @RequestMapping(value = "/guess", method = RequestMethod.POST, headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<GameOverInfo> getCustomerById(@PathVariable String id)
    {
        Customer customer;
        try
        {
            customer = customerService.getCustomerDetail(id);
        }
        catch (CustomerNotFoundException e)
        {
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Customer>(customer,HttpStatus.OK);
    }


    //POST
    //make guess
    @ExceptionHandler(GameOverException.class)
    public ResponseEntity<GameOverInfo> gameOver(Exception e)
    {
        GameOverInfo error = new GameOverInfo(e.toString());
        return new ResponseEntity<GameOverInfo>(error, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/guess", method = RequestMethod.POST, headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public Game makeGuess(@RequestBody Guess gameAndLetter, HttpSession session) throws GameOverException, GameDoesNotExistException, InvalidCharacterException{
        String game = gameAndLetter.getGame();
        String guess = gameAndLetter.getGuess();
        Game g = getGame(game,session);
        String gameId = g.getId();
        GameStatus stat = g.getStatus();
        if(!(stat == null)){
            System.out.println(stat);

            switch(stat){
                case ACTIVE:
                    break;
                case WON:
                    GameOverException gameWon = new GameOverException();
                    gameOver(gameWon);
                    break;
                case LOST:
                    System.out.println("FUCK");
                    GameOverException gameLost = new GameOverException();
                    gameOver(gameLost);
                    System.out.println("THIS");

                    break;
            }
        }

        if(gameId.equals(game) && guess.length() > 0) {
            char ch = cleanUp(guess);
            boolean correct = compareWords(ch, g);

            if(!correct){
                g.incIncorrect_guesses();

            }
            else {
                //change game's guessed word
                g.setGuessedWord(ch);

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
    //clean up input if more than one character/keep only first char
    private char cleanUp(String c){
        //automatically turn letter to lowercase
        String guess = c.toLowerCase();
        char ch = guess.charAt(0);
        return ch;
    }
    private boolean compareWords(char ch, Game g){

        //g.setGuessedChars(ch);
        String word = g.getWord();
        boolean correct;
        CharSequence cs = Character.toString(ch);
        //check if word contains given char
        System.out.println(word);
        if(word.contains(cs)){
            correct = true;
            System.out.println("AYAYA");
        }
        //increase incorrect guesses if it does not
        else{
            correct = false;
        }
        return correct;

    }

}
