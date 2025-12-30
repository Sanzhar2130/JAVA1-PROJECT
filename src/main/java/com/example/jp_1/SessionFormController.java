package com.example.jp_1;

import com.example.jp_1.dao.HallDao;
import com.example.jp_1.dao.MovieDao;
import com.example.jp_1.dao.SessionDao;
import com.example.jp_1.model.Hall;
import com.example.jp_1.model.Movie;
import com.example.jp_1.model.Session;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SessionFormController {
    private App app;
    private SessionDao sessionDao;
    private MovieDao movieDao;
    private HallDao hallDao;

    @FXML
    private ComboBox<Movie> movieComboBox;
    @FXML
    private ComboBox<Hall> hallComboBox;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField priceField;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setApp(App app) {
        this.app = app;
    }

    public void setDaos(SessionDao sessionDao, MovieDao movieDao, HallDao hallDao) {
        this.sessionDao = sessionDao;
        this.movieDao = movieDao;
        this.hallDao = hallDao;
        loadData();
    }

    @FXML
    public void initialize() {
        movieComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Movie movie) {
                return movie == null ? "" : movie.getTitle();
            }

            @Override
            public Movie fromString(String string) {
                return null;
            }
        });

        hallComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Hall hall) {
                return hall == null ? "" : hall.getName();
            }

            @Override
            public Hall fromString(String string) {
                return null;
            }
        });
    }

    private void loadData() {
        if (movieDao == null || hallDao == null) {
            return;
        }
        try {
            List<Movie> movies = movieDao.findAll();
            List<Hall> halls = hallDao.findAll();
            movieComboBox.setItems(FXCollections.observableArrayList(movies));
            hallComboBox.setItems(FXCollections.observableArrayList(halls));
        } catch (SQLException e) {
            showAlert("Error: Failed to load data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        Movie selectedMovie = movieComboBox.getValue();
        Hall selectedHall = hallComboBox.getValue();
        String startTimeStr = startTimeField.getText();
        String priceStr = priceField.getText();

        if (selectedMovie == null || selectedHall == null || startTimeStr.isEmpty() || priceStr.isEmpty()) {
            showAlert("Error: Please fill in all required fields");
            return;
        }

        LocalDateTime startTime;
        double price;

        try {
            startTime = LocalDateTime.parse(startTimeStr, formatter);
        } catch (Exception e) {
            showAlert("Error: Invalid start time: " + e.getMessage());
            return;
        }

        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Error: Invalid price format: " + e.getMessage());
            return;
        }

        Session session = new Session();
        session.setMid(selectedMovie.getMid());
        session.setHid(selectedHall.getHid());
        session.setStartTime(startTime);
        session.setBasePrice(price);

        try {
            if (sessionDao != null) {
                sessionDao.save(session);
                if (app != null) {
                    app.showSessionList();
                }
            }
        } catch (SQLException e) {
            showAlert("Error: Failed to save session: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        if (app != null) {
            app.showSessionList();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
