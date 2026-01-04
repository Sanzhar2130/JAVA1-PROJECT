package com.project.cinema;


import com.project.cinema.dao.BookingDao;
import com.project.cinema.dao.ClientDao;
import com.project.cinema.dao.SeatDao;
import com.project.cinema.dao.TicketDao;
import com.project.cinema.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.scene.control.ComboBox;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionViewController {

    @FXML private Label movieTitleLabel;
    @FXML private Label sessionInfoLabel;
    @FXML private GridPane seatGrid;
    @FXML private Label totalPriceLabel;
    @FXML private ComboBox<Client> clientComboBox;

    private App app;
    private Session currentSession;
    private SeatDao seatDao;
    private TicketDao ticketDao;
    private BookingDao bookingDao;
    private ClientDao clientDao;

    private final List<Seat> selectedSeats = new ArrayList<>();

    public void setApp(App app) {
        this.app = app;
    }

    public void initData(Session session, SeatDao seatDao, TicketDao ticketDao, BookingDao bookingDao, ClientDao clientDao) {
        this.currentSession = session;
        this.seatDao = seatDao;
        this.ticketDao = ticketDao;
        this.bookingDao = bookingDao;
        this.clientDao = clientDao;

        updateHeader();
        loadSeats();
        loadClients();
    }

    private void updateHeader() {
        movieTitleLabel.setText("Session ID " + currentSession.getSessId());
        sessionInfoLabel.setText("Hall ID: " + currentSession.getHid() + ", Price: " + currentSession.getBasePrice());
    }

    private void loadSeats() {
        try {
            List<Seat> allSeats = seatDao.findByHid(currentSession.getHid());
            List<Integer> occupiedIds = ticketDao.findOccupiedSid(currentSession.getSessId());

            seatGrid.getChildren().clear();

            for (Seat seat : allSeats) {
                Button seatBtn = new Button(seat.getRowNumber() + "-" + seat.getSeatNumber());
                seatBtn.setPrefSize(50, 50);

                if (occupiedIds.contains(seat.getSid())) {
                    seatBtn.setStyle("-fx-background-color: #ffcccc; -fx-border-color: red;");
                    seatBtn.setDisable(true);
                } else {
                    seatBtn.setStyle("-fx-background-color: #ccffcc; -fx-border-color: green;");
                    seatBtn.setOnAction(e -> toggleSeatSelection(seatBtn, seat));
                }
                seatGrid.add(seatBtn, seat.getSeatNumber() - 1, seat.getRowNumber() - 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading seats", e.getMessage());
        }
    }

    private void toggleSeatSelection(Button btn, Seat seat) {
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            btn.setStyle("-fx-background-color: #ccffcc; -fx-border-color: green;");
        } else {
            selectedSeats.add(seat);
            btn.setStyle("-fx-background-color: #ffff99; -fx-border-color: orange;");
        }
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = selectedSeats.size() * currentSession.getBasePrice();
        totalPriceLabel.setText("Total: " + total);
    }

    @FXML
    private void handleBuy() {
        if (selectedSeats.isEmpty()) {
            showAlert("Error", "Select at least one seat!");
            return;
        }

        Client selectedClient = clientComboBox.getValue();
        if (selectedClient == null) {
            showAlert("Error", "Please select a client!");
            return;
        }

        try {
            Booking booking = new Booking();
            booking.setClientId(selectedClient.getClientId());
            booking.setBookingCode("B-" + System.currentTimeMillis());
            booking.setCreatedAt(LocalDateTime.now());
            booking.setStatus("Confirmed");
            booking.setPaymentMethod("Card");
            booking.setTotalPrice(selectedSeats.size() * currentSession.getBasePrice());

            bookingDao.save(booking);

            for (Seat seat : selectedSeats) {
                Ticket ticket = new Ticket();
                ticket.setBid(booking.getBid());
                ticket.setSessId(currentSession.getSessId());
                ticket.setSid(seat.getSid());
                ticket.setTtId(1);
                ticket.setPrice(currentSession.getBasePrice());

                ticketDao.save(ticket);
            }

            showAlert("Success", "Tickets purchased successfully!");
            selectedSeats.clear();
            updateTotalPrice();
            loadSeats();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to purchase tickets: " + e.getMessage());
        }
    }

    private void loadClients() {
        if (clientDao == null) {
            return;
        }
        try {
            List<Client> clients = clientDao.findAll();
            clientComboBox.setItems(FXCollections.observableArrayList(clients));
            clientComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Client c) {
                    return c == null ? "" : c.getFirstName() + " " + c.getLastName();
                }
                @Override
                public Client fromString(String string) { return null; }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        if (app != null) {
            app.showSessionList();
        }
    }
}