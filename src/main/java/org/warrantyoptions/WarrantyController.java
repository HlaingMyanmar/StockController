package org.warrantyoptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.Alerts.AlertBox;
import org.paymentoptions.Payment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.ResourceBundle;


public class WarrantyController implements Initializable {

    @FXML
    private TableView  warrantytable;

    @FXML
    private TableColumn<Warranty, Integer> codeCol;

    @FXML
    private TextField name;

    @FXML
    private TableColumn<Warranty, String> nameCol;

    @FXML
    private Button savebtn;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Warrantydb warrantydb  = context.getBean("warrantydb",Warrantydb.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ini();
        tableini();
        getLoadData();

        warrantytable.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {


            String value = String.valueOf(event.getNewValue());

            if(null!=value && !value.isEmpty()){



                event.getRowValue().setWdesc(value);
                Warranty warranty = (Warranty) warrantytable.getSelectionModel().getSelectedItem();


                if(warrantydb.update(warranty)==1){
                    getLoadData();

                    AlertBox.showInformation("‌အောင်မြင်သည်", "ပြင်ဆင်ခြင်းအောင်မြင်ပါသည်။");

                }


            }



        });


    }




    private void getLoadData() {

        ObservableList<Warranty> paymentObservableList = FXCollections.observableArrayList();

        paymentObservableList.addAll(warrantydb.getAllList());

        warrantytable.setItems(paymentObservableList);

    }

    private void tableini() {

        codeCol.setCellValueFactory(new PropertyValueFactory<>("wid"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("wdesc"));


    }

    private void ini() {


        savebtn.setOnAction(event -> {


            String wname = name.getText();

            if((warrantydb.insert( new Warranty(wname)))==1){

                getLoadData();

                AlertBox.showInformation("‌အောင်မြင်သည်။","အသစ်ထည့်သွင်းမှု အောင်မြင်သည်။");

                name.setText("");
            }


        });





    }
}
