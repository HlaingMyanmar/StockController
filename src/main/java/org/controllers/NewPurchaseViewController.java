package org.controllers;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.Alerts.AlertBox;
import org.databases.Purchasedb;
import org.databases.PurchasehasStockdb;
import org.databases.Stockdb;
import org.datalistgenerator.StockGenerate;
import org.datalistgenerator.SupplierGenerate;
import org.models.Purchase;
import org.models.PurchasehasStock;
import org.models.Stock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.Generator.Deliver;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.Generator.Currency.convertToMyanmarCurrency;
import static org.controllers.CategorController._cacode;
import static org.controllers.CategorController._status;
import static org.controllers.StockViewController._stockcode;

public class NewPurchaseViewController extends Deliver  implements Initializable {


    @FXML
    private TableColumn<Stock, String> brandCol;

    @FXML
    private TableColumn<Stock, String> categroyCol;

    @FXML
    private TableColumn<Stock, String> codeCol;

    @FXML
    private TableColumn<Stock, String> nameCol;

    @FXML
    private TableColumn<Stock, Integer> priceCol;

    @FXML
    private TableColumn<Stock, Integer> qtyCol;

    @FXML
    private TableColumn<Stock, Integer> totalCol;

    @FXML
    private  ComboBox<String>  stockbrandcobox;

    @FXML
    private TextField stockcategorycobox;

    @FXML
    private TableView purchasetable;



    @FXML
    private TextField datetxt;

    @FXML
    private Button addItem;

    @FXML
    private Button getpricebtn;

    @FXML
    private JFXCheckBox itemnew;



    @FXML
    private Label lbCount;

    @FXML
    private Label lbTotal;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button submitItembtn;

    @FXML
    private TextField purchaseidtxt;

    @FXML
    private Button removeItem;

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
    private TextField stocktotaltxt;

    @FXML
    private TextField supplierid;

    @FXML
    private ComboBox<String> suppliername;

    @FXML
    private AnchorPane switchPane;

    @FXML
    private Button findcategorybtn;

    private static int _counter = 1;

    private static int _stockcounter = 1;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Stockdb stockdao = context.getBean("stockdb", Stockdb.class);

    Purchasedb purchasedb = context.getBean("purchasedb",Purchasedb.class);

    ObservableList<Stock> __predataList = FXCollections.observableArrayList();

    PurchasehasStockdb purchasehasStockdb = context.getBean("purchasehastockdb",PurchasehasStockdb.class);





    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ini();
        getIniPurchaseTable();

        new SupplierGenerate().getSupplierName(suppliername,supplierid);

        stockbrandcobox.setItems(getBrandNameList());


        saveAction();





    }

    @Transactional
    protected void saveAction(){


        addItem.setOnAction(_ -> {

            if(purchaseidtxt.getText()==null || datetxt.getText()==null || stockidtxt.getText() == null || stocknametxt.getText()== null || stockcategorycobox.getText() ==null || stockbrandcobox.getValue() == null || stockqtytxt.getText().isEmpty() || stockpricetxt.getText().isEmpty() ){


                AlertBox.showWarning("သတိ","လိုအပ်သည့် အချက်အလက်များ ဖြည့်သွင်းပါ။");


            }

            else {

                String stocode = stockidtxt.getText();
                String stoname = stocknametxt.getText();
                String stobrand = getBrandCode(stockbrandcobox.getValue());
                int qty = Integer.parseInt(stockqtytxt.getText());
                int price = Integer.parseInt(stockpricetxt.getText());


                boolean isDuplicate = __predataList.stream()
                        .anyMatch(b -> b.getStockcode().equals(stockidtxt.getText()));

                if(isDuplicate){

                    AlertBox.showError("သတိ","သင့်၏ အချက်အလက်သည် ထပ်တူကျနေပါသည်။");

                }

                else {

                    String stocate = stockcategorycobox.getText();

                    Stock stock =new Stock(stocode,stoname,stobrand,stocate,qty,price,qty*price);

                    if(itemnew.isSelected()){

                        Stock stockfor =new Stock(stocode,stoname,stobrand,stocate,0,0);


                        if (stockdao.insert(stockfor) == 1) {

                            AlertBox.showInformation("ပစ္စည်းအသစ်", "ပစ္စည်းအသစ်အား အောင်မြင်စွာဖန်တီးပြီးပါပြီ။");
                            getQtyItem(lbCount);
                            stockidtxt.setText(new StockGenerate().getStockID());
                            getBookClear();



                        }
                        itemnew.setSelected(false);


                    }

                    __predataList.add(stock);
                    purchasetable.setItems(__predataList);
                    getBookClear();

                    getAmountItem(lbTotal);



                }





            }




        });

        removeItem.setOnAction(_ -> {

            try {

                String stockcode = ((Stock) purchasetable.getSelectionModel().getSelectedItem()).getStockcode();
                LocalDate now = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                String currentDate = now.format(formatter);

                int selectedIndex = purchasetable.getSelectionModel().getSelectedIndex();

                try {

                    if (selectedIndex >= 0 && stockdao.delete(new Stock(stockcode)) == 1 && currentDate.equals(stockcode.substring(3, 11))) {

                        purchasetable.getItems().remove(selectedIndex);

                        AlertBox.showInformation("အောင်မြင်ပါသည်။", "သင့်၏ အချက်အလက် ဖျက်ပြီးပါပြီ။");

                        getAmountItem(lbTotal);
                        getQtyItem(lbCount);

                    } else {

                        AlertBox.showWarning("Notice", "သင့်သည် ပစ္စည်းအား မှတ်သားထားခြင်းမရှိပါ။");
                    }

                }catch (DataIntegrityViolationException e){

                    purchasetable.getItems().remove(selectedIndex);

                }

            }catch (NullPointerException e){

                AlertBox.showError("သတိမူရန်","သင်ဖျက်လို့ ပစ္စည်းအား မှတ်သားပါ။");

            }





        });

        submitItembtn.setOnAction(_ -> {


            if(purchaseidtxt.getText()==null || datetxt.getText()==null || supplierid.getText()==null || __predataList.isEmpty()){


                AlertBox.showWarning("ကျေးဇူးပြု၍စစ်ဆေးပေးပါ။","လိုအပ်သည့်အချက်အလက်များ ဖြည့်သွင်းပေးပါ။");


            }


            else {

                String code = purchaseidtxt.getText();
                Date text = Date.valueOf(datetxt.getText());
                String suppliercode = supplierid.getText();


                if(purchasedb.insert(new Purchase(code, text))==1){

                    List<PurchasehasStock> purchasehasStockList = new ArrayList<>();

                    for (Stock stock : __predataList) {

                        purchasehasStockList.add( new PurchasehasStock(code,suppliercode,stock.getStockcode(),stock.getQty(),stock.getPrice()));


                    }


                    int check=1;
                    int [] result=purchasehasStockdb.insertBatch(purchasehasStockList);

                    for(int i :result){

                       if(i!=1 ){



                          check =0;
                          break;

                       }



                    }


                    if( check==1 ){


                        AlertBox.showInformation("Successful","New Purchase Successfull Insert!!!");

                        for(PurchasehasStock purchasehasStock :purchasehasStockList){


                            stockdao.sumQty(new Stock(purchasehasStock.getStockcode(),purchasehasStock.getQty(),purchasehasStock.getOrg_price()));


                        }
                        Stage mainStage= (Stage) lbCount.getScene().getWindow();

                        mainStage.close();

                    }

                }


            }


        });

    }

    private void ini() {

        getActionEvent();

        purchaseidtxt.setText(getPurchaseID());

        datetxt.setText(String.valueOf(LocalDate.now()));

        itemnew.setOnAction(_ -> {

            if(itemnew.isSelected()){


                stockidtxt.setText(new StockGenerate().getStockID());



            }


        });

        findcategorybtn.setOnAction(_ -> {

            Stage stage = new Stage();

            _status = true;



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/categoryview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) findcategorybtn.getScene().getWindow();
            stage.setTitle("Category");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();




        });

        stockcategorycobox.setOnMouseClicked(_ -> {
            stockcategorycobox.setText(_cacode);
        });




    }
    private void getIniPurchaseTable(){

        tableIniti(codeCol, nameCol, categroyCol, brandCol, qtyCol, priceCol, totalCol);

    }


    private void getAmountItem(Label label){


        int sum = __predataList.stream()
                .mapToInt(Stock::getTotal)
                .sum();

        label.setText(convertToMyanmarCurrency(sum));

    }

    private void getQtyItem(Label label){

        label.setText(String.valueOf(__predataList.size()));

    }

    static void tableIniti(TableColumn<Stock, String> codeCol, TableColumn<Stock, String> nameCol, TableColumn<Stock, String> categroyCol, TableColumn<Stock, String> brandCol, TableColumn<Stock, Integer> qtyCol, TableColumn<Stock, Integer> priceCol, TableColumn<Stock, Integer> totalCol) {

        codeCol.setCellValueFactory(new PropertyValueFactory<>("stockcode"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("stockname"));
        categroyCol.setCellValueFactory(new PropertyValueFactory<>("ccode"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("bid"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private  void getBookClear(){

        stockidtxt.setText("");
        stocknametxt.setText("");
        stockqtytxt.setText("");
        stockpricetxt.setText("");
        stockbrandcobox.setValue("");
        stockcategorycobox.setText("");
        stocktotaltxt.setText("");


    }

    private String getPurchaseID(){

        String id= (purchasedb.getAllList().isEmpty())?null: (purchasedb.getAllList().getFirst().getPuid());

        return  getID("#P", id);

    }




    public void getActionEvent(){





        mainPane.setOnKeyPressed(event -> {




            if(event.getCode() == KeyCode.F1){

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
                Stage mainStage = (Stage) findcategorybtn.getScene().getWindow();
                stage.setTitle("Category");
                stage.initOwner(mainStage);
                stage.setScene(scene);
                stage.show();




            }
        });

        stockidtxt.setOnMouseClicked(_ -> {



            stockidtxt.setText(_stockcode);

            Stock stock = getStockList(_stockcode);

            try {

                stocknametxt.setText(stock.getStockname());
                stockcategorycobox.setText(stock.getCcode());
                stockbrandcobox.setValue(stock.getBid());
                stockpricetxt.setText(String.valueOf(stock.getPrice()));

            }catch (NullPointerException e){

                AlertBox.showWarning("ရွေးချယ်ပါ။", "ပစ္စည်းအမျိးအမည် (သို့) ပစ္စည်းအိုင်ဒီအသစ်ထည့်ပါ။");
            }









        });

        stockqtytxt.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.TAB){

                if(stockpricetxt.getText().isEmpty()){

                    stockpricetxt.setText("0");
                    stockpricetxt.selectAll();

                }
                else {

                    stocktotaltxt.setText(String.valueOf(Integer.parseInt(stockpricetxt.getText()) *Integer.parseInt( stockqtytxt.getText())));

                }


            }


        });



        keyRelease(stockqtytxt);

        keyRelease(stockpricetxt);


    }

    private void keyRelease(TextField stockpricetxt) {

        stockpricetxt.setOnKeyReleased(_ -> {

            try {


                int qty = stockqtytxt.getText()==null?0: Integer.parseInt(stockqtytxt.getText());
                int price = stockpricetxt.getText()==null?0:Integer.parseInt(stockpricetxt.getText());

                stocktotaltxt.setText(String.valueOf(qty * price));

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
}
