package com.example.jp_1;

import com.example.jp_1.dao.SessionDao;
import com.example.jp_1.model.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class SessionListController {
    private App app;
    private SessionDao sessionDao;

    @FXML
    private TableView<Session> sessionTable;
    @FXML
    private TableColumn<Session, Integer> colId;
    @FXML
    private TableColumn<Session, Integer> colMovie;
    @FXML
    private TableColumn<Session, Integer> colHall;
    @FXML
    private TableColumn<Session, LocalDateTime> colTime;
    @FXML
    private TableColumn<Session, Double> colPrice;

    public void setApp(App app) {
        this.app = app;
    }

    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
        loadData();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("sessId"));
        colMovie.setCellValueFactory(new PropertyValueFactory<>("mid"));
        colHall.setCellValueFactory(new PropertyValueFactory<>("hid"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
    }

    private void loadData() {
        if (sessionDao == null) return;
        try {
            sessionTable.setItems(FXCollections.observableArrayList(sessionDao.findAll()));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load sessions: " + e.getMessage());
        }
    }

    @FXML
    private void handleSelectSession() {
        Session selected = sessionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            app.showSessionView(selected);
        } else {
            showAlert("Warning", "Select session from the list!");
        }
    }

    @FXML
    private void handleBack() {
        app.showMainMenu();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleCreateSession() {
        app.showSessionForm(null);
    }

    @FXML
    private void handleEditSession() {
        Session selected = sessionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            app.showSessionForm(selected);
        } else {
            showAlert("Warning", "Select session from the list!");
        }
    }

    @FXML
    private void handleDeleteSession() {
        Session selected = sessionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a session to delete");
            return;
        }
        try {
            if (sessionDao != null) {
                sessionDao.delete(selected.getSessId());
                loadData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete session: " + e.getMessage());
        }
    }
}