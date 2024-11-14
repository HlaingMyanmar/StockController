package org.usersoptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.Alerts.AlertBox;
import org.container.Users;
import org.controllers.ApplicationViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label forgotlb;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ComboBox<String> nameList;

    @FXML
    private PasswordField passwordpwd;

    @FXML
    private Button submitbtn;

    public static String _level =null;

    public static String _role =null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ini();

        getAction();



    }

    private void getAction() {


        submitbtn.setOnAction(_ -> {


            getLogin();

        });

        mainPane.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.ENTER){

                getLogin();


            }

        });

    }

    private void ini() {

        ObservableList<String> list = FXCollections.observableArrayList();

        for (Users user : Users.values()) {
            list.add(user.getUsername());
        }

        nameList.setItems(list);



    }
    private void getLogin() {

        int i = 0;

        try {


            for (Users user : Users.values()) {


                if (nameList.getValue().equals(user.getUsername()) && passwordpwd.getText().equals(user.getPassword())) {

                    _level = user.getUsername();





                    i =1;


                    AlertBox.showInformation("အကောင့်ဝင်ခြင်း", "‌အောင်မြင်ပါသည်။");

                    Stage stage = new Stage();


                    FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/applicationview.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    stage.initStyle(StageStyle.UTILITY);
                    stage.initModality(Modality.WINDOW_MODAL);
                    Stage mainStage = (Stage) submitbtn.getScene().getWindow();
                    stage.setTitle("ပင်မ စာမျက်နှာ");
                    stage.setScene(scene);
                    mainStage.close();
                    stage.show();






                }


            }

        } catch (NullPointerException _) {


            AlertBox.showError("အကောင့်ဝင်ခြင်း", "ကျေးဇူးပြု၍ နာမည်ရွေးပါ။");

        }


        if (passwordpwd.getText().isEmpty()) {

            AlertBox.showError("အကောင့်ဝင်ခြင်း", "သင့်၏ စကားဝှက်ဖြည့်ပါ။");


        } else if (i == 0) {

            AlertBox.showError("အကောင့်ဝင်ခြင်း", "သင့်၏ စကားဝှက်မှားယွင်းနေပါသည်။");


        }

    }

    }
