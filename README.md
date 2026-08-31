# Sudoku Solver

###### Author: Petter Bergström Talje

A modular JavaFX application for generating, playing, and solving Sudoku puzzles across multiple board sizes and difficulties.
The project is structured into independent Maven modules to clearly separate UI, game logic, puzzle generation, and core models.


## Features

- Supports board sizes 4x4, 6x6, and 9x9.
- Enforces Sudoku rules for valid number placement, live as the player plays.
- Difficulty levels Easy, Medium, Hard.
- Optional, configurable reaction to a rule-breaking move:
   - Cheat Mode: reverts the move so the player can retry, and unlocks hints —
     revealing one cell at a time or instantly solving the rest of the board.
     Both draw from a solution computed live in the background against the
     board's current state, so they stay consistent with the player's own
     valid moves even when the puzzle admits more than one solution.
   - End Game on Mistake: ends the game on a rule-breaking move, permanently
     marking the offending cell and framing the row and/or subgrid causing
     the conflict in red.
- Internationalization (i18n) with switchable language files.

## Technologies Used

- **Java 17** – Core language for all modules
- **JavaFX** – User interface framework (FXML views, controllers, styling)
- **Maven (Multi-Module)** – Build system and modular project structure
- **SLF4J + Logback** – Unified logging facade with Logback backend
- **JUnit 5 + Mockito** – Unit testing and mocking
- **ResourceBundle (i18n)** – Localization and dynamic language switching

## Project Structure
The project is designed as a multi-module Maven structure, separating UI, game logic, puzzle generation, and core models for clean boundaries and testability.

```
    sudoku-solver/
     |
     ├─ app/          -->   JavaFX startup & main application class
     |
     ├─ core/         -->    SudokuBoard (domain model) and its generation package
     |                       (SudokuBuilder, SolutionGenerator) for puzzle creation
     |
     ├─ game/         -->    GameService: central gameplay coordinator.
     |                       LiveSolutionTracker keeps a valid completion of the
     |                       current board ready in the background for hints/solve
     |
     ├─ shared/       -->    Cross-module interfaces, listeners, and shared constants/helpers
     |
     ├─ ui/           -->    Controllers, FXML, ViewModels, UI factories & event handlers
     |                       Observes game updates via CellViewListener
     |
     └─ validation/   -->    Validator: checks rows, columns and subgrids against
                              Sudoku rules, both per move and on a finished board
     
```


## Run:
From the project root, run:
```bash
   mvn clean javafx:run -pl app
```
No additional runtime configuration is required as long as Java 17 and Maven are installed.

## Available Languages

The application supports multiple languages through ResourceBundle files stored in module named "shared":
   
   _shared/src/main/resources/i18n/Resource Bundle 'messages'_

##  Enjoy!
