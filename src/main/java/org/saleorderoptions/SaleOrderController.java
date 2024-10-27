package org.saleorderoptions;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.Alerts.AlertBox;
import org.controllers.ApplicationViewController;
import org.models.PurchaseList;
import org.orderoptions.Order;
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

import static org.Generator.Currency.convertToMyanmarCurrency;
import static org.orderoptions.OrderDashboardController.*;

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

    @FXML
    private TextField cuphone;

    @FXML
    private TextField cutxt;

    @FXML
    private JFXCheckBox wavechebox;


    @FXML
    private JFXCheckBox kbzchebox;


    @FXML
    private JFXCheckBox cashchebox;

    @FXML
    private TextArea curemark;



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

            if(event.getButton()== MouseButton.SECONDARY)
            {

                correcttableIcon();
                deletetableIcon();

            }
        });


        editbtn.setOnAction(event -> {

            int oldAmount = saleOrderdb.getFindID(__oid).stream()
                    .mapToInt(SaleOrder::getTotal)
                    .sum();


            String cuName =  cutxt.getText();
            String cuPhone =  cuphone.getText();
            String cuRemark = curemark.getText();
            int    cuPayName = getPayId(String.valueOf(getFirstSelectedCheckbox(kbzchebox,wavechebox,cashchebox).getText()));

            Order order = new Order(__oid,__odate,cuName,cuPhone,cuRemark,cuPayName);

            int  oldPayId = orderdb.getAllList().stream()
                    .filter(order1 -> order1.getOid().equals(__oid))
                    .map(Order::getPayid)
                    .findFirst()
                    .orElse(-1);


            int result = new SaleServices().updateOrderAndUpdatePaymentMethod(order,oldPayId,oldAmount);

            if(result==1){

                AlertBox.showInformation("အရောင်းစာမျက်နှာ","ပြန်ပြုပြင်ခြင်းအောင်မြင်ပါသည်။");




            }








        });

        newItem.setOnAction(event -> {

            Stage stage  = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/newitemview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) newItem.getScene().getWindow();
            stage.setTitle("အရောင်း ပြုပြင်ခြင်း စာမျက်နှာ");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();



            stage.setOnCloseRequest(closeEvent -> {


                ini();

            });

        });






    }
    private Integer getPayId(String payname) {
        return paymentdb.getAllList().stream()
                .filter(payment->payment.getPaymethodname().equals(payname))
                .map(Payment::getPayid)
                .findFirst()
                .orElse(null);
    }

    private CheckBox getFirstSelectedCheckbox(CheckBox... checkBoxes) {
        for (CheckBox checkbox : checkBoxes) {
            if (checkbox.isSelected()) {
                return checkbox;
            }
        }
        return null;
    }

    private void ini() {

        saletable.getItems().clear();

        Order orderr = orderdb.getAllList().stream()
                .filter(order -> order.getOid().equals(__oid))
                .findFirst().orElse(null);

        codetxt.setText(__oid);
        assert orderr != null;
        datetxt.setText(String.valueOf(orderr.getOdate()));
        cuphone.setText(orderr.getCuphone());
        cutxt.setText(orderr.getCuname());
        curemark.setText(orderr.getRemark());


        int payid = orderr.getPayid();


        switch (payid){

            case 5 : kbzchebox.setSelected(true);break;
            case 6 : wavechebox.setSelected(true);break;
            case 7 : cashchebox.setSelected(true);break;

        }


        saletable.getItems().setAll( saleOrderdb.getFindID(__oid));

        kbzchebox.setOnAction(event -> handleCheckboxSelection(kbzchebox, wavechebox, cashchebox));
        wavechebox.setOnAction(event -> handleCheckboxSelection(wavechebox, kbzchebox, cashchebox));
        cashchebox.setOnAction(event -> handleCheckboxSelection(cashchebox, kbzchebox, wavechebox));



        double total = saleOrderdb.getFindID(__oid).stream()
                        .mapToDouble(SaleOrder::getTotal)
                        .sum();
        stocktotaltxt.setText(convertToMyanmarCurrency(total));




    }




    private void handleCheckboxSelection(CheckBox selectedCheckbox, CheckBox... otherCheckboxes) {

        if (selectedCheckbox.isSelected()) {
            for (CheckBox checkbox : otherCheckboxes) {
                checkbox.setSelected(false);
            }
        }
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

