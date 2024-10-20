package org.saleoptions;

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
import org.controllers.ApplicationViewController;
import org.models.Stock;

import java.io.IOException;
import java.net.URL;
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
    private ComboBox<?> stockwarranty;

    @FXML
    private Button submitItembtn;






    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ini();
        actionEvent();
        tableIni();


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
    }
}
