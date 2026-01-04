package com.project.cinema;

import com.project.cinema.dao.*;
import com.project.cinema.model.Movie;
import com.project.cinema.model.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application{
    private Stage primaryStage;

    private final DatabaseConnection dbConnection = new DatabaseConnection();

    private final MovieDao movieDao = new MovieDao(dbConnection);
    private final SessionDao sessionDao = new SessionDao(dbConnection);
    private final SeatDao seatDao = new SeatDao(dbConnection);
    private final TicketDao ticketDao = new TicketDao(dbConnection);
    private final BookingDao bookingDao = new BookingDao(dbConnection);
    private final GenreDao genreDao = new GenreDao(dbConnection);
    private final HallDao hallDao = new HallDao(dbConnection);
    private final ClientDao clientDao = new ClientDao(dbConnection);

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Cinema Project ZHA0067");
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        showMainMenu();

    }

    public void showMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
            Parent root = loader.load();
            MainMenuController controller = loader.getController();
            controller.setApp(this);
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMovieList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("movieList.fxml"));
            Parent root = loader.load();
            MovieListController controller = loader.getController();
            controller.setApp(this);
            controller.setMovieDao(this.movieDao);
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showMovieForm(Movie movieToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("movieForm.fxml"));
            Parent root = loader.load();
            MovieFormController controller = loader.getController();
            controller.setApp(this);
            controller.setDaos(this.movieDao, this.genreDao);

            if (movieToEdit != null) {
                controller.setMovieToEdit(movieToEdit);
            }

            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSessionList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sessionList.fxml"));
            Parent root = loader.load();
            SessionListController controller = loader.getController();
            controller.setApp(this);
            controller.setSessionDao(this.sessionDao);
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void showSessionView(Session session) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sessionView.fxml"));
            Parent root = loader.load();
            SessionViewController controller = loader.getController();// Предполагаем, что вы добавили метод setApp в SessionViewController
            controller.setApp(this);
            controller.initData(session, this.seatDao, this.ticketDao, this.bookingDao, this.clientDao);
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void showSessionForm(Session sessionToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sessionForm.fxml"));
            Parent root = loader.load();
            SessionFormController controller = loader.getController();
            controller.setApp(this);
            controller.setDaos(this.sessionDao, this.movieDao, this.hallDao);

            if (sessionToEdit != null) {
                controller.setSessionToEdit(sessionToEdit);
            }

            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); }
    }

    public void showClientForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clientForm.fxml"));
            Parent root = loader.load();
            ClientFormController controller = loader.getController();
            controller.setApp(this);
            controller.setClientDao(this.clientDao);
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        dbConnection.closeConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}
