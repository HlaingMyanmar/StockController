package org.saleorderoptions;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.Alerts.AlertBox;
import org.controllers.ApplicationViewController;
import org.databases.Stockdb;
import org.models.Stock;
import org.orderoptions.Order;
import org.orderoptions.Orderdb;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.saleoptions.Sale;
import org.saleoptions.Saledb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.warrantyoptions.Warranty;
import org.warrantyoptions.Warrantydb;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.controllers.CategorController._status;
import static org.controllers.StockViewController._stockcode;
import static org.orderoptions.OrderDashboardController.__oid;

public class NewItemViewController implements Initializable {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button newItem;

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
    private ComboBox<String> stockwarranty;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Stockdb stockdb = context.getBean("stockdb", Stockdb.class);
    Saledb saledb  = context.getBean("saledb",Saledb.class);
    Warrantydb warrantydb  = context.getBean("warrantydb",Warrantydb.class);
    Orderdb orderdb  = context.getBean("orderdb",Orderdb.class);
    Paymentdb paymentdb  = context.getBean("paymentdb", Paymentdb.class);



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ini();
        actionEvent();



    }

    private void actionEvent() {

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
                stage.setTitle("ပစ္စည်းစာရင်း");
                stage.initOwner(mainStage);
                stage.setScene(scene);
                stage.show();

            }



        });


        stockidtxt.setOnMouseClicked(event -> {

         Stock stock =    stockdb.getAllList().stream()
                         .filter(stock1 -> stock1.getStockcode().equals(_stockcode))
                         .findFirst().orElse(null);


            assert stock != null;

            if (stock.getQty() == 0) {

                AlertBox.showWarning("ပစ္စည်းစာရင်း", "၎င်း ပစ္စည်းပျက်နေပါသည်။");

            }

            else {

                stockidtxt.setText(stock.getStockcode());
                stocknametxt.setText(stock.getStockname());
                stockqtytxt.setText(String.valueOf(stock.getQty()));
                stockpricetxt.setText(String.valueOf(stock.getPrice()));
            }


        });


        newItem.setOnAction(event -> {



           String stockcode =  stockidtxt.getText();
           int stockQty = Integer.parseInt(stockqtytxt.getText());
           int  stockPrice = Integer.parseInt(stockpricetxt.getText());
           int stockDiscount = Integer.parseInt(stockdiscount.getText());
           int stockWarranty = getWarrantyId(stockwarranty.getValue());


           if((saledb.insert(new Sale(stockcode,__oid,stockWarranty,stockQty,stockPrice,stockDiscount)))==1){


               if(stockdb.subQty(new Stock(stockcode,stockQty,stockPrice))==1 &&   paymentdb.sumAmount(new Payment(getPayiD(__oid),(stockQty*stockPrice)-stockDiscount))==1){

                   AlertBox.showInformation("ပစ္စည်းထည့်သွင်းခြင်း","အောင်မြင်ပါသည်။");

                   Stage stage = new Stage();
                   stage.close();

               }


           }


        });







    }

    private void ini() {

        conboxData();



    }
    private Integer getWarrantyId(String warrantyname) {


        return warrantydb.getAllList().stream()
                .filter(warranty -> warranty.getWdesc().equals(warrantyname))
                .map(Warranty::getWid)
                .findFirst()
                .orElse(null);


    }
    private void conboxData() {


        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll( warrantydb.getAllList().stream()
                .map(Warranty::getWdesc)
                .toList());

        stockwarranty.setItems(list);


    }

    private int getPayiD(String id){


      return  orderdb.getAllList().stream()
              .filter(order -> order.getOid().equals(id))
              .mapToInt(Order::getPayid)
              .findFirst().orElse(1);


    }




}
