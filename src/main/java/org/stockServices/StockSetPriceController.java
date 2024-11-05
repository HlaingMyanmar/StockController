package org.stockServices;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.Alerts.AlertBox;
import org.databases.Stockdb;
import org.models.Stock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import static org.controllers.StockViewController._stockcode;

public class StockSetPriceController implements Initializable {

    @FXML
    private TableColumn<StockSetPrice, String> codeCol;

    @FXML
    private TableColumn<StockSetPrice, Date> dateCol;

    @FXML
    private TableColumn<StockSetPrice, Integer> priceCol;

    @FXML
    private TextField pricetxt;

    @FXML
    private TableView  stockpricetable;

    @FXML
    private Button submitbtn;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Stockdb dao = context.getBean("stockdb", Stockdb.class);

    StockSetPricedb stockSetPricedb  = context.getBean("stocksetpricedb", StockSetPricedb.class);


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        tableIni();


        stockpricetable.getItems().setAll(getLoadData(_stockcode));





        submitbtn.setOnAction(ed -> {


            try {



                int price = Integer.parseInt(pricetxt.getText());

                if(dao.setPrice(new Stock( _stockcode,price))==1){

                    AlertBox.showInformation("ပစ္စည်းစာရင်းများ", "ဈေးနှုန်းသတ်မှတ်ခြင်း အောင်မြင်သည်။");

                }

            }catch (NumberFormatException e ){

                AlertBox.showError("ပစ္စည်းစာရင်းများ", "ဈေးနှုန်းသတ်မှတ်ပါ။");


            }

        });


    }

    private void tableIni() {


        codeCol.setCellValueFactory(new PropertyValueFactory<>("puid"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("pudate"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    private List<StockSetPrice> getLoadData(String code){


        return stockSetPricedb.getList(code);





    }
}
