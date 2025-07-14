# Sudoku Solver

Sudoku Solver is a user-friendly JavaFX application that lets you play and solve Sudoku puzzles. 
Choose your board size and difficulty, switch between languages, and get help using cheat features when you're stuck. 
The app ensures valid input and unique solutions every time.
## Features

- Supports multiple board sizes: 4x4, 6x6, and 9x9.  
  Larger sizes (12x12, 16x16, 25x25) are currently unsupported due to stability concerns but can be re-enabled by updating the relevant constants in the `Constants` class.
- Interactive grid with real-time validation and visual feedback.
- Enforces Sudoku rules for valid number placement.
- Smart puzzle generation with difficulty levels (Easy, Medium, Hard).
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

- `app/` – Application launcher and setup logic
- `controllers/` – JavaFX controllers for views
- `models/` – Core domain models (e.g., SudokuBoard, Difficulty)
- `viewmodels/` – Bridge between models and UI
- `builders/` – Logic for generating valid Sudoku boards
- `ui/` – JavaFX UI components and styling logic
- `utils/` – Helpers for validation, localization, constants, etc.
- `resources/`
   - `fxml/` – JavaFX view definitions
   - `i18n/` – ResourceBundle property files for localization
   - `styles/` – CSS styling


## Installation & Running
1. Clone the repository:
   ```
   git clone https://github.com/juninatt/Sudoku-Solver.git
   ```

2. Build and run the project:
   ```
   mvn clean javafx:run
   ```
   Make sure you have correct Java version and Maven installed and configured.

## Available Languages

The application supports multiple languages through ResourceBundle files stored in:
   
   _src/main/resources/i18n/Resource Bundle 'messages'_
   

Currently, there are three property files:
- **messages_en.properties** – English
- **messages_es.properties** – Spanish
- **messages_sv.properties** – Swedish

To change the default language, update the path used in the Localization class.

##  Enjoy! 
