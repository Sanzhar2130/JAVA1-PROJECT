package com.example.jp_1;

import com.example.jp_1.dao.ClientDao;
import com.example.jp_1.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ClientFormController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

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
            showAlert("Ошибка", "Имя, Фамилия и Email обязательны!");
            return;
        }

        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhone(phone);

        try {
            clientDao.save(client);
            showAlert("Успех", "Клиент успешно добавлен!");
            if (app != null) app.showMainMenu();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка БД", "Не удалось сохранить клиента: " + e.getMessage());
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
}