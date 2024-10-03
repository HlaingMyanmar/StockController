package org.paymentoptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.Alerts.AlertBox;
import org.DAO.DataAccessObject;
import org.databases.Branddb;
import org.models.Brand;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.Generator.Currency.convertToMyanmarCurrency;

public class PaymentviewController implements Initializable {


    @FXML
    private TextField accountnotxt;

    @FXML
    private TextField codetxt;

    @FXML
    private TableColumn<Payment, String> acCol;

    @FXML
    private TableColumn<Payment, Integer> paycodeCol;

    @FXML
    private TableView  paymenttable;

    @FXML
    private TableColumn<Payment, String> paymethodCol;

    @FXML
    private TableColumn<Payment, Integer> totalCol;

    @FXML
    private TableColumn<Payment, String> acnameCol;

    @FXML
    private TableColumn<Payment, Timestamp> createCol;

    @FXML
    private TextField createtxt;

    @FXML
    private TextField methodtxt;

    @FXML
    private Button savebtn;

    @FXML
    private TextField accountnametxt;

    @FXML
    private TextField totaltxt;

    @FXML
    private Label lbtotal;


    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        ini();
        actionEvent();
        tableini();
        
    }

    private void tableini() {

        paycodeCol.setCellValueFactory(new PropertyValueFactory<>("payid"));
        paymethodCol.setCellValueFactory(new PropertyValueFactory<>("paymethodname"));
        acCol.setCellValueFactory(new PropertyValueFactory<>("payaccount"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        createCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        acnameCol.setCellValueFactory(new PropertyValueFactory<>("accountname"));



    }

    private void actionEvent() {

        totaltxt.setText("0");


        savebtn.setOnAction(event -> {




            Payment payment = new Payment(methodtxt.getText(),accountnotxt.getText(),accountnametxt.getText(),Integer.parseInt(totaltxt.getText()));

                if(paymentdb.insert(payment)==1){

                    AlertBox.showInformation("အောင်မြင်သည်။","အကောင့်အသစ် ဖန်တီးခြင်း‌အောင်မြင်ပါသည်။");

                    getLoadData();


                }

                    });



    }

    private void ini() {

        getLoadData();

        paymenttable.setEditable(true);

        acCol.setCellFactory(TextFieldTableCell.forTableColumn());
        acCol.setOnEditCommit(event -> {


            String value = String.valueOf(event.getNewValue());

            if(null!=value && !value.isEmpty()){



                event.getRowValue().setPayaccount(value);
                Payment payment = (Payment) paymenttable.getSelectionModel().getSelectedItem();


                if(paymentdb.update(payment)==1){
                    getLoadData();

                    AlertBox.showInformation("‌အောင်မြင်သည်", "ပြင်ဆင်ခြင်းအောင်မြင်ပါသည်။");

                }


            }



        });
        acnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        acnameCol.setOnEditCommit(event -> {


            String value = String.valueOf(event.getNewValue());

            if(null!=value && !value.isEmpty()){



                event.getRowValue().setAccountname(value);
                Payment payment = (Payment) paymenttable.getSelectionModel().getSelectedItem();


                if(paymentdb.update(payment)==1){

                    getLoadData();

                    AlertBox.showInformation("‌အောင်မြင်သည်", "ပြင်ဆင်ခြင်းအောင်မြင်ပါသည်။");

                }


            }



        });






    }

    private void getLoadData(){


        ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();

        paymentObservableList.addAll(paymentdb.getAllList());

        paymenttable.setItems(paymentObservableList);

        int sum =paymentObservableList.stream()
                .mapToInt(Payment::getTotal)
                .sum();

        lbtotal.setText(convertToMyanmarCurrency(sum));


    }






}
