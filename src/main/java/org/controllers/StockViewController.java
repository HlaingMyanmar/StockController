package org.controllers;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.Alerts.AlertBox;
import org.databases.Stockdb;
import org.datalistgenerator.StockGenerate;
import org.models.Stock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class StockViewController extends StockGenerate implements Initializable {

    @FXML
    private Label lbCount;

    @FXML
    private Label lbTotal;

    @FXML
    private TableColumn<Stock, String> sbrandCol;

    @FXML
    private TableColumn<Stock, String> scategoryCol;

    @FXML
    private TableColumn<Stock, String> scodeCol;

    @FXML
    private TableColumn<Stock, String> snameCol;

    @FXML
    private TableColumn<Stock, Integer> spriceCol;

    @FXML
    private TableColumn<Stock, Integer> sqtyCol;

    @FXML
    private TableView  stocktable;

    @FXML
    private TableColumn<Stock, Integer> stotalCol;

    @FXML
    private TextField searchCom;

    @FXML
    private TextField searchGroup;

    @FXML
    private TextField searchID;

    @FXML
    private TextField searchItem;

    @FXML
    private TextField searchPrice;

    @FXML
    private TextField searchQty;

    @FXML
    private TextField searchTotal;

    @FXML
    private Button exportpdfbtn;

    @FXML
    private Button setPricebtn;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Stockdb dao = context.getBean("stockdb", Stockdb.class);

    public static String _stockcode = null;




    @FXML
    void tableClickAction(MouseEvent event) {



    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getIniBookTable();

        ini();

        getActionEvent();

    }

    private void ini() {

        ObservableList<Stock> observableList = FXCollections.observableArrayList();


        observableList.addAll(dao.getAllList());

        FilteredList<Stock> filteredData = new FilteredList<>(observableList, b -> true);

        addTextFieldListener(searchCom, filteredData);
        addTextFieldListener(searchGroup, filteredData);
        addTextFieldListener(searchID, filteredData);
        addTextFieldListener(searchItem, filteredData);
        addTextFieldListener(searchPrice, filteredData);
        addTextFieldListener(searchQty, filteredData);
        addTextFieldListener(searchTotal, filteredData);


        SortedList<Stock> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(stocktable.comparatorProperty());

        stocktable.setItems(sortedData);
        updateLabels(filteredData);

        stocktable.setOnMouseClicked(event -> {

            if(event.getClickCount()==2){

                Stock st = (Stock) stocktable.getSelectionModel().getSelectedItem();

                _stockcode = st.getStockcode();
                System.out.println(_stockcode);
                Stage mainStage = (Stage) stocktable.getScene().getWindow();
                mainStage.close();

            }



        });
    }

    private void getIniBookTable() {

        NewPurchaseViewController.tableIniti(scodeCol, snameCol, scategoryCol, sbrandCol, sqtyCol, spriceCol, stotalCol);


    }
    private void addTextFieldListener(TextField textField, FilteredList<Stock> filteredData) {


        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(stock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (stock.getStockcode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (stock.getStockname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (stock.getBid().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (stock.getCcode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(stock.getQty()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(stock.getPrice()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(stock.getTotal()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (textField == searchCom && stock.getCcode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (textField == searchGroup && stock.getBid().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (textField == searchItem && stock.getStockname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (textField == searchPrice && String.valueOf(stock.getPrice()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (textField == searchQty && String.valueOf(stock.getQty()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (textField == searchTotal && String.valueOf(stock.getTotal()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });

            updateLabels(filteredData);
        });


    }

    private void getActionEvent(){

        exportpdfbtn.setOnAction(event -> {

            exportToPDF(dao.getAllList(),new Stage());


        });

        setPricebtn.setOnAction(event -> {

            try {



                Stock stock = (Stock)  stocktable.getSelectionModel().getSelectedItem();

                _stockcode= stock.getStockcode();


                Stage stage = new Stage();



                FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/stockSetPriceview.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.initStyle(StageStyle.UTILITY);
                stage.initModality(Modality.WINDOW_MODAL);
                Stage mainStage = (Stage) stocktable.getScene().getWindow();
                stage.setTitle("Category");
                stage.initOwner(mainStage);
                stage.setScene(scene);
                stage.show();


                stage.setOnCloseRequest(event1 -> {

                    ini();

                });







            }catch (NullPointerException e){


                AlertBox.showWarning("ပစ္စည်းစာရင်းများ", "ပစ္စည်းတစ်ခုခုကို ရွေးချယ်ပါ။");

            }






        });

    }
    private void updateLabels(FilteredList<Stock> filteredData) {
        lbCount.setText(String.valueOf(filteredData.size()));
        double sum = filteredData.stream()
                .mapToDouble(Stock::getTotal)
                .sum();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("my", "MM"));
        lbTotal.setText(convertToMyanmarCurrency(sum));
    }


}
