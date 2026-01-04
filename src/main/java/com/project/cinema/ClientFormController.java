package com.project.cinema;

import com.project.cinema.dao.ClientDao;
import com.project.cinema.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

public class ClientFormController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;

    private App app;
    private ClientDao clientDao;

    public void setApp(App app) {
        this.app = app;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @FXML
    private void onSaveClick(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showAlert("Error", "First name, Last name and Email are required");
            return;
        }

        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhone(phone);

        try {
            clientDao.save(client);
            saveClientToFile(client);
            showAlert("Success", "The client has been successfully added");
            if (app != null) app.showMainMenu();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Couldn't save the client: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelClick(ActionEvent event) {
        if (app != null) app.showMainMenu();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveClientToFile(Client client) {
        String filename = "clientExport.txt";

        try (FileWriter fileWriter = new FileWriter(filename, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String line = String.format("ID: %d | Name: %s %s | Email: %s | Phone: %s",
                    client.getClientId(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getEmail(),
                    client.getPhone());
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        } catch (IOException e) {
            showAlert("Error", "Could not save client: " + e.getMessage());
        }
    }
}