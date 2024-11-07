package org.expense_options;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.Alerts.AlertBox;
import org.databases.Stockdb;
import org.models.Category;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.ResourceBundle;

public class Exp_TypesController implements Initializable {

    @FXML
    private Button deletebtn;

    @FXML
    private TableColumn<Exp_Types, String> desCol;

    @FXML
    private TextField desctxt;

    @FXML
    private TableView exptypetable;

    @FXML
    private TableColumn<Exp_Types, String> remarkCol;

    @FXML
    private TextArea remarktxt;

    @FXML
    private Button savebtn;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Exp_Typesdb expTypedb = context.getBean("exp_typesdb",Exp_Typesdb.class);


    @Override
    public void initialize(URL location, ResourceBundle resources) {





        ini();
        tableIni();
        actionEvent();



    }

    private void actionEvent() {

        savebtn.setOnAction(event -> {



           String desc =  desctxt.getText();

           String remark=  remarktxt.getText();

           Exp_Types expTypes = new Exp_Types(desc,remark);

           if(expTypedb.insert(expTypes)==1){


               AlertBox.showInformation("အသုံးစရိတ်","အောင်မြင်ပါသည်။");

               desctxt.setText("");
               remarktxt.setText("");

               ini();


           }




        });

        deletebtn.setOnAction(event -> {




          String desc = ((Exp_Types) exptypetable.getSelectionModel().getSelectedItem()).getCategory_name();

          if(expTypedb.getDeleteById(  getExpId(desc))==1){

              AlertBox.showInformation("အသုံးစရိတ်","အောင်မြင်ပါသည်။");

              ini();

          }



        });


        exptypetable.setOnMouseClicked(event -> {



            if(event.getClickCount()==2){

                getRowUpdate(desCol);
                getRowUpdate(remarkCol);


            }


        });


    }



    private void ini() {

        getLoadData();

        exptypetable.setEditable(true);


    }

    private void getLoadData(){


        exptypetable.getItems().setAll(expTypedb.getAllList());



    }

    private int getExpId(String type){


        return  expTypedb.getAllList().stream()
                .filter(expTypes -> expTypes.getCategory_name().equals(type))
                .map(Exp_Types::getCategory_id)
                .findFirst().orElse(-1);


    }

    private void getRowUpdate(TableColumn<Exp_Types, String> tableColumn) {
        getableColumn(tableColumn, exptypetable);
    }


    private void getableColumn(TableColumn<Exp_Types, String> tableColumn, TableView<Exp_Types> exptypetable) {
        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableColumn.setOnEditCommit(event -> {
            String value = String.valueOf(event.getNewValue());
            Exp_Types expTypes = event.getRowValue();

            boolean bo = false;


            if (tableColumn.getText().equals("ကုန်ကျစရိတ် အမျိုးအစား")) {

                expTypes.setCategory_name(value);
                bo = true;

            } else if (tableColumn.getText().equals("ကုန်ကျစရိတ်အချက်အလက်")) {
                expTypes.setDescription(value);
            }


            Exp_Types expTypesUpdate = new Exp_Types(
                    getExpId(expTypes.getCategory_name()),
                    expTypes.getCategory_name(),
                    expTypes.getDescription()
            );

            if (expTypedb.update(expTypesUpdate) == 1 ) {

                if(bo){

                    AlertBox.showInformation("အသုံးစရိတ်", "အမျိုးအစား ပြန်လည်ပြုပြင်ခြင်း အောင်မြင်ပါသည်။");

                }
                else {

                    AlertBox.showInformation("အသုံးစရိတ်", "အချက်အလက် ပြန်လည်ပြုပြင်ခြင်း အောင်မြင်ပါသည်။");

                }

                ini();
            }
            else {
                AlertBox.showError("","jj");
            }

        });
    }



    private void tableIni() {

        desCol.setCellValueFactory(new PropertyValueFactory<>("category_name"));
        remarkCol.setCellValueFactory(new PropertyValueFactory<>("description"));


    }


}
