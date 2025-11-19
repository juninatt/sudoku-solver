# Sudoku Solver

Sudoku Solver is a user-friendly JavaFX application that lets you play and solve Sudoku puzzles. 
Supports several board sizes and difficulties and includes a cheat mode.
## Features

- Supports board sizes 4x4, 6x6, and 9x9.  
- Real-time validation and visual feedback.
- Enforces Sudoku rules for valid number placement.
- Difficulty levels Easy, Medium, Hard.
- Cheat Mode: optional mode where users can:
   - Reveal one correct cell at a time.
   - Instantly solve the entire puzzle.
- Internationalization (i18n) with switchable language files.
- Guaranteed unique solutions using backtracking and recursive validation.

## Technologies Used

- **JavaFX** – For the user interface.
- **Java** – Core logic and game mechanics.
- **Maven** – Project management and dependency handling.

## Project Structure (Simplified)

```
    sudoku-solver/
    ├─ src/main/java/se/pbt/sudokusolver/
    │  ├─ app/          # Application entrypoint and startup
    │  ├─ builders/     # Sudoku generation and solution logic
    │  ├─ controllers/  # JavaFX controllers
    │  ├─ models/       # Core domain models
    │  ├─ ui/           # Custom UI components
    │  ├─ utils/        # Localization, constants, helpers
    │  └─ viewmodels/   # Bridge between models and UI
    │
    └─ src/main/resources/
       ├─ fxml/         # FXML view definitions
       ├─ i18n/         # Localization bundles
       └─ styles/       # CSS stylesheets
```


## Run:
From the project root, simply run:
```bash
   mvn clean javafx:run -pl app
```
No additional runtime configuration is required as long as Java 17 and Maven are installed.

## Available Languages

The application supports multiple languages through ResourceBundle files stored in:
   
   _src/main/resources/i18n/Resource Bundle 'messages'_

##  Enjoy! 
