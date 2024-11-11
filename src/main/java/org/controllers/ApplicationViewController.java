package org.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.Alerts.AlertBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationViewController implements Initializable {

    @FXML
    private Label loginlevel;

    @FXML
    private MenuItem showStockbtn;

    @FXML
    private MenuItem showbrandbtn;

    @FXML
    private MenuItem showcategorybtn;

    @FXML
    private MenuItem showsupplierbtn;


    @FXML
    private MenuItem purchaseListsbtn;

    @FXML
    private MenuItem paymentbtn;

    @FXML
    private MenuItem warrantybtn;

    @FXML
    private MenuItem orderListbtn;


    @FXML
    private MenuItem exptype_btn;


    @FXML
    private MenuItem dailyexpe_btn;


    @FXML
    private MenuItem incometypes;

    @FXML
    private MenuItem dailyincome;



    @FXML
    private AnchorPane switchPane;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ini();

    }

    private void ini() {

        showbrandbtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/brandview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("Brand");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

        });

        showcategorybtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/categoryview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("Category");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

        });

        incometypes.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/income_types.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("ဝင်ငွေ အမျိုးအစားများ");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

        });
        exptype_btn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/exp_types.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("အသုံးစရိတ်");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

        });


        warrantybtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/warrantyview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("Warranty");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();


        });

        paymentbtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/paymentview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("ငွေပေး‌ချေခြင်းပုံစံ");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();



        });

        purchaseListsbtn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/purchasedashboardview.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }




        });

        dailyincome.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/income_daily_view.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }




        });

        orderListbtn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/saledashboardview.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }




        });



        showsupplierbtn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/supplierview.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }

        });

        dailyexpe_btn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/exp_daily_view.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }

        });





        showStockbtn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/stockview.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }

        });

    }
}
