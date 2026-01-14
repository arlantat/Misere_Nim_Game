# Misere Nim Game API

This is a Spring Boot application implementing the Misere version of the Nim game with one heap.

## Game Rules
- The game is played with one heap of matches.
- Two players take turns (Human and Computer).
- On each turn, a player must take at least **one** and at most **three** matches from the heap.
- The player who takes the **last match loses**.

## How to Build and Run

### Prerequisites
- JDK 17 or higher

### Build
```bash
./gradlew build
```

### Run
```bash
./gradlew bootRun
```
The application will start on `http://localhost:8080`.

## API Endpoints

### Start a New Game
Initializes the heap with a given number of matches and selects a strategy.

**URL**: `/api/game/start`
**Method**: `POST`
**Parameters**:
- `initialHeap` (optional, default: 13): Number of matches to start with.
- `strategy` (optional, default: `optimal`): Strategy for the computer (`optimal` or `random`).

**Example**:
```bash
curl -X POST "http://localhost:8080/api/game/start?initialHeap=13&strategy=optimal"
```

### Make a Move
The human player removes matches from the heap. The computer will automatically make its move immediately after.

**URL**: `/api/game/move`
**Method**: `POST`
**Parameters**:
- `matches`: Number of matches to remove (1, 2, or 3).

**Example**:
```bash
curl -X POST "http://localhost:8080/api/game/move?matches=3"
```

### Get Game State
Returns the current heap size, whose turn it is, and whether the game is over.

**URL**: `/api/game/state`
**Method**: `GET`

**Example**:
```bash
curl "http://localhost:8080/api/game/state"
```

## Explanations for Kotlin Beginners (from Java background)

1.  **Data Classes**: `GameState` is a `data class`. In Kotlin, this automatically generates `equals()`, `hashCode()`, `toString()`, and `copy()` methods. It's much more concise than Java's POJOs or Records.
2.  **Null Safety**: The `winner` property in `GameState` is typed as `Player?`. The `?` means it can be null. Kotlin's compiler enforces checks when you try to access nullable types, preventing `NullPointerException`.
3.  **Primary Constructors**: In `GameController`, the constructor is defined in the class header: `class GameController(private val gameService: GameService)`. This both declares a field and initializes it via the constructor.
4.  **Properties**: In Kotlin, we use properties instead of explicit getters/setters. `val` is for read-only (immutable) and `var` is for mutable properties.
5.  **Default Arguments**: Functions like `startNewGame` have default values for parameters, reducing the need for method overloading.
6.  **Expressions**: Many things in Kotlin are expressions. For example, the `remainder` calculation and `if` blocks can return values directly.
