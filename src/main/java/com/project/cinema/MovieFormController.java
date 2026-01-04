package com.project.cinema;

import com.project.cinema.dao.GenreDao;
import com.project.cinema.dao.MovieDao;
import com.project.cinema.model.Genre;
import com.project.cinema.model.Movie;
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
    private Movie movieToEdit = null;

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
        String description = descriptionArea.getText();

        if (title.isEmpty() || director.isEmpty() || durationStr.isEmpty() || selectedGenre == null || description.isEmpty()) {
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
        movie.setDescription(description);

        try {
            if (movieDao != null) {
                if (movieToEdit == null) {
                    movieDao.save(movie);
                } else {
                    movie.setMid(movieToEdit.getMid());
                    movieDao.update(movie);
                }
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

    public void setMovieToEdit(Movie movie) {
        this.movieToEdit = movie;
        if (movie != null) {
            titleField.setText(movie.getTitle());
            directorField.setText(movie.getDirector());
            durationField.setText(String.valueOf(movie.getDurationMinutes()));
            descriptionArea.setText(movie.getDescription());

            if (movie.getGid() != null) {
                for (Genre g : genreComboBox.getItems()) {
                    if (g.getGid() == movie.getGid()) {
                        genreComboBox.setValue(g);
                        break;
                    }
                }
            }
        }
    }
}