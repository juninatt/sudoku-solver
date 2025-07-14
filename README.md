# Sudoku Solver

A JavaFX-based Sudoku app that lets users generate, play, and solve puzzles of various sizes and difficulty levels.

## Features
- Supports multiple board sizes: 4x4, 6x6, and 9x9. Larger sizes (12x12, 16x16, 25x25) are currently unsupported due to stability concerns, but can be re-enabled by updating the relevant constants in the `Constants` class.
- Interactive grid with real-time validation.
- Enforces Sudoku rules for valid number placement.
- Internationalization (i18n) with switchable language files.
- Smart puzzle generation with difficulty levels (Easy, Medium, Hard).
- Guaranteed unique solutions using backtracking and recursive validation.

## Technologies Used
- **JavaFX** – For the user interface.
- **Java** – Core logic and game mechanics.

## Project Structure (Simplified)

- `controllers/` – JavaFX controllers for views
- `models/` – Core data models like SudokuBoard, Difficulty
- `viewmodels/` – Logic connecting UI and models
- `builders/` – Classes responsible for generating valid boards
- `ui/` – JavaFX views and cell creation logic
- `utils/` – Helpers for localization, validation, constants
- `resources/fxml/` – View layouts
- `resources/i18n/` – Localized property files


## Installation & Running
1. Clone the repository:
   ```
   git clone https://github.com/juninatt/Sudoku-Solver.git
   ```

2. Build and run the project:
   ```
   mvn clean javafx:run
   ```

## Available Languages

The application supports multiple languages through ResourceBundle files stored in:
   
   _src/main/resources/i18n/Resource Bundle 'messages'_
   

Currently, there are three property files:
- **messages_en.properties** – English
- **messages_es.properties** – Spanish
- **messages_sv.properties** – Swedish

Each file contains the same keys but with text in the respective language. To switch
the language used by the UI, modify the ResourceBundle path in the `Localization` class
to point to the desired file. 


##  Enjoy! 
