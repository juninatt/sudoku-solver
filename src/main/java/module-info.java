module se.pbt.sudokusolver {
    // --- Required JavaFX Modules ---
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.javafx;

    // --- Exports for External Access ---
    exports se.pbt.sudokusolver.controllers;
    exports se.pbt.sudokusolver.models;
    exports se.pbt.sudokusolver.app;

    // --- Opens for Reflection (JavaFX FXML) ---
    opens se.pbt.sudokusolver.controllers to javafx.fxml;
    opens se.pbt.sudokusolver.app to javafx.fxml;
}
