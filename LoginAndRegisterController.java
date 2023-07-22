package com.example.controller;

import com.example.database.DbSetEmployee;
import com.example.exceptions.AlertMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginAndRegisterController {

    @FXML
    private AnchorPane registerForm;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignUp;

    @FXML
    private ComboBox<?> cbBoxSelect;

    @FXML
    private CheckBox checkBoxLogin;

    @FXML
    private CheckBox checkBoxRegister;

    @FXML
    private Hyperlink lbLogin;

    @FXML
    private Hyperlink lbRegister;

    @FXML
    private AnchorPane loginForm;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPasswordLogin;

    @FXML
    private PasswordField txtPasswordRegister;

    @FXML
    private TextField txtShowLogin;

    @FXML
    private TextField txtShowRegister;

    @FXML
    private TextField txtUsernameLogin;

    @FXML
    TextField txtUsernameRegister;

    private AlertMessage alert = new AlertMessage();

    public void registerClear(){
        txtEmail.clear();
        txtUsernameRegister.clear();
        txtPasswordRegister.clear();
        txtShowRegister.clear();
    }

    DbSetEmployee e = new DbSetEmployee();
    public void registerAccount() {
        if (txtEmail.getText().isEmpty() || txtUsernameRegister.getText().isEmpty() || txtPasswordRegister.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else {
            e.Register(txtUsernameRegister.getText(), txtEmail.getText(), txtPasswordRegister.getText());
            registerClear();
            loginForm.setVisible(true);
            registerForm.setVisible(false);
        }
    }

    public void registerShowPassword() {
        if (checkBoxRegister.isSelected()) {
            txtShowRegister.setText(txtPasswordRegister.getText());
            txtShowRegister.setVisible(true);
            txtPasswordRegister.setVisible(false);
        } else {
            txtPasswordRegister.setText(txtShowRegister.getText());
            txtShowRegister.setVisible(false);
            txtPasswordRegister.setVisible(true);
        }
    }

    public void loginShowPassword() {
        if (checkBoxLogin.isSelected()) {
            txtShowLogin.setText(txtPasswordLogin.getText());
            txtShowLogin.setVisible(true);
            txtPasswordLogin.setVisible(false);
        } else {
            txtPasswordLogin.setText(txtShowLogin.getText());
            txtShowLogin.setVisible(false);
            txtPasswordLogin.setVisible(true);
        }
    }


    public void loginAccount() {
        if (txtUsernameLogin.getText().isEmpty() || txtPasswordLogin.getText().isEmpty()) {
            alert.errorMessage("Incorrect Username/Password");
        } else {
            if (checkBoxLogin.isSelected()) {
                txtPasswordLogin.setText(txtShowLogin.getText());
            }
            e.Login(txtUsernameLogin.getText(), txtPasswordLogin.getText());
        }
    }

    @FXML
    private void switchForm(ActionEvent event) {
        if (event.getSource() == lbRegister) {
            loginForm.setVisible(false);
            registerForm.setVisible(true);
        } else if (event.getSource() == lbLogin) {
            loginForm.setVisible(true);
            registerForm.setVisible(false);
        }
    }
}
