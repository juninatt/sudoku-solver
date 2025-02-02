module se.pbt.sudokusolver {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens se.pbt.sudokusolver to javafx.fxml;
    exports se.pbt.sudokusolver;
}