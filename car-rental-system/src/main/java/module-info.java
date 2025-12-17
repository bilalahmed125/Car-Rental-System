module com.bilal {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.bilal to javafx.fxml;
    exports com.bilal;
}
