package com.example.jp_1;

import com.example.jp_1.dao.GenreDao;
import com.example.jp_1.dao.MovieDao;
import com.example.jp_1.model.Genre;
import com.example.jp_1.model.Movie;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MovieFormController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField directorField;
    @FXML
    private ComboBox<Genre> genreComboBox;
    @FXML
    private TextField durationField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label statusLabel;

    private MovieDao movieDao;
    private GenreDao genreDao;

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    public void setDaos(MovieDao movieDao, GenreDao genreDao) {
        this.movieDao = movieDao;
        this.genreDao = genreDao;

        loadGenres();
    }

    @FXML
    public void initialize() {
        genreComboBox.setConverter(new StringConverter<Genre>() {
            @Override
            public String toString(Genre genre) {
                return genre == null ? "" : genre.getName();
            }

            @Override
            public Genre fromString(String string) {
                return null;
            }
        });
    }

    private void loadGenres() {
        if (genreDao == null) {
            return;
        }

        try {
            List<Genre> genres = genreDao.findAll();
            genreComboBox.setItems(FXCollections.observableArrayList(genres));
        } catch (SQLException e) {
            statusLabel.setText("Error loading genres: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveClick(ActionEvent event) {
        String title = titleField.getText();
        String director = directorField.getText();
        String durationStr = durationField.getText();
        Genre selectedGenre = genreComboBox.getValue();

        if (title.isEmpty() || director.isEmpty() || durationStr.isEmpty() || selectedGenre == null) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            statusLabel.setText("The duration must be a positive number.");
            return;
        }

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setDurationMinutes(duration);
        movie.setGid(selectedGenre.getGid());
        movie.setDescription(descriptionArea.getText());

        try {
            if (movieDao != null) {
                movieDao.save(movie);
                if (app != null) {
                    app.showMovieList();
                }
            }
        } catch (Exception e) {
            statusLabel.setText("Saving error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelClick(ActionEvent event) throws IOException {
        if (app != null) {
            app.showMovieList();
        }
    }
}