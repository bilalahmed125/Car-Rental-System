module com.bilal {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.bilal to javafx.fxml;
    exports com.bilal;
}
