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
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

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

    //GET
    //create new game
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public StartedGame startGame(HttpSession session){
        List<Game> games = getCurrentGames(session);
        Game newGame = new Game(wordList);
        games.add(newGame);
        return new StartedGame(newGame);
    }

    //exception handler for dealing with games that are not active
    @ExceptionHandler(GameOverException.class)
    private ResponseEntity<GameOverInfo> gameOver()
    {
        String s = "Game is already complete";
        GameOverInfo error = new GameOverInfo(s);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //POST
    //make guess
    @RequestMapping(value = "/guess", method = RequestMethod.POST, headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> makeGuess (@RequestBody Guess gameAndLetter, HttpSession session) throws GameDoesNotExistException, InvalidCharacterException {
        String game = gameAndLetter.getGame();
        String guess = gameAndLetter.getGuess();
        Game g = getGame(game, session);
        String gameId = g.getId();
        GameStatus stat = g.getStatus();
        if (!(stat == null)) {
            switch (stat) {
                case ACTIVE:
                    break;
                case WON:
                    return gameOver();
                case LOST:
                    return gameOver();
            }
        }

        if (gameId.equals(game) && guess.length() > 0) {
            char ch = cleanUp(guess);
            boolean correct = compareWords(ch, g);

            if (!correct) {
                g.incIncorrect_guesses();

            } else {
                //change game's guessed word
                g.setGuessedWord(ch);

            }
            g.setStatus();

        } else {
            if (!gameId.equals(game)) {
                throw new GameDoesNotExistException(game);
            } else {
                throw new InvalidCharacterException(guess);
            }
        }
        return new ResponseEntity<>(g, HttpStatus.OK);

    }


    // Find an existing game
    private Game getGame(String id, HttpSession session) throws GameDoesNotExistException{
        List<Game> games = (List<Game>) session.getAttribute("games");
        if(games == null){
            throw new GameDoesNotExistException(id);
        }
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
        String word = g.getWord();
        boolean correct;
        CharSequence cs = Character.toString(ch);
        //check if word contains given char
        if(word.contains(cs)){
            correct = true;
        }
        //increase incorrect guesses if it does not
        else{
            correct = false;
        }
        return correct;

    }

}
