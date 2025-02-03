module se.pbt.sudokusolver {
    // --- Required JavaFX Modules ---
    requires javafx.controls;
    requires javafx.fxml;

    // --- Exports for External Access ---
    exports se.pbt.sudokusolver.controllers;
    exports se.pbt.sudokusolver.models;
    exports se.pbt.sudokusolver.services;
    exports se.pbt.sudokusolver.app;

    // --- Opens for Reflection (JavaFX FXML) ---
    opens se.pbt.sudokusolver.controllers to javafx.fxml;
    opens se.pbt.sudokusolver.app to javafx.fxml;
}
