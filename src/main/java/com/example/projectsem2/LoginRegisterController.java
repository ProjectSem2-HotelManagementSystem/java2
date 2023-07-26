package com.example.projectsem2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class LoginRegisterController {

    @FXML
    private PasswordField changePass_Password;

    @FXML
    private Button changePass_backBtn;

    @FXML
    private PasswordField changePass_cPassword;

    @FXML
    private AnchorPane changePass_form;

    @FXML
    private Button changePass_proceedBtn;

    @FXML
    private TextField forgot_answer;

    @FXML
    private Button forgot_backBtn;

    @FXML
    private AnchorPane forgot_form;

    @FXML
    private Button forgot_prooceedBtn;

    @FXML
    private ComboBox<?> forgot_selectQuestion;

    @FXML
    private TextField forgot_username;

    @FXML
    private Button login_btn;

    @FXML
    private Button login_createAccount;

    @FXML
    private Hyperlink login_forgotPassword;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField login_password;

    @FXML
    private CheckBox login_selectShowPassword;

    @FXML
    private TextField login_showPassword;

    @FXML
    private TextField login_username;


    @FXML
    private TextField signup_answer;

    @FXML
    private Button signup_btn;

    @FXML
    private PasswordField signup_cPassword;

    @FXML
    private TextField signup_email;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private Button signup_loginAccount;

    @FXML
    private PasswordField signup_password;

    @FXML
    private ComboBox<?> signup_selectQuestion;

    @FXML
    private TextField signup_username;

    private Connection conn;
    private PreparedStatement prepar;
    private Statement stmt;
    private ResultSet result;


    public void login() {
        AlertMessage alert = new AlertMessage();
        //CHECK IF ALL OR SOME OF FIELDS ARE EMPTY
        if(login_username.getText().isEmpty() || login_password.getText().isEmpty()){
            alert.errorMessage("The Username or Password Is Incorrect");
        }else {
            String selectData = "SELECT username, password FROM users WHERE "
                    + "username = ? and password = ?"; // users IS OUR TABLE NAME

                conn = Database.ConnectDB();
                if (login_selectShowPassword.isSelected()){
                    login_password.setText(login_showPassword.getText());
                }else {
                    login_showPassword.setText(login_password.getText());
                }

            try{
                prepar = conn.prepareStatement(selectData);
                prepar.setString(1, login_username.getText());
                prepar.setString(2,login_password.getText());

                result = prepar.executeQuery();
                if (result.next()){
                     //ONCE ALL DATA THAT USERS INSERT ARE CORRECT, THEN WE WILL PROCEED TO OUR MAIN FORM
                    alert.successMessage("Successfully Login!");

                    Parent root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.show();
                    login_btn.getScene().getWindow().hide();

                } else {
                    alert.errorMessage("The Username or Password Is Incorrect");
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showPassword() {
        if(login_selectShowPassword.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        }else {
            login_password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_password.setVisible(true);
        }
    }
    public void register() {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{3}$";
        AlertMessage alert = new AlertMessage();
        //CHECK IF WE HAVE EMPTY FIELDS
        if(signup_email.getText().isEmpty() || signup_username.getText().isEmpty()
                || signup_password.getText().isEmpty() || signup_cPassword.getText().isEmpty()
        || signup_selectQuestion.getSelectionModel().getSelectedItem() == null || signup_answer.getText().isEmpty()) {
            alert.errorMessage("All fields are necessary to be filled!");
        } else if (!signup_email.getText().matches(emailRegex)) {
            alert.errorMessage("Invalid email address!");
        } else if (signup_password.getText() == signup_cPassword.getText()) {
            //CHECK IF VALUE OF PASSWORD FIELDS IS EQUAL TO CONFIRM PASSWORD
            alert.errorMessage("Password does not match!");
        } else if (signup_password.getText().length() < 8) {
            //CHECK IF THE LENGTH OF PASSWORD VALUE IS LESS THAN TO 8
            //, WE WILL CHECK THE CHARACTERS OF PASSWORD
            alert.errorMessage("Invalid Password, at least 8 characters needed!");
        }else {
            //CHECK IF THE USERNAME IS ALREADY TAKE
//            String checkUsername = "SELECT * FROM users WHERE username = '"
//                + signup_username.getText() + "'";
            String checkData = "SELECT email, username FROM users " + "WHERE email = ? AND username = ?";
                conn = Database.ConnectDB();
            try {
                stmt = conn.prepareStatement(checkData);
                result = stmt.executeQuery(checkData);
//                stmt = conn.createStatement();
//               result = stmt.executeQuery(checkUsername);

                if(result.next()){
                    alert.errorMessage(signup_email.getText() + " is already taken");
                    alert.errorMessage(signup_username.getText() +  " is already taken");
                } else{

                    String insertData = "INSERT INTO users "
                            + "(email, username, password, question, answer)"
                            + "VALUES(?, ?, ?, ?, ?)";
                    prepar = conn.prepareStatement(insertData);
                    prepar.setString(1, signup_email.getText());
                    prepar.setString(2, signup_username.getText());
                    prepar.setString(3,signup_password.getText());
                    prepar.setString(4, (String) signup_selectQuestion.getSelectionModel().getSelectedItem());
                    prepar.setString(5, signup_answer.getText());
                    prepar.executeUpdate();
                    alert.successMessage("Registered Successfully!");
                    registerClearFields();


                    signup_form.setVisible(false);
                    login_form.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void forgotPassword() {
        AlertMessage alert = new AlertMessage();

        if(forgot_username.getText().isEmpty()
                || forgot_selectQuestion.getSelectionModel().getSelectedItem() == null
                || forgot_answer.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields!");

        }else {
            String checkData = "SELECT username, question, answer FROM users "
                    + "WHERE username = ? AND question = ? AND answer = ?";

            conn = Database.ConnectDB();

            try{
                prepar = conn.prepareStatement(checkData);
                prepar.setString(1, forgot_username.getText());
                prepar.setString(2, (String) forgot_selectQuestion.getSelectionModel().getSelectedItem());
                prepar.setString(3,forgot_answer.getText());

               result = prepar.executeQuery();
               //IF CORRECT
               if (result.next()){
                   // PROCEED CHANGE PASSWORD
                   signup_form.setVisible(false);
                   login_form.setVisible(false);
                   forgot_form.setVisible(false);
                   changePass_form.setVisible(true);
               }else {
                    alert.errorMessage("Incorrect Information");
               }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void forgotListQuestion() {
        List<String> listQ = new ArrayList<>();
        for(String data : questionList) {
            listQ.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listQ);
        forgot_selectQuestion.setItems(listData);
    }

    private String[] questionList = {"What is your favorite food?"
            , "What is your favorite color?"
            , "What is the name of your pet?"
            ,"What is your most favorite sport?"};

    public void questions() {
        List<String> listQ = new ArrayList<>();
        for(String data : questionList) {
            listQ.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listQ);
        signup_selectQuestion.setItems(listData);
    }


    public void registerClearFields() {
        signup_email.setText("");
        signup_username.setText("");
        signup_password.setText("");
        signup_cPassword.setText("");
        signup_selectQuestion.getSelectionModel().clearSelection();
        signup_answer.setText("");
    }

    public void changePassword() {
        AlertMessage alert = new AlertMessage();
        //CHECK ALL FIELDS IF EMPTY OR NOT
        if(changePass_Password.getText().isEmpty() || changePass_cPassword.getText().isEmpty()){
            alert.errorMessage("Please fill all blank fields!");
        } else if (!changePass_Password.getText().equals(changePass_cPassword.getText())) {
            //CHECK IF THE PASSWORD AND CONFIRMATION ARE NOT MATCH
            alert.errorMessage("Password does not match!");
        }else if(changePass_Password.getText().length() < 8) {
            //CHECK IF THE LENGTH OF PASSWORD VALUE IS LESS THAN TO 8
            alert.errorMessage("Invalid Password, at least 8 characters needed");
        }else {

            String updateData = "UPDATE users SET password = ? "
                    + "WHERE username = '" + forgot_username.getText() + "'";

            conn = Database.ConnectDB();

            try{
                prepar = conn.prepareStatement(updateData);
                prepar.setString(1,changePass_Password.getText());


                prepar.executeUpdate();
                alert.successMessage("Successfully changed Password");

                signup_form.setVisible(false);
                login_form.setVisible(true);
                forgot_form.setVisible(false);
                changePass_form.setVisible(false);

                login_username.setText("");
                login_password.setVisible(true);
                login_password.setText("");
                login_showPassword.setVisible(false);
                login_selectShowPassword.setSelected(false);

                changePass_Password.setText("");
                changePass_cPassword.setText("");

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void switchForm(ActionEvent event){
        // THE REGISTRATION FORM WILL BE VISIBLE
        if (event.getSource() == signup_loginAccount || event.getSource() == forgot_backBtn){
            signup_form.setVisible(false);
            login_form.setVisible(true);
            forgot_form.setVisible(false);
            changePass_form.setVisible(false);
        } else if (event.getSource() == login_createAccount) { // LOGIN FORM WILL BE VISIBLE
            signup_form.setVisible(true);
            login_form.setVisible(false);
            forgot_form.setVisible(false);
            changePass_form.setVisible(false);
        } else if (event.getSource() == login_forgotPassword || event.getSource() == changePass_backBtn) {
            signup_form.setVisible(false);
            login_form.setVisible(false);
            forgot_form.setVisible(true);
            changePass_form.setVisible(false);

            forgotListQuestion();
        }

    }
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        questions();

        forgotListQuestion();
    }
}