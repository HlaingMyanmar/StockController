package org.saleorderoptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.Alerts.AlertBox;
import org.databases.Stockdb;
import org.orderoptions.OrderDataView;
import org.orderoptions.Orderdb;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.saleoptions.Sale;
import org.saleoptions.Saledb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.warrantyoptions.Warranty;
import org.warrantyoptions.Warrantydb;

import java.net.URL;
import java.util.ResourceBundle;

import static org.orderoptions.OrderDashboardController.__oid;
import static org.saleorderoptions.SaleOrderController.__saleOrder;

public class SaleOrderEditViewController implements Initializable {

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


    SaleOrder saleOrder = __saleOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conboxData();

        stockidtxt.setText(saleOrder.getStockcode());
        stocknametxt.setText(saleOrder.getStockname());
        stockqtytxt.setText(String.valueOf(saleOrder.getQty()));
        stockpricetxt.setText(String.valueOf(saleOrder.getPrice()));
        stockdiscount.setText(String.valueOf(saleOrder.getDiscount()));
        stockwarranty.setValue(saleOrder.getWdesc());


        newItem.setOnAction(event -> {

            try {

                Sale sale = new Sale(saleOrder.getStockcode(), __oid, getWarrantyId(stockwarranty.getValue()), Integer.parseInt(stockqtytxt.getText()), Integer.parseInt(stockpricetxt.getText()), Integer.parseInt(stockdiscount.getText()));


                Payment payment = new Payment(saleOrder.getPayid(), sale.getTotal());
                int i =new SaleServices().updateSaleAndUpdatePayment(sale, saleOrder.getQty(), saleOrder.getPrice(), saleOrder.getDiscount(), payment);

                if(i==1){

                    AlertBox.showInformation("အရောင်းစာမျက်နှာ","ပြင်ဆင်ခြင်း အောင်မြင်ပါသည်။");


                }


            }catch (Exception e) {

                AlertBox.showError("အရောင်းစာမျက်နှာ","မှားယွင်းနေပါသည်။");

            }

        });






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
}
