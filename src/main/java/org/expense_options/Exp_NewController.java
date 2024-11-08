package org.expense_options;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.Alerts.AlertBox;
import org.databases.Stockdb;
import org.expense_services.ExpenseServices;
import org.models.Brand;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.Generator.IDGenerate.getID;
import static org.Generator.IDGenerate.getStockIDGenerate;

public class Exp_NewController implements Initializable {

    @FXML
    private TextField amount;

    @FXML
    private TextField date;

    @FXML
    private TextArea desc;

    @FXML
    private JFXComboBox<String> exp_cat;

    @FXML
    private TextField expcode;

    @FXML
    private Button newExpbtn;

    @FXML
    private JFXCheckBox wavechebox;

    @FXML
    private JFXCheckBox cashchebox;

    @FXML
    private JFXCheckBox kbzchebox;


    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Exp_Viewdb expViewdb = context.getBean("exp_viewdb", Exp_Viewdb.class);

    Exp_Typesdb expTypedb = context.getBean("exp_typesdb",Exp_Typesdb.class);


    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ini();



    newExpbtn.setOnAction(event -> {

        if(areComboxFieldEmpty(exp_cat) || areAllCheckboxesUnchecked(kbzchebox,wavechebox,cashchebox)){



            AlertBox.showWarning("အသုံးစရိတ်","လိုအပ်သည့် အချက်အလက်များ ဖြည့်သွင်းပါ။");


        }else {



            try {

                int  checkboxid = getPayId(String.valueOf(getFirstSelectedCheckbox(kbzchebox,wavechebox,cashchebox).getText()));
                String code = expcode.getText();
                Date exdate = Date.valueOf(date.getText());
                int  exptype = getExpTypeID(exp_cat.getValue());
                int total = Integer.parseInt(amount.getText());
                String descri = desc.getText();

                Exp_View expView =new Exp_View(code,exdate,exptype,total,descri);
                Payment payment = new Payment(checkboxid,expView.getTotal());



        if(new ExpenseServices().useExpense(expView, payment)){


            AlertBox.showInformation("အသုံးစရိတ်","အောင်မြင်ပါသည်။");

            ini();

        }




            }catch(NumberFormatException e){


                AlertBox.showWarning("အသုံးစရိတ်","လိုအပ်သည့် အချက်အလက်များ ဖြည့်သွင်းပါ။");


            }

        }








        });



    }

    private void ini() {



        expcode.setText(getExpenseID());
        date.setText(LocalDate.now().toString());
        exp_cat.setItems(getExpTypeList());

        kbzchebox.setOnAction(event -> handleCheckboxSelection(kbzchebox, wavechebox, cashchebox));
        wavechebox.setOnAction(event -> handleCheckboxSelection(wavechebox, kbzchebox, cashchebox));
        cashchebox.setOnAction(event -> handleCheckboxSelection(cashchebox, kbzchebox, wavechebox));

        amount.setText("");
        desc.setText("");
        kbzchebox.setSelected(false);
        cashchebox.setSelected(false);
        wavechebox.setSelected(false);


    }

    private Integer getPayId(String payname) {
        return paymentdb.getAllList().stream()
                .filter(payment->payment.getPaymethodname().equals(payname))
                .map(Payment::getPayid)
                .findFirst()
                .orElse(null);
    }


    private String getExpenseID(){

        String id= (expViewdb.getAllList().isEmpty())?null: (expViewdb.getAllList().getFirst().getExpense_id());

        return  getStockIDGenerate("#Exp", id);

    }

    private ObservableList<String> getExpTypeList(){

        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll(expTypedb.getAllList().stream()
                .map(Exp_Types::getCategory_name)
                .toList());

        return list;

    }

    private int getExpTypeID(String name){

       return expTypedb.getAllList().stream()
                .filter(e ->e.getCategory_name().equals(name))
                .map(Exp_Types::getCategory_id)
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
