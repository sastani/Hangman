# Hangman
REST API for playing Hangman. It uses a word list from Duke's CS Department.

To get a new game:
GET
http://localhost:8080/new

To make a guess:
POST
http://localhost:8080/guess
{"game":[gameId] "guess":[character]}

To get a JSON object containing game data for every game in the current session make the following API call:
POST
http://localhost:8080/games

If you pass a string of characters that is longer than one character,
it only keeps the first character and uses that to make a guess.

Additionally, if you guess a character that is correct multiple times, you are not penalized.
However, if you guess a wrong character multiple times, you will lose a turn each time.
