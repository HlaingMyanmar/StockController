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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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

    ObservableList<SaleDataList> __presaledataList = FXCollections.observableArrayList();




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ini();
        actionEvent();
        tableIni();

        conboxData();




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


               Date orderdate = Date.valueOf(odate.getText());
               String ordercode = ocode.getText();
               String customerid =  cuid.getText();
               String customerphone = cuphone.getText();
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

                }
                else {

                    __presaledataList.add(new SaleDataList(stockid,stockname,warranty,qty,price,discount));
                    saletable.setItems(__presaledataList);


                }





            }



        });



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



}
