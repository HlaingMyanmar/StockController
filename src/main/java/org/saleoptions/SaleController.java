package org.saleoptions;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.Alerts.AlertBox;
import org.controllers.ApplicationViewController;
import org.databases.Stockdb;
import org.models.Brand;
import org.models.Stock;
import org.orderoptions.Order;
import org.orderoptions.Orderdb;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Text;
import org.warrantyoptions.Warranty;
import org.warrantyoptions.Warrantydb;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.Generator.Currency.convertToMyanmarCurrency;
import static org.Generator.IDGenerate.getID;
import static org.controllers.CategorController._status;
import static org.controllers.StockViewController._stockcode;

public class SaleController implements Initializable {

    @FXML
    private Button addItem;


    @FXML
    private TextField cuid;

    @FXML
    private TextField cuphone;

    @FXML
    private Label lbCount;

    @FXML
    private Label lbTotal;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableColumn<SaleDataList, String> nameCol;

    @FXML
    private TableColumn<SaleDataList, String> codeCol;

    @FXML
    private TableColumn<SaleDataList, Integer> priceCol;

    @FXML
    private TableView saletable;

    @FXML
    private TableColumn<SaleDataList, Integer> qtyCol;

    @FXML
    private TableColumn<SaleDataList, Integer> totalCol;

    @FXML
    private TableColumn<SaleDataList, String> warrantyCol;

    @FXML
    private TableColumn<SaleDataList, Integer> discountCol;


    @FXML
    private TextField ocode;

    @FXML
    private TextField odate;

    @FXML
    private Button removeItem;

    @FXML
    private TextField stockdiscount;

    @FXML
    private TextField stockidtxt;

    @FXML
    private TextField stocknametxt;

    @FXML
    private TextField stockpricetxt;

    @FXML
    private TextField stockqtytxt;

    @FXML
    private TextArea stockremarktxt;

    @FXML
    private Label stocktotaltxt;

    @FXML
    private ComboBox<String> stockwarranty;

    @FXML
    private Button submitItembtn;

    @FXML
    private JFXCheckBox cashchebox;

    @FXML
    private JFXCheckBox wavechebox;

    @FXML
    private JFXCheckBox kbzchebox;



    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Stockdb stockdb = context.getBean("stockdb", Stockdb.class);
    Saledb saledb  = context.getBean("saledb",Saledb.class);
    Warrantydb warrantydb  = context.getBean("warrantydb",Warrantydb.class);
    Orderdb orderdb  = context.getBean("orderdb",Orderdb.class);
    Paymentdb paymentdb  = context.getBean("paymentdb", Paymentdb.class);

    ObservableList<SaleDataList> __presaledataList = FXCollections.observableArrayList();




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ini();
        actionEvent();
        tableIni();

        conboxData();

        ocode.setText(getOrderCode());







    }

    private void conboxData() {


        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll( warrantydb.getAllList().stream()
                .map(Warranty::getWdesc)
                .toList());

        stockwarranty.setItems(list);











    }

    private void keyRelease(TextField stockpricetxt) {



        stockqtytxt.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.TAB){

                if(stockpricetxt.getText().isEmpty()){

                    stockpricetxt.setText("0");
                    stockpricetxt.selectAll();

                }
                else {

                    int qty = stockqtytxt.getText()==null?0: Integer.parseInt(stockqtytxt.getText());
                    int price = stockpricetxt.getText()==null?0:Integer.parseInt(stockpricetxt.getText());
                    int discount = Objects.equals(stockdiscount.getText(), "") ?0:Integer.parseInt(stockdiscount.getText());

                    int result = (qty*price)-discount;

                    stocktotaltxt.setText(convertToMyanmarCurrency(result));
                }


            }


        });

        stockpricetxt.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.TAB){

                if(stockdiscount.getText().isEmpty()){

                    stockdiscount.setText("0");
                    stockdiscount.selectAll();

                }
                else {

                    int qty = stockqtytxt.getText()==null?0: Integer.parseInt(stockqtytxt.getText());
                    int price = stockpricetxt.getText()==null?0:Integer.parseInt(stockpricetxt.getText());
                    int discount =stockdiscount.getText()==""?0:Integer.parseInt(stockdiscount.getText());

                    int result = (qty*price)-discount;

                    stocktotaltxt.setText(convertToMyanmarCurrency(result));
                }


            }


        });





        stockpricetxt.setOnKeyReleased(event -> {

            try {

                int discount = Objects.equals(stockdiscount.getText(), "") ?0:Integer.parseInt(stockdiscount.getText());


                int qty = stockqtytxt.getText()==null?0: Integer.parseInt(stockqtytxt.getText());
                int price = stockpricetxt.getText()==null?0:Integer.parseInt(stockpricetxt.getText());

                int result = (qty*price)-discount;

                stocktotaltxt.setText(convertToMyanmarCurrency(result));

            }catch (NumberFormatException _e){

                if(stockpricetxt.getText().isEmpty()){

                    stockpricetxt.setText("0");
                    stockpricetxt.selectAll();
                }
                else {

                    AlertBox.showWarning("Not Allowed", "This text is't allowed , Please Check insert data?"+"\n"+"' "+stockpricetxt.getText() +" '"+" That is not number ......");
                    stockpricetxt.setText("");

                }



            }


        });







    }



    private void tableIni() {



        codeCol.setCellValueFactory(new PropertyValueFactory<>("stockcode"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("stockname"));
        warrantyCol.setCellValueFactory(new PropertyValueFactory<>("wdesc"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));



    }

    private void actionEvent() {

        stockidtxt.setOnMouseClicked(event -> {

            stockidtxt.setText(_stockcode);


            stockidtxt.setOnMouseClicked(e -> {

                Stock stock  = (Stock) stockdb.getAllList()
                        .stream()
                        .filter(stock1 -> stock1.getStockcode().equals(_stockcode))
                        .findFirst()
                        .orElse(null);;


                stockidtxt.setText(_stockcode);

                assert stock != null;
                stocknametxt.setText(stock.getStockname());
                stockpricetxt.setText(String.valueOf(stock.getPrice()));
                stockqtytxt.setText(String.valueOf(stock.getQty()));



            });



        });


        mainPane.setOnKeyPressed(event -> {


            if(event.getCode()== KeyCode.F1){

                Stage stage = new Stage();

                _status = true;



                FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/stockview.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.initStyle(StageStyle.UTILITY);
                stage.initModality(Modality.WINDOW_MODAL);
                Stage mainStage = (Stage) mainPane.getScene().getWindow();
                stage.setTitle("Category");
                stage.initOwner(mainStage);
                stage.setScene(scene);
                stage.show();




            }

        });



        addItem.setOnAction(event -> {



            if (areFieldEmpty(odate, ocode, stockidtxt, stocknametxt,stockqtytxt,stockpricetxt,stockdiscount) || areComboxFieldEmpty(stockwarranty) || areAllCheckboxesUnchecked(kbzchebox,wavechebox,cashchebox)) {

                AlertBox.showError("အရောင်း စာမျက်နှာ","လိုအပ်သည့် အချက်အလက်များ ဖြည့်သွင်းပါ။");


            }

            else {


               String stockid = stockidtxt.getText();
               String stockname=  stocknametxt.getText();
               int qty = Integer.parseInt(stockqtytxt.getText());
               int price = Integer.parseInt(stockpricetxt.getText());
               int discount = Integer.parseInt(stockdiscount.getText());
               String warranty =  stockwarranty.getValue();
               String checkboxid = String.valueOf(getFirstSelectedCheckbox(kbzchebox,wavechebox,cashchebox).getText());

                boolean isDuplicate = __presaledataList.stream()
                        .anyMatch(b -> b.getStockcode().equals(stockid));

                if(isDuplicate){

                    AlertBox.showError("အရောင်း စာမျက်နှာ","သင့်ထည့်သွင်းထားသော ပစ္စည်းများသည် ထပ်တူကျနေပါသည်။");
                    getClear();
                }
                else {

                    __presaledataList.add(new SaleDataList(stockid,stockname,warranty,qty,price,discount));

                    getClear();

                    saletable.setItems(__presaledataList);


                }





            }



        });

        removeItem.setOnAction(event -> {


           int indexNum =  saletable.getSelectionModel().getSelectedIndex();

           __presaledataList.remove(indexNum);


        });

        submitItembtn.setOnAction(event -> {

            try {


                if (areFieldEmpty(odate, ocode) || areAllCheckboxesUnchecked(kbzchebox, wavechebox, cashchebox)) {
                    AlertBox.showError("အရောင်း စာမျက်နှာ", "လိုအပ်သည့် အချက်အလက်များ ဖြည့်သွင်းပါ။");
                    return;

                }


                Date orderdate = Date.valueOf(odate.getText());
                String ordercode = ocode.getText();
                String customerid =  cuid.getText().trim();
                String customerphone = cuphone.getText().trim();
                int  checkboxid = getPayId(String.valueOf(getFirstSelectedCheckbox(kbzchebox,wavechebox,cashchebox).getText()));


                if (!isValidPhoneNumber(customerphone)) {
                    AlertBox.showError("အရောင်း စာမျက်နှာ", "ဖောက်သည်အိုင်ဒီ သို့မဟုတ် ဖုန်းနံပါတ် မမှန်ကန်ပါ။");
                    return;

                }



                if(orderdb.insert(new Order(ordercode,orderdate,customerid,customerphone,checkboxid))==1) {


                    ObservableList<Sale> finalSaleList = FXCollections.observableArrayList();

                    for (SaleDataList sa : __presaledataList) {


                        finalSaleList.add(new Sale(sa.getStockcode(), ordercode, getWarrantyId(sa.getWdesc()), sa.getQty(), sa.getPrice(), sa.getDiscount()));


                    }
                    int[] result = saledb.insertBatch(finalSaleList);
                    boolean isSuccess = true;
                    for (int res : result) {
                        if (res != 1) {
                            isSuccess = false;
                            break;
                        }
                    }

                    if (isSuccess) {

                        int check = 0;
                        int checkp =0;


                        for (Sale sale : finalSaleList) {


                            check = stockdb.subQty(new Stock(sale.getStockcode(), sale.getQty(),sale.getPrice()));

                            checkp = paymentdb.sumAmount(new Payment(checkboxid,(sale.getQty()*sale.getPrice())-sale.getDiscount()));


                        }

                        if(check==1 && checkp==1) {

                            AlertBox.showInformation("အရောင်း စာမျက်နှာ", "အောင်မြင်ပါသည်။");
                        }

                        getTextFieldClear(cuid,cuphone,stockidtxt,stocknametxt,stockqtytxt,stockpricetxt,stockdiscount);

                        getComboboxClear(stockwarranty);

                        getCheckClear(kbzchebox,wavechebox,cashchebox);

                        ocode.setText(getOrderCode());

                        odate.setText(String.valueOf(LocalDate.now()));

                        saletable.getSelectionModel().clearSelection();

                        __presaledataList.clear();
                        finalSaleList.clear();


                    }


                }

            } catch (Exception e) {

                e.printStackTrace();
                AlertBox.showError("အရောင်း စာမျက်နှာ", "အမှားတစ်ခုဖြစ်ပွားခဲ့သည်။");
            }


        });



    }

    private boolean isValidCustomerId(String customerId) {

        return customerId != null && customerId.matches("[A-Za-z0-9]+");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {

        return phoneNumber != null && phoneNumber.matches("\\d{7,15}");
    }


    private void getTextFieldClear( TextField... textFields) {

        for (TextField textField : textFields) {

            textField.clear();

        }


    }

    private void getComboboxClear( ComboBox<String> ... comboBoxes) {

        for (ComboBox<String> comboBox : comboBoxes) {
            comboBox.getSelectionModel().clearSelection();
            comboBox.setValue(null);
        }

    }

    private void getCheckClear(CheckBox... checkBoxes){

        for (CheckBox checkBox : checkBoxes) {
            checkBox.setSelected(false);
        }

    }


    private Integer getPayId(String payname) {
        return paymentdb.getAllList().stream()
                .filter(payment->payment.getPaymethodname().equals(payname))
                .map(Payment::getPayid)
                .findFirst()
                .orElse(null);
    }

    private Integer getWarrantyId(String warrantyname) {


        return warrantydb.getAllList().stream()
                .filter(warranty -> warranty.getWdesc().equals(warrantyname))
                .map(Warranty::getWid)
                .findFirst()
                .orElse(null);


    }


    private void getClear(){
        stockidtxt.setText("");
        stocknametxt.setText("");
        stockqtytxt.setText("");
        stockpricetxt.setText("");
        stockdiscount.setText("");
        stockwarranty.setValue("");
    }

    private boolean areFieldEmpty(TextField... fields) {

        boolean hasEmptyFields = false;

        for (TextField field : fields) {
            if (field.getText().isEmpty()) {

                field.setStyle("-fx-background-color: red;"); // Highlight empty field in red
                hasEmptyFields = true;
                return true;
            }
        }
        return false;


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



    private void ini() {



        odate.setText(String.valueOf(LocalDate.now()));

        keyRelease(stockqtytxt);

        keyRelease(stockpricetxt);

        stockdiscount.setOnKeyReleased(event -> {


          try {

              int discount = Objects.equals(stockdiscount.getText(), "") ?0:Integer.parseInt(stockdiscount.getText());


              int qty = stockqtytxt.getText()==null?0: Integer.parseInt(stockqtytxt.getText());
              int price = stockpricetxt.getText()==null?0:Integer.parseInt(stockpricetxt.getText());

              int result = (qty*price)-discount;

              stocktotaltxt.setText(convertToMyanmarCurrency(result));

          }catch (NumberFormatException _e){

              if(stockpricetxt.getText().isEmpty()){

                  stockpricetxt.setText("0");
                  stockpricetxt.selectAll();
              }
              else {

                  AlertBox.showWarning("Not Allowed", "This text is't allowed , Please Check insert data?"+"\n"+"' "+stockpricetxt.getText() +" '"+" That is not number ......");
                  stockpricetxt.setText("");

              }



          }



      });



        kbzchebox.setOnAction(event -> handleCheckboxSelection(kbzchebox, wavechebox, cashchebox));
        wavechebox.setOnAction(event -> handleCheckboxSelection(wavechebox, kbzchebox, cashchebox));
        cashchebox.setOnAction(event -> handleCheckboxSelection(cashchebox, kbzchebox, wavechebox));








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


    private  String getOrderCode(){

        String id= (orderdb.getAllList().isEmpty())?null: (orderdb.getAllList().getFirst().getOid());

        return  getID("#O", id);


    }



}
