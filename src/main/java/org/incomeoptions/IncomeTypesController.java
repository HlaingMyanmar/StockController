package org.incomeoptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import org.Alerts.AlertBox;
import org.expense_options.Exp_Types;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.ResourceBundle;

public class IncomeTypesController implements Initializable {

    @FXML
    private TableColumn<Incometype, Integer> codeCol;


    @FXML
    private TableColumn<Incometype, String> descCol;

    @FXML
    private TextField desctxt;

    @FXML
    private TableView  incometypetable;

    @FXML
    private TableColumn<Incometype, String> nameCol;

    @FXML
    private TextArea remarktxt;

    @FXML
    private Button savebtn;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    Incometypedb incometypedb = context.getBean("incomedb",Incometypedb.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ini();
        tableini();
        actionEvent();

    }

    private void actionEvent() {

        incometypetable.setEditable(true);
        getRowUpdate(nameCol);
        getRowUpdate(descCol);

        savebtn.setOnAction(event -> {


           if(incometypedb.insert(new Incometype(desctxt.getText(),remarktxt.getText()))==1){

               AlertBox.showInformation("ဝင်ငွေ","ဝင်ငွေ အမျိုးအစား ထည့်သွင်းပြီးပါပြီ။");

               ini();


           }


        });

        incometypetable.setOnKeyPressed(event -> {


                if(event.getCode()== KeyCode.DELETE){

                      Incometype incometype = (Incometype) incometypetable.getSelectionModel().getSelectedItem();

                      if(incometypedb.getDeleteById(incometype.getCat_id())==1){

                                AlertBox.showInformation("ဝင်ငွေ","အောင်မြင်စွာ ဖျက်ပြီးပါပြီ။");

                                ini();

                      }


                }


        });







    }

    private void tableini() {


        codeCol.setCellValueFactory(new PropertyValueFactory<>("cat_id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("cat_name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));





    }

    private void getRowUpdate(TableColumn<Incometype, String> tableColumn) {
        getableColumn(tableColumn,incometypetable);
    }

    private void getableColumn(TableColumn<Incometype, String> col,TableView<Incometype> table) {

        col.setCellFactory(TextFieldTableCell.forTableColumn());

        col.setOnEditCommit(event -> {

            String value = String.valueOf(event.getNewValue());

            Incometype incometype = event.getRowValue();




            if(col.getText().equals("ဝင်ငွေ ပုံစံ")){

                event.getRowValue().setCat_name(value);

                Incometype update= new Incometype(

                     incometype.getCat_id(),
                     incometype.getCat_name(),
                     incometype.getDescription()

                );

                if(incometypedb.update(update)==1){


                    AlertBox.showInformation("ဝင်ငွေ","ဝင်ငွေ ပုံစံ အောင်မြင်စွာပြုပြီးပါပြီ။");
                    ini();


                }


            }else if (col.getText().equals("အချက်အလက်")){


                event.getRowValue().setDescription(value);

                Incometype update= new Incometype(

                        incometype.getCat_id(),
                        incometype.getCat_name(),
                        incometype.getDescription()

                );
                if(incometypedb.update(update)==1){


                    AlertBox.showInformation("ဝင်ငွေ","ဝင်ငွေ အချက်အလက် အောင်မြင်စွာပြုပြီးပါပြီ။");
                    ini();


                }



            }




        });

    }


    private void ini() {

        getLoadData(incometypetable);

        desctxt.setText("");
        remarktxt.setText("");



    }

    private void getLoadData(TableView tableView){


        tableView.getItems().setAll(incometypedb.getAllList());

    }
}
