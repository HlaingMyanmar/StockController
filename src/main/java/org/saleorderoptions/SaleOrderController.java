package org.saleorderoptions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.Alerts.AlertBox;
import org.controllers.ApplicationViewController;
import org.models.PurchaseList;
import org.orderoptions.Orderdb;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.saleoptions.Sale;
import org.saleoptions.Saledb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.orderoptions.OrderDashboardController.__odate;
import static org.orderoptions.OrderDashboardController.__oid;

public class SaleOrderController implements Initializable {



    @FXML
    private Label codetxt;

    @FXML
    private Label datetxt;

    @FXML
    private TableColumn<SaleOrder, Integer> discountCol;

    @FXML
    private TableColumn<SaleOrder, String> codeCol;

    @FXML
    private Button editbtn;



    @FXML
    private TableColumn<SaleOrder, String> nameCol;

    @FXML
    private Button newItem;

    @FXML
    private TableColumn<SaleOrder, Integer> priceCol;


    @FXML
    private TableColumn<SaleOrder, Integer> qtyCol;

    @FXML
    private TableView saletable;

    @FXML
    private Label stocktotaltxt;

    @FXML
    private TableColumn<SaleOrder, Integer> totalCol;

    @FXML
    private TableColumn<SaleOrder, String> warrantyCol;




    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    SaleOrderdb saleOrderdb  = context.getBean("saleorderdb",SaleOrderdb.class);

    Saledb saledb  = context.getBean("saledb",Saledb.class);

    Orderdb orderdb  = context.getBean("orderdb",Orderdb.class);

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);

    public static SaleOrder __saleOrder = null;

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


        saletable.setOnMouseClicked(event -> {


            correcttableIcon();





        });



    }

    private void ini() {

        codetxt.setText(__oid);
        datetxt.setText(String.valueOf(__odate));

        deletetableIcon();


        saletable.getItems().setAll( saleOrderdb.getFindID(__oid));





    }

    private void deletetableIcon() {

        Callback<TableColumn<SaleOrder, String>, TableCell<SaleOrder, String>> cellFactory = (event) -> new TableCell<>() {
            private final Button editButton = new Button();

            private final Image editImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/cancel.png")));

            private final ImageView editImageView = new ImageView(editImage);

            {

                editImageView.setFitWidth(30);
                editImageView.setFitHeight(20);


                editButton.setGraphic(editImageView);



                editButton.setOnAction(event -> {


                    SaleOrder saleOrder = getTableView().getItems().get(getIndex());

                try {


                    SaleServices saleServices = new SaleServices();
                    saleServices.deleteSaleAndUpdatePayment(__oid, saleOrder);

                    AlertBox.showInformation("အရောင်း ပြန်လည်ပြုပြင်ခြင်း စာမျက်နှာ","အောင်မြင်သည်။");


                }catch (Exception e) {



                }


                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        };

        TableColumn<SaleOrder, String> editColumn = new TableColumn<>("ဖျက်ရန်");
        editColumn.setCellFactory(cellFactory);
        saletable.getColumns().add(editColumn);


    }

    private void correcttableIcon() {

        Callback<TableColumn<SaleOrder, String>, TableCell<SaleOrder, String>> cellFactory = (event) -> new TableCell<>() {

            private final Button editButton = new Button();

            private final Image editImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/correct.png")));

            private final ImageView editImageView = new ImageView(editImage);

            {

                editImageView.setFitWidth(30);
                editImageView.setFitHeight(20);


                editButton.setGraphic(editImageView);



                editButton.setOnAction(event -> {


                    SaleOrder saleOrder = getTableView().getItems().get(getIndex());

                    __saleOrder=saleOrder;


                    Stage stage = new Stage();

                    FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/saleordereditview.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.initStyle(StageStyle.UTILITY);
                    stage.initModality(Modality.WINDOW_MODAL);
                    Stage mainStage = (Stage) editbtn.getScene().getWindow();
                    stage.setTitle("ငွေပေး‌ချေခြင်းပုံစံ");
                    stage.initOwner(mainStage);
                    stage.setScene(scene);
                    stage.show();

                    stage.setOnCloseRequest(closeEvent -> {


                        ini();

                    });

                });


            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        };



        TableColumn<SaleOrder, String> editColumn = new TableColumn<>("သတ်မှတ်ရန်");
        editColumn.setCellFactory(cellFactory);
        saletable.getColumns().add(editColumn);

    }




}

