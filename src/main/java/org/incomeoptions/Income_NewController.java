package org.incomeoptions;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.Alerts.AlertBox;
import org.expense_options.Exp_Types;
import org.expense_options.Exp_Typesdb;
import org.expense_options.Exp_Viewdb;
import org.expense_services.ExpenseServices;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static org.Generator.IDGenerate.getStockIDGenerate;

public class Income_NewController implements Initializable {

    @FXML
    private TextField amount;

    @FXML
    private JFXCheckBox cashchebox;

    @FXML
    private TextField code;

    @FXML
    private TextField date;

    @FXML
    private TextArea desc;

    @FXML
    private JFXCheckBox kbzchebox;

    @FXML
    private Button newExpbtn;

    @FXML
    private JFXComboBox<String> type;

    @FXML
    private JFXCheckBox wavechebox;


    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);

    Income_Viewdb incomeViewdb = context.getBean("income_viewdb",Income_Viewdb.class);

    Incometypedb incometypedb = context.getBean("incomedb",Incometypedb.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ini();
        actionEvent();






    }

    private void actionEvent() {


        newExpbtn.setOnAction(event -> {



            if(areComboxFieldEmpty(type) || areAllCheckboxesUnchecked(kbzchebox,wavechebox,cashchebox)){



                AlertBox.showWarning("ဝင်ငွေ","လိုအပ်သည့် အချက်အလက်များ ဖြည့်သွင်းပါ။");


            }else {


                try{



                    int  checkboxid = getPayId(String.valueOf(getFirstSelectedCheckbox(kbzchebox,wavechebox,cashchebox).getText()));
                    String codee = code.getText();
                    Date incomedate = Date.valueOf(date.getText());
                    int  incometype = getIncomeTypeID(type.getValue());
                    int total = Integer.parseInt(amount.getText());
                    String descri = desc.getText();

                    Income_View incomeView = new Income_View(codee,incomedate,incometype,total,checkboxid,descri);

                    Payment payment = new Payment(checkboxid,incomeView.getAmount());


                    if(new IncomeServices().useIncomeAddPaymentUpdate(incomeView, payment)){


                        AlertBox.showInformation("ဝင်ငွေ","အောင်မြင်ပါသည်။");

                        ini();

                    }


                }catch(NumberFormatException e){


                    AlertBox.showWarning("ဝင်ငွေ","လိုအပ်သည့် အချက်အလက်များ ဖြည့်သွင်းပါ။");


                }




            }



        });



    }

    private void ini() {

        code.setText(getIncomeID());
        date.setText(LocalDate.now().toString());
        type.setItems(getIncomeTypesList());

        kbzchebox.setOnAction(event -> handleCheckboxSelection(kbzchebox, wavechebox, cashchebox));
        wavechebox.setOnAction(event -> handleCheckboxSelection(wavechebox, kbzchebox, cashchebox));
        cashchebox.setOnAction(event -> handleCheckboxSelection(cashchebox, kbzchebox, wavechebox));

        amount.setText("");
        desc.setText("");
        kbzchebox.setSelected(false);
        cashchebox.setSelected(false);
        wavechebox.setSelected(false);


    }

    private String getIncomeID(){

        String id= (incomeViewdb.getAllListID().isEmpty())?null: (incomeViewdb.getAllListID().getFirst().getIncome_id());

        return  getStockIDGenerate("#Inc", id);

    }

    private Integer getPayId(String payname) {
        return paymentdb.getAllList().stream()
                .filter(payment->payment.getPaymethodname().equals(payname))
                .map(Payment::getPayid)
                .findFirst()
                .orElse(null);
    }

    private ObservableList<String> getIncomeTypesList(){

        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll(incometypedb .getAllList().stream()
                .map(Incometype::getCat_name)
                .toList());

        return list;

    }
    private int getIncomeTypeID(String name){

        return incometypedb.getAllList().stream()
                .filter(e ->e.getCat_name().equals(name))
                .map(Incometype::getCat_id)
                .findFirst().orElse(-1);


    }


    private void handleCheckboxSelection(CheckBox selectedCheckbox, CheckBox... otherCheckboxes) {

        if (selectedCheckbox.isSelected()) {
            for (CheckBox checkbox : otherCheckboxes) {
                checkbox.setSelected(false);
            }
        }
    }

    private CheckBox getFirstSelectedCheckbox(CheckBox... checkBoxes) {
        for (CheckBox checkbox : checkBoxes) {
            if (checkbox.isSelected()) {
                return checkbox;
            }
        }
        return null;
    }

    private  boolean areComboxFieldEmpty(ComboBox<String>... fields) {

        boolean hasEmptyFields = false;

        for( ComboBox<String> field : fields) {

            if(field.getValue().isEmpty()){

                field.setStyle("-fx-background-color: red;");
                hasEmptyFields = true;
                return true;

            }



        }
        return false;

    }

    private boolean areAllCheckboxesUnchecked(CheckBox... checkboxes) {
        for (CheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                return false;
            }
        }
        return true;
    }
}
