package com.example.projectsem2;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class checkInController implements Initializable {
    @FXML
    private TextField idCard;

    @FXML
    private DatePicker checkIn_date;

    @FXML
    private AnchorPane checkIn_form;

    @FXML
    private DatePicker checkOut_date;

    @FXML
    private Label customerNumber;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private ComboBox<?> roomNumber;

    @FXML
    private ComboBox<?> roomType;

    @FXML
    private Label total;

    @FXML
    private Label totalDays;


    private Connection conn;
    private PreparedStatement prepar;
    private ResultSet rs;
    private Statement stmt;

    public void customerCheckIn() {
        String insertCustomerData = "INSERT INTO customer (customer_id,roomType,roomNumber,firstName,lastName,phoneNumber,idCard,checkIn,checkOut)"
                + "VALUES(?::integer,?,?,?,?,?,?,?::date,?::date)";

        conn = Database.ConnectDB();

        try {

            String customerNum = customerNumber.getText();
            String roomTp = (String)roomType.getSelectionModel().getSelectedItem();
            String roomNum = (String)roomNumber.getSelectionModel().getSelectedItem();
            String firstN = firstName.getText();
            String lastN = lastName.getText();
            String phoneNum = phoneNumber.getText();
            String idCard1 = idCard.getText();
            String checkDate = String.valueOf(checkIn_date.getValue());
            String checkOutDate = String.valueOf(checkOut_date.getValue());
            Alert alert;

            if(customerNum == null || roomTp == null || roomNum == null || firstN == null || lastN == null || phoneNum == null || idCard1 == null
            || checkDate == null || checkOutDate == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields!");
                alert.showAndWait();
            }else {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");

                Optional<ButtonType> option = alert.showAndWait();

                    // INSERT CUSTOMER INFORMATION DATABASE
                if (option.get().equals(ButtonType.OK)) {
                    prepar = conn.prepareStatement(insertCustomerData);
                    prepar.setString(1,customerNum);
                    prepar.setString(2,roomTp);
                    prepar.setString(3,roomNum);
                    prepar.setString(4,firstN);
                    prepar.setString(5,lastN);
                    prepar.setString(6,phoneNum);
                    prepar.setString(7,idCard1);
                    prepar.setString(8,checkDate);
                    prepar.setString(9,checkOutDate);

                    prepar.executeUpdate();

                    //INSERT CUSTOMER RECEIPT DATABASE
                    String date = String.valueOf(checkIn_date.getValue());
                    String totalC = String.valueOf(totalP);
                    String customerN = customerNumber.getText();

                    String customerReceipt = "INSERT INTO customer_receipt(customer_num,total,customer_date)"
                            + "VALUES (?::integer,?::float,?::date)";
                    prepar = conn.prepareStatement(customerReceipt);

                    prepar.setString(1,customerN);
                    prepar.setString(2,totalC);
                    prepar.setString(3,date);

                    prepar.execute();

                    String sqlEditStatus = "UPDATE room SET status ='Occupied' WHERE roomNumber ='"+roomNum+"'";
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sqlEditStatus);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Check-In!");
                    alert.showAndWait();

                    reset();

                }else {
                    return;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        firstName.setText("");
        lastName.setText("");
        phoneNumber.setText("");
        idCard.setText("");
        roomType.getSelectionModel().clearSelection();
        roomNumber.getSelectionModel().clearSelection();
        totalDays.setText("---");
        total.setText("0.0 VND");
    }

    public void totalDays () {

        Alert alert;
        if (!(checkOut_date.getValue().isAfter(checkIn_date.getValue()))){

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid check-out date!");
            alert.showAndWait();
        }else {

            getData.totalDays = checkOut_date.getValue().compareTo(checkIn_date.getValue());

        }
    }

    private float totalP = 0;

    public void displayTotal () {
        totalDays();

        String totalD = String.valueOf(getData.totalDays);

        totalDays.setText(totalD);

        String selectItem = (String) roomNumber.getSelectionModel().getSelectedItem();

        String sql = "SELECT * FROM room WHERE roomNumber = '"+selectItem+"'";

        int priceData = 0;

        conn = Database.ConnectDB();

        try {
            prepar = conn.prepareStatement(sql);
            rs = prepar.executeQuery();

            if (rs.next()) {
                priceData = rs.getInt("price");

            }
                totalP = (float) ((priceData)*getData.totalDays);
                System.out.println("Total Payment: " + totalP);
                System.out.println("priceData: " + priceData);
                total.setText(String.valueOf(totalP)+" VND");

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void roomTypeList() {
        String listType = "SELECT * FROM room WHERE status = 'Available' ";

        conn = Database.ConnectDB();

        try{

            ObservableList listData = FXCollections.observableArrayList();
            prepar = conn.prepareStatement(listType);
            rs = prepar.executeQuery();

            while (rs.next()){
                listData.add(rs.getString("roomType"));
            }
            roomType.setItems(listData);

            roomNumberList();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void roomNumberList() {

        String item = (String) roomType.getSelectionModel().getSelectedItem();

        String availableRoomNumber = "SELECT * FROM room WHERE roomType = '"+item+"' AND status = 'Available' ORDER BY roomNumber ASC";

        conn = Database.ConnectDB();

        try {
            ObservableList listData = FXCollections.observableArrayList();
            prepar = conn.prepareStatement(availableRoomNumber);
            rs = prepar.executeQuery();

            while (rs.next()) {
                listData.add(rs.getString("roomNumber"));
            }

            roomNumber.setItems(listData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void customerNumber() {
        String customerNum = "SELECT customer_id FROM customer";

        conn = Database.ConnectDB();
        try{

            prepar = conn.prepareStatement(customerNum);
            rs = prepar.executeQuery();

            while (rs.next()) {
                getData.customerNum = rs.getInt("customer_id");
            }
        }catch (Exception e) {

        }
    }

    public void displayCustomerNumber() {
        customerNumber();
        customerNumber.setText(String.valueOf(getData.customerNum + 1));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayCustomerNumber();
        roomTypeList();
        roomNumberList();
    }
}
