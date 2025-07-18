module se.pbt.sudokusolver {
    // --- Required JavaFX Modules ---
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires org.slf4j;

    // --- Exports for External Access ---
    exports se.pbt.sudokusolver.controllers;
    exports se.pbt.sudokusolver.models;
    exports se.pbt.sudokusolver.builders;
    exports se.pbt.sudokusolver.app;
    exports se.pbt.sudokusolver.utils;

    // --- Opens for Reflection (JavaFX FXML) ---
    opens se.pbt.sudokusolver.controllers to javafx.fxml;
    opens se.pbt.sudokusolver.builders to javafx.fxml;
    opens se.pbt.sudokusolver.app to javafx.fxml;
    exports se.pbt.sudokusolver.builders.helpers;
}
