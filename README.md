# Misere Nim Game API

This is a Spring Boot application implementing the Misère version of the Nim game with one heap.

## Game Rules
- The game is played with one heap of matches.
- Two players take turns (Human and Computer).
- On each turn, a player must take 1-3 matches.
- The player who takes the last match loses.

## How to Build and Run

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Gradle

### Build the Application
To compile the code and create an executable JAR file:
```bash
./gradlew build
```
The JAR file will be located at `build/libs/Misere_Nim_Game-1.0-SNAPSHOT.jar`.

### Run the Application
You can run the application directly using Gradle:
```bash
./gradlew bootRun
```
The application will start on `http://localhost:8080`.

## Example Curl Commands

### Start a New Game
```bash
curl -X POST "http://localhost:8080/api/game/start?initialHeap=13&strategy=optimal"
```

### Make a Move
```bash
curl -X POST "http://localhost:8080/api/game/move?matches=3"
```

### Get Game State
```bash
curl "http://localhost:8080/api/game/state"
```

## Notes
- Human player always plays first. I've thought of making the option configurable, but request to start a new game is always made by the human player so it makes more sense that way.
- processTurn() method processes both the human and computer moves for a single round. I wanted to separate human and computer moves, but it would be more natural to do so if there were separate triggers such as having a UI that lets human trigger computer moves.
