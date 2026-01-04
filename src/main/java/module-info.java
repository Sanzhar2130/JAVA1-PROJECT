module com.project.cinema {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    opens com.project.cinema to javafx.fxml;
    opens com.project.cinema.model to javafx.base;
    exports com.project.cinema;
}