package org.saleoptions;

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
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private TextField stocktotaltxt;

    @FXML
    private ComboBox<String> stockwarranty;

    @FXML
    private Button submitItembtn;



    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Stockdb stockdb = context.getBean("stockdb", Stockdb.class);
    Saledb saledb  = context.getBean("saledb",Saledb.class);
    Warrantydb warrantydb  = context.getBean("warrantydb",Warrantydb.class);




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

                    stocktotaltxt.setText(String.valueOf(result));
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

                    stocktotaltxt.setText(String.valueOf(result));
                }


            }


        });





        stockpricetxt.setOnKeyReleased(event -> {

            try {

                int discount = Objects.equals(stockdiscount.getText(), "") ?0:Integer.parseInt(stockdiscount.getText());


                int qty = stockqtytxt.getText()==null?0: Integer.parseInt(stockqtytxt.getText());
                int price = stockpricetxt.getText()==null?0:Integer.parseInt(stockpricetxt.getText());

                int result = (qty*price)-discount;

                stocktotaltxt.setText(String.valueOf(result));

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

              stocktotaltxt.setText(String.valueOf(result));

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
