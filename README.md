# Sudoku Solver

###### Author: Petter Bergström Talje

A modular JavaFX application for generating, playing, and solving Sudoku puzzles across multiple board sizes and difficulties.
The project is structured into independent Maven modules to clearly separate UI, game logic, puzzle generation, and core models.


## Features

- Supports board sizes 4x4, 6x6, and 9x9.  
- Enforces Sudoku rules for valid number placement.
- Difficulty levels Easy, Medium, Hard.
- Cheat Mode: optional mode where users can:
   - Reveal one correct cell at a time.
   - Instantly solve the entire puzzle.
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
     ├─ core/         -->    SudokuBoard: core domain model
     |
     ├─ game/         -->    GameService: central gameplay coordinator
     |
     ├─ generation/   -->    SudokuBuilder, SolutionGenerator, UniquenessChecker
     |                       Full puzzle creation logic
     |
     ├─ shared/       -->    Cross-module interfaces, listeners, and shared constants/helpers
     |
     ├─ ui/           -->    Controllers, FXML, ViewModels, UI factories & event handlers
     |                       Observes game updates via CellViewListener
     |
     └─ validation/   -->    Validator: ensures final solution correctness
     
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
