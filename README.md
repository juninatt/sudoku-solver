# Sudoku Solver

A JavaFX-based Sudoku application that allows users to play and solve Sudoku puzzles of various sizes.

## Features
- Supports multiple board sizes (4x4, 6x6, 9x9, 12x12, 16x16, 25x25).
- Interactive grid with real-time validation.
- Enforces Sudoku rules for valid number placement.
- Dynamic UI with automatic subgrid structuring.

## Technologies Used
- **JavaFX** – For the user interface.
- **Java** – Core logic and game mechanics.
- **MVVM Architecture** – Separation of concerns between UI, ViewModel, and game logic.

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