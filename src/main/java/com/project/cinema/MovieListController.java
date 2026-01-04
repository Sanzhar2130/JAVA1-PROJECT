package com.project.cinema;

import com.project.cinema.dao.MovieDao;
import com.project.cinema.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MovieListController implements Initializable {
    @FXML
    private TableView<Movie> moviesTable;
    @FXML
    private TableColumn<Movie, Integer> idColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> directorColumn;
    @FXML
    private TableColumn<Movie, Integer> durationColumn;
    @FXML
    private TableColumn<Movie, String> descriptionColumn;
    @FXML
    private Button deleteButton;
    private ObservableList<Movie> moviesList = FXCollections.observableArrayList();
    private MovieDao movieDao;
    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    public void setMovieDao(MovieDao movieDao) {
        this.movieDao = movieDao;
        loadData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("mid"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        moviesTable.setItems(moviesList);
    }

    private void loadData() {
        if (movieDao == null) {
            return;
        }

        moviesList.clear();
        try {
            List<Movie> movies = movieDao.findAll();
            moviesList.addAll(movies);
        } catch (Exception e) {
            showAlertDialog(Alert.AlertType.ERROR, "Database Error", "Failed to load movies: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAddMovie(ActionEvent event) {
        if (app != null) {
            app.showMovieForm(null);
        }
    }

    @FXML
    private void handleDeleteMovie(ActionEvent event) {
        Movie selected = moviesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlertDialog(Alert.AlertType.WARNING, "No Selection", "Please select a movie to delete.");
            return;
        }

        try {
            movieDao.delete(selected.getMid());
            moviesList.remove(selected);
        } catch (Exception e) {
            showAlertDialog(Alert.AlertType.ERROR, "Delete Error", "Failed to delete movie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (app != null) {
            app.showMainMenu();
        }
    }


    private void showAlertDialog(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleEditMovie() {
        Movie selected = moviesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlertDialog(Alert.AlertType.WARNING, "No Selection", "Please select a movie to edit.");
            return;
        }
        app.showMovieForm(selected);
    }

}
