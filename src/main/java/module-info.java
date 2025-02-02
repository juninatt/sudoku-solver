module se.pbt.sudokusolver {
    // JavaFX Modules
    requires javafx.controls;
    requires javafx.fxml;

    // External Libraries
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    // Package Exports
    exports se.pbt.sudokusolver.controllers;
    exports se.pbt.sudokusolver.app;
    exports se.pbt.sudokusolver.models;

    // Open Packages to JavaFX for Reflection
    opens se.pbt.sudokusolver.controllers to javafx.fxml;
    opens se.pbt.sudokusolver.app to javafx.fxml;
    opens se.pbt.sudokusolver.models to javafx.fxml;
}
