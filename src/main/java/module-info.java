module com.example.jp_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    opens com.example.jp_1 to javafx.fxml;
    opens com.example.jp_1.model to javafx.base;
    exports com.example.jp_1;
}