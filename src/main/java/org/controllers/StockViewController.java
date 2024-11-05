package org.controllers;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.Alerts.AlertBox;
import org.databases.Stockdb;
import org.datalistgenerator.StockGenerate;
import org.models.Stock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


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

                stock.getStockcode();

                class GUI extends Application {


                    @Override
                    public void start(Stage stage) throws Exception {

                        TextField stockprice = new TextField();
                        stockprice.setPromptText("ဈေးနှုန်းသတ်မှတ်ပါ။");

                        Button button = new Button("သေချာသည်။");

                        VBox box = new VBox();
                        box.getChildren().addAll(stockprice,button);
                        Insets insets = new Insets(20, 10, 20, 10);
                        box.setPadding(insets);
                        box.setSpacing(20);

                        Scene scene = new Scene(box,250,100);

                        stage.setScene(scene);

                        stage.setTitle("ဈေးနှုန်းသတ်မှတ်ခြင်း");
                        stage.show();

                        button.setOnAction(event -> {


                            try {

                                int price = Integer.parseInt(stockprice.getText());

                                if(dao.setPrice(new Stock(stock.getStockcode(),price))==1){

                                    AlertBox.showInformation("ပစ္စည်းစာရင်းများ", "ဈေးနှုန်းသတ်မှတ်ခြင်း အောင်မြင်သည်။");

                                }

                            }catch (NumberFormatException e ){

                                AlertBox.showError("ပစ္စည်းစာရင်းများ", "ဈေးနှုန်းသတ်မှတ်ပါ။");


                            }




                        });



                        stage.setOnCloseRequest(event1 -> {



                            ini();


                        });


                    }

                }


                GUI gui = new GUI();
                try {


                    gui.start(new Stage());


                } catch (Exception e) {


                    throw new RuntimeException(e);
                }


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
