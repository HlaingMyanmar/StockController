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
    private MenuItem newPurchasebtn;

    @FXML
    private MenuItem purchaseListsbtn;

    @FXML
    private AnchorPane switchPane;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ini();

    }

    private void ini() {

        showbrandbtn.setOnAction(_ -> {

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

        showcategorybtn.setOnAction(_ -> {

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


        newPurchasebtn.setOnAction(_ -> {




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

        showsupplierbtn.setOnAction(_ -> {

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





        showStockbtn.setOnAction(_ -> {

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
