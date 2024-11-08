package org.expense_options;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controllers.ApplicationViewController;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Exp_ViewController implements Initializable {

    @FXML
    private TableColumn<Exp_View, Integer> amountCol;


    @FXML
    private Spinner<Integer> dayPicker;

    @FXML
    private Spinner<Integer> monthPicker;

    @FXML
    private Spinner<Integer> yearPicker;

    @FXML
    private LineChart<?, ?> barChart;

    @FXML
    private TableColumn<Exp_View, String> catCol;

    @FXML
    private TableColumn<Exp_View, String> codeCol;

    @FXML
    private TableColumn<Exp_View, Timestamp> createCol;

    @FXML
    private TableColumn<Exp_View, Date> dateCol;

    @FXML
    private TableColumn<Exp_View, Timestamp> updateCol;


    @FXML
    private TableColumn<Exp_View, String> descCol;

    @FXML
    private TableView expensetable;

    @FXML
    private Label lbCount;

    @FXML
    private Label lbTotal;


    @FXML
    private Button newExpbtn;

    @FXML
    private PieChart pieChart;

    @FXML
    private TextField searchtxt;


    @Override
    public void initialize(URL location, ResourceBundle resources) {




        ini();
        actionEvent();



    }

    private void actionEvent() {



        newExpbtn.setOnAction(event -> {


            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/exp_newExp.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) expensetable.getScene().getWindow();
            stage.setTitle("အသုံးစရိတ်");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();


            stage.setOnCloseRequest(event1 -> {


                ini();

            });




        });




    }

    private void ini() {

        int day = LocalDate.now().getDayOfMonth();
        dayPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, day));

        int year = LocalDate.now().getYear();
        yearPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, year));

        int month = LocalDate.now().getMonthValue();
        monthPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        monthPicker.getValueFactory().setValue(month);


    }




}
