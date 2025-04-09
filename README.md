## RESTful Hangman

REST API for playing Hangman creating using Spring Boot. It uses a word list from Duke's CS Department. This program requires Java 17+. It has been tested with OpenJDK 17 and above.

# Installation:

## Clone this repository
``` 
git clone https://github.com/sastani/Hangman 
```
## Install maven
* Download maven from ``https://maven.apache.org/download.cgi``
* Extract the maven archive using ``unzip`` or ``tar``
* Add the bin directory of the extracted folder to your ``PATH``

## Compile
Run the following to build the project:
```
mvn package
```
## Run

```
java -jar target/Hangman-0.0.1-SNAPSHOT.jar
```

## Making requests

To get a new game:

**GET localhost:8080/new**

To make a guess:

**POST localhost:8080/guess**

Request body:
```
{
    "game": [gameId],
    "guess": [character]
}
```

For example:
```
{
    "game": "ruzklt",
    "guess": "a"
}
```

To get a JSON object containing game data for every game in the current session make the following API call:

**POST localhost:8080/games**

## Sidenotes
If you pass a string of characters that is longer than one character,
it only keeps the first character and uses that to make a guess.

Additionally, if you guess a character that is correct multiple times, you are not penalized.
However, if you guess a wrong character multiple times, you will lose a turn each time.
