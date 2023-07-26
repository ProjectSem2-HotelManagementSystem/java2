package com.example.projectsem2;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.events.MouseEvent;

public class AdminController implements Initializable {
    @FXML
    private Button availableRoom_addBtn;

    @FXML
    private Button availableRoom_btn;

    @FXML
    private Button availableRoom_checkinBtn;

    @FXML
    private Button availableRoom_clearBtn;
    @FXML
    private TableView<RoomData> availableRoom_tableView;

    @FXML
    private TableColumn<RoomData, String> availableRoom_col_price;

    @FXML
    private TableColumn<RoomData, String> availableRoom_col_roomNumber;

    @FXML
    private TableColumn<RoomData, String> availableRoom_col_roomType;

    @FXML
    private TableColumn<RoomData, String> availableRoom_col_status;

    @FXML
    private Button availableRoom_deleteBtn;

    @FXML
    private AnchorPane availableRoom_form;

    @FXML
    private TextField availableRoom_price;

    @FXML
    private TextField availableRoom_roomNumber;

    @FXML
    private ComboBox<?> availableRoom_roomType;

    @FXML
    private TextField availableRoom_search;

    @FXML
    private ComboBox<?> availableRoom_status;

    @FXML
    private Button availableRoom_updateBtn;

    @FXML
    private Label current_form;

    @FXML
    private Button customers_btn;

    @FXML
    private TableColumn<customerData, String> customers_checkIn;

    @FXML
    private TableColumn<customerData, String> customers_checkOut;

    @FXML
    private TableColumn<customerData, String> customers_customerNumber;

    @FXML
    private TableColumn<customerData, String> customers_firstName;

    @FXML
    private AnchorPane customers_form;

    @FXML
    private TableColumn<customerData, String> customers_lastName;

    @FXML
    private TableColumn<customerData, String> customers_phoneNumber;

    @FXML
    private TextField customers_search;

    @FXML
    private TableView<customerData> customers_tableView;

    @FXML
    private TableColumn<?, ?> customers_totalPayment;

    @FXML
    private AnchorPane dashboard_TB;

    @FXML
    private Button dashboard_btn;

    @FXML
    private BarChart<String, Integer> dashboard_chart_BD;

    @FXML
    private AreaChart<?, ?> dashboard_chart_ID;

    @FXML
    private TableColumn<?, ?> dashboard_col_ID;

    @FXML
    private TableColumn<?, ?> dashboard_col_name;

    @FXML
    private TableColumn<?, ?> dashboard_col_status;

    @FXML
    private TableColumn<?, ?> dashboard_col_working;

    @FXML
    private Label dashboard_dayIncome;

    @FXML
    private Label dashboard_dayBook;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private TableView<?> dashboard_tableView;


    @FXML
    private Label date_time;

    @FXML
    private Label nav_adminID;

    @FXML
    private Label nav_username;

    @FXML
    private Button profileSetting_btn;

    @FXML
    private Circle top_profile;

    @FXML
    private Label top_username;

    private Connection conn;
    private PreparedStatement prepar;
    private Statement stmt;
    private ResultSet result;

    private int count = 0;

    public void dashboardIncomeChart() {
        dashboard_chart_ID.getData().clear();

        String sql =
                "SELECT customer_date, SUM(total) AS total FROM customer_receipt GROUP BY customer_date ORDER BY customer_date ASC LIMIT 8";
        conn = Database.ConnectDB();

        XYChart.Series chart = new XYChart.Series<>();
        chart.setName(null);
        try{
            prepar = conn.prepareStatement(sql);
            result = prepar.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString("customer_date"), result.getInt("total")));
            }
            dashboard_chart_ID.getData().add(chart);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void dashboardCountBookToday() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT COUNT(*) AS id FROM customer WHERE checkIn = '"+sqlDate+"'";

        conn = Database.ConnectDB();

        count = 0;

        try{

            prepar = conn.prepareStatement(sql);
            result = prepar.executeQuery();

            while(result.next()){
                count = result.getInt("id");
                dashboard_dayBook.setText(String.valueOf(count));
            }
            System.out.println(count);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayCountBookToday() {
        dashboardCountBookToday();
    }

    private int sumToday = 0;

    public void dashboardSumIncomeToday() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT SUM(total) AS total FROM customer WHERE checkIn ='"+sqlDate+"'";

        conn = Database.ConnectDB();


        try {
            prepar = conn.prepareStatement(sql);
            result = prepar.executeQuery();
            while (result.next()) {
                sumToday = result.getInt("total");
                dashboard_dayIncome.setText(String.valueOf(sumToday+" VND"));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displaySumIncomeToday() {
        dashboardSumIncomeToday();
    }

    private int overall;

    public void dashboardSumTotalIncome() {
        String sql = "SELECT SUM(total) AS total FROM customer_receipt";

        conn = Database.ConnectDB();

        try{
            prepar = conn.prepareStatement(sql);
            result = prepar.executeQuery();

            while (result.next()) {
                overall = result.getInt("total");
                dashboard_totalIncome.setText(String.valueOf(overall + " VND"));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displaySumTotalIncome() {
        dashboardSumTotalIncome();
    }

    public void switchForm(ActionEvent event) {
        if(event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            availableRoom_form.setVisible(false);
            customers_form.setVisible(false);

            displayCountBookToday();
            displaySumIncomeToday();
            displaySumTotalIncome();
            dashboardIncomeChart();
        } else if (event.getSource() == availableRoom_btn) {
            dashboard_form.setVisible(false);
            availableRoom_form.setVisible(true);
            customers_form.setVisible(false);

            availableRoomShowData();
            availableRoomSearch();
        } else if (event.getSource() == customers_btn) {
            dashboard_form.setVisible(false);
            availableRoom_form.setVisible(false);
            customers_form.setVisible(true);

            customerShowData();
            searchCustomer();
        }

    }

    public ObservableList<RoomData> availableRoomListData() {
        ObservableList<RoomData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM room";

        conn = Database.ConnectDB();

        try {

            RoomData roomD;
            prepar = conn.prepareStatement(sql);
            result = prepar.executeQuery();
            while(result.next()) {
                roomD = new RoomData(result.getString("roomNumber")
                        , result.getString("roomType")
                        ,result.getString("status")
                        ,result.getInt("price"));
                listData.add(roomD);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<RoomData> roomDataList;
    public void availableRoomShowData() {
        roomDataList = availableRoomListData();

        availableRoom_col_roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        availableRoom_col_roomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        availableRoom_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        availableRoom_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        availableRoom_tableView.setItems(roomDataList);
    }
// SEARCH THE DATA ON TABLEVIEW AVAILABLE ROOM FROM
    public void availableRoomSearch() {
        FilteredList<RoomData> filter = new FilteredList<>(roomDataList, e -> true);

        availableRoom_search.textProperty().addListener((Observable,oldValue,newValue) -> {
            filter.setPredicate(predicateRoom -> {
                if (newValue == null && newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if (predicateRoom.getRoomNumber().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateRoom.getRoomType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateRoom.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateRoom.getPrice().toString().contains(searchKey)) {
                    return true;
                }else {
                    return false;
                }
            });
        });

        SortedList<RoomData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(availableRoom_tableView.comparatorProperty());
        availableRoom_tableView.setItems(sortList);
    }

//    Available Room
    public void dashboardAdd() {
        String sql = "INSERT INTO room"
                + "(roomNumber, roomType, status, price)"
                + " VALUES (?,?,?,?::integer)";

        conn = Database.ConnectDB();

        Alert alert;

        try {
            String roomNumber = availableRoom_roomNumber.getText();
            String roomType = (String)availableRoom_roomType.getSelectionModel().getSelectedItem();
            String status = (String)availableRoom_status.getSelectionModel().getSelectedItem();
            String price = availableRoom_price.getText();

//            CHECK IF FIELDS EMPTY
            if (roomNumber == null || roomType == null || status == null || price == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else {
                String check = "SELECT roomNumber FROM room WHERE roomNumber = '"+roomNumber+"' ";

                prepar = conn.prepareStatement(check);
                result = prepar.executeQuery();
//              CHECK IF THE ROOM NUMBER HAS THE SAME TO THE PREVIOUS ROOM NUMBER
                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Room # " +roomNumber+ " was already exist!" );
                    alert.showAndWait();
                }else {

                    prepar = conn.prepareStatement(sql);
                    prepar.setString(1, roomNumber);
                    prepar.setString(2, roomType);
                    prepar.setString(3, status);
                    prepar.setString(4, price);

                    prepar.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    availableRoomShowData();
                    availableRoomClear();}
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void availableRoomClear() {
        availableRoom_roomNumber.setText("");
        availableRoom_roomType.getSelectionModel().clearSelection();
        availableRoom_status.getSelectionModel().clearSelection();
        availableRoom_price.setText("");
    }

    public void availableRoomUpdate() {
        String type1 = (String) availableRoom_roomType.getSelectionModel().getSelectedItem();
        String status1 = (String) availableRoom_status.getSelectionModel().getSelectedItem();
        String price1 = availableRoom_price.getText();
        String roomNum = availableRoom_roomNumber.getText();

        String sql = "UPDATE room SET roomType = '"
                +type1+"', status= '"+status1+"' , price = '"+price1
                +"' WHERE roomNumber = '"+roomNum+"'";

        conn = Database.ConnectDB();

        try{
            Alert alert;
            //CHECK IF EMPTY THE FIELDS
            if (type1 == null || status1 == null || price1 == null || roomNum == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the data first!");
                alert.showAndWait();
            }else {
                prepar = conn.prepareStatement(sql);
                prepar.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();
                //TO SHOW THE UPDATED TABLEVIEW
                availableRoomShowData();
                //CLEAR FIELDS
                availableRoomClear();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableRoomDelete() {
        String type1 = (String) availableRoom_roomType.getSelectionModel().getSelectedItem();
        String status1 = (String) availableRoom_status.getSelectionModel().getSelectedItem();
        String price1 = availableRoom_price.getText();
        String roomNum = availableRoom_roomNumber.getText();

        String deleteData = "DELETE FROM room WHERE roomNumber = '"+roomNum+"'";

        conn = Database.ConnectDB();

        try{

            Alert alert;
            if (type1 == null || status1 == null|| price1 == null|| roomNum == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the data first");
                alert.showAndWait();
            }else {

                stmt = conn.createStatement();
                stmt.executeUpdate(deleteData);

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete Room# "+roomNum+"?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)) {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(deleteData);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    availableRoomShowData();

                    availableRoomClear();
                }else {
                    return;
                }
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<customerData> customerListData () {
        ObservableList<customerData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer";

        conn = Database.ConnectDB();
        try{
            prepar = conn.prepareStatement(sql);
            result = prepar.executeQuery();

            customerData custD;

            while (result.next()) {
                custD = new customerData(result.getInt("customer_id")
                        ,result.getString("firstName")
                        ,result.getString("lastName")
                        ,result.getString("phoneNumber")
                        ,result.getDate("checkIn")
                        ,result.getDate("checkOut")
                        ,result.getInt("total")
                        );
                listData.add(custD);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<customerData> listCustomerData;

    public void customerShowData() {

        listCustomerData = customerListData();

        customers_customerNumber.setCellValueFactory(new PropertyValueFactory<>("customerNum"));
        customers_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        customers_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        customers_phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customers_checkIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        customers_checkOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        customers_totalPayment.setCellValueFactory(new PropertyValueFactory<>("total"));

        customers_tableView.setItems(listCustomerData);
    }
// SEARCH THE DATA ON TABLEVIEW FOR CUSTOMER
    public void searchCustomer() {

        FilteredList<customerData> filter = new FilteredList<>(listCustomerData, e -> true);

        customers_search.textProperty().addListener((Observable,oldValue,newValue) -> {

            filter.setPredicate(predicateCustomer ->{
                if (newValue == null && newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateCustomer.getCustomerNum().toString().contains(searchKey)) {
                    return true;
                }else if (predicateCustomer.getFirstName().toLowerCase().contains(searchKey)){
                    return true;
                } else if (predicateCustomer.getLastName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getPhoneNumber().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getCheckIn().toString().contains(searchKey)){
                    return true;
                } else if (predicateCustomer.getCheckOut().toString().contains(searchKey)) {
                    return true;
                } else if (predicateCustomer.getTotal().toString().contains(searchKey)) {
                    return true;
                }else {
                    return false;
                }
            });
        });

        SortedList<customerData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(customers_tableView.comparatorProperty());
        customers_tableView.setItems(sortList);
    }

    private String type[] =
            {"Single Room", "Double Room ","Triple Room", "Quad Room", "King Room","Queen Room","Penthouse Room"};
    public void availableRoomType() {
        List<String> listData = new ArrayList<>();

        for (String data: type) {
            listData.add(data);
        }
        ObservableList list = FXCollections.observableArrayList(listData);
        availableRoom_roomType.setItems(list);
    }

    private String status[] = {"Available", "Not Available", "Occupied"};

    public void availableRoomStatus() {
        List<String> listData = new ArrayList<>();

        for (String data: status) {
            listData.add(data);
        }
        ObservableList list = FXCollections.observableArrayList(listData);
        availableRoom_status.setItems(list);
    }


    public void availableRoomSelectData() {
        RoomData roomD = availableRoom_tableView.getSelectionModel().getSelectedItem();

        int num = availableRoom_tableView.getSelectionModel().getSelectedIndex();

        if((num -1 ) < -1) {
            return;
        }

        availableRoom_roomNumber.setText(String.valueOf(roomD.getRoomNumber()));
        availableRoom_price.setText(String.valueOf(roomD.getPrice()));

    }

    public void availableRoomCheckIn() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("checkIn-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);


            stage.resizableProperty().setValue(false);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void runTime() {
        new Thread() {
            public void run() {
                SimpleDateFormat format = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss a");

                while(true) {
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        date_time.setText(format.format(new Date()));
                    });
                }
            }
        }.start();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availableRoomType();
        availableRoomStatus();
        displayCountBookToday();
        displaySumIncomeToday();
        displaySumTotalIncome();
        dashboardIncomeChart();
        runTime();
//        TO SHOW THE DATA ON TABLEVIEW
        availableRoomShowData();
//        TO SHOW THE DATA ON TABLEVIEW
        customerShowData();
        availableRoomSearch();
        searchCustomer();
    }
}
