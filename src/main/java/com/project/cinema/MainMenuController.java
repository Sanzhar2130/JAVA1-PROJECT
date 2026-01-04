package com.project.cinema;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainMenuController {
    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private void goToMovies(ActionEvent event) throws IOException {
        app.showMovieList();
    }

    @FXML
    private void goToSessions(ActionEvent event) {
        app.showSessionList();
    }

    @FXML
    private void goToClientForm(ActionEvent event) {
        app.showClientForm();
    }

    @FXML
    private void exitApp(ActionEvent event) {
        System.exit(0);
    }
}