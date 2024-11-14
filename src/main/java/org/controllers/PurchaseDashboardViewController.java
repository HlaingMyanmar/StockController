package org.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.stage.Popup;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.Alerts.AlertBox;
import org.Generator.Deliver;
import org.databases.PurchasehasStockdb;
import org.databases.Stockdb;
import org.models.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.Generator.Currency.convertToMyanmarCurrency;

public class PurchaseDashboardViewController extends Deliver implements Initializable {

    @FXML
    private Spinner<Integer> dayPicker;

    @FXML
    private Spinner<Integer> monthPicker;

    @FXML
    private Spinner<Integer> yearPicker;

    @FXML
    private Label lbCount;

    @FXML
    private Label lbTotal;


    @FXML
    private Button newpurchasebtn;

    @FXML
    private TableColumn<PurchaseList, String> editCol;

    @FXML
    private TableColumn<PurchaseList, String> pcodeCol;

    @FXML
    private TableColumn<PurchaseList, Date> pdateCol;

    @FXML
    private TableColumn<PurchaseList, String> psupplierCol;

    @FXML
    private TableColumn<PurchaseList, Integer> ptotalCol;

    @FXML
    private Button printpurchasebtn;

    @FXML
    private TableView  purchasetable;

    @FXML
    private PieChart pipeChart;


    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    PurchasehasStockdb phsdb = context.getBean("purchasehastockdb", PurchasehasStockdb.class);

    Stockdb stockdb = context.getBean("stockdb", Stockdb.class);

    PurchasehasStockdb purchasehasStockdb = context.getBean("purchasehastockdb",PurchasehasStockdb.class);


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ini();
        ActionEvent();
        tableIni();




        purchasetable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }




    private void ini() {

        purchasetable.setItems(getList());

        getAmount(getList(),lbTotal);
        getQtyItem(getList(),lbCount);
        createChart(getList());
        tableCellsetIcon();


        int day = LocalDate.now().getDayOfMonth();
        dayPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, day));

        int year = LocalDate.now().getYear();
        yearPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, year));

        int month = LocalDate.now().getMonthValue();
        monthPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        monthPicker.getValueFactory().setValue(month);




    }

    private void ActionEvent() {

        printpurchasebtn.setOnAction(event -> {



            ObservableList<PurchaseList> purchaseLists = purchasetable.getSelectionModel().getSelectedItems();


            exportToPDF(purchaseLists,new Stage());



        });


        newpurchasebtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/newpurchaseview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) newpurchasebtn.getScene().getWindow();
            stage.setTitle("Purchase");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();


            stage.setOnCloseRequest(event1 -> {

                ini();

            });

        });



        dayPicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<PurchaseList> pList = phsdb.getPurchaseDashboardList();
            ObservableList<PurchaseList> purchaseLists = FXCollections.observableArrayList();

            purchaseLists.addAll(pList
                    .stream()
                    .filter(purchaseList -> purchaseList.getPudate().toLocalDate().getYear()==yearPicker.getValue())
                    .filter(purchaseList -> purchaseList.getPudate().toLocalDate().getMonthValue()==monthPicker.getValue())
                    .filter(purchaseList -> purchaseList.getPudate().toLocalDate().getDayOfMonth()==newValue)
                    .toList());

            purchasetable.setItems(purchaseLists);
            createChart(purchaseLists);

            getAmount(purchaseLists,lbTotal);
            getQtyItem(purchaseLists,lbCount);



        });

        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<PurchaseList> pList = phsdb.getPurchaseDashboardList();
            ObservableList<PurchaseList> purchaseLists = FXCollections.observableArrayList();

            purchaseLists.addAll(pList
                    .stream()
                    .filter(purchaseList -> purchaseList.getPudate().toLocalDate().getYear()==yearPicker.getValue())
                    .filter(purchaseList -> purchaseList.getPudate().toLocalDate().getMonthValue()==newValue)
                    .toList());

            purchasetable.setItems(purchaseLists);
            createChart(purchaseLists);

            getAmount(purchaseLists,lbTotal);
            getQtyItem(purchaseLists,lbCount);




        });

        yearPicker.valueProperty().addListener((observable, oldValue, newValue)  -> {

            List<PurchaseList> pList = phsdb.getPurchaseDashboardList();
            ObservableList<PurchaseList> purchaseLists = FXCollections.observableArrayList();

            purchaseLists.addAll(pList
                    .stream()
                    .filter(purchaseList -> purchaseList.getPudate().toLocalDate().getYear()==newValue)
                    .toList());

            purchasetable.setItems(purchaseLists);
            createChart(purchaseLists);

            getAmount(purchaseLists,lbTotal);
            getQtyItem(purchaseLists,lbCount);


        });




    }

    private void tableIni() {
        pcodeCol.setCellValueFactory(new PropertyValueFactory<>("puid"));
        editCol.setCellValueFactory(new PropertyValueFactory<>(""));
        pdateCol.setCellValueFactory(new PropertyValueFactory<>("pudate"));
        psupplierCol.setCellValueFactory(new PropertyValueFactory<>("suname"));
        ptotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

    }

    private ObservableList<PurchaseList> getList(){

        LocalDate today = LocalDate.now();

        ObservableList<PurchaseList> purchaseLists = FXCollections.observableArrayList();

        purchaseLists.addAll(phsdb.getPurchaseDashboardList() .stream()
                .filter(purchaseList -> purchaseList.getPudate().toLocalDate().equals(today))
                .toList());

        return purchaseLists;

    }

    private void getAmount(ObservableList<PurchaseList> list,Label label){

        int sum =   list.stream()
                .mapToInt(PurchaseList::getTotal)
                .sum();
        label.setText(convertToMyanmarCurrency(sum));

    }
    private void getQtyItem(ObservableList<PurchaseList> list,Label label){

        label.setText(String.valueOf(list.size()));

    }

//    private void createChart(ObservableList<PurchaseList>list){
//
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
//
//        list.stream()
//                .collect(Collectors.groupingBy(
//                        PurchaseList::getSuname,
//                        Collectors.counting()))
//                .forEach((category,count)->{
//
//                    pieChartData.add(new PieChart.Data(category,count));
//
//                });
//
//        pipeChart.setData(pieChartData);
//
//
//    }
    private void createChart(ObservableList<PurchaseList> list) {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        list.stream()
                .collect(Collectors.groupingBy(
                        PurchaseList::getSuname,
                        Collectors.summingInt(PurchaseList::getTotal)))  // Summing up the 'total' field
                .forEach((category, total) -> {
                    pieChartData.add(new PieChart.Data(category+"\n"+total+" MMK", total));
                });

        pipeChart.setData(pieChartData);
    }

    private void tableCellsetIcon() {



        Callback<TableColumn<PurchaseList, String>, TableCell<PurchaseList, String>> cellFactory = (event) -> new TableCell<>() {

            private final Button editButton = new Button();
            private final Image editImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/editdata.png")));

            {
                ImageView imageView = new ImageView(editImage);
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                editButton.setGraphic(imageView);

                editButton.setOnAction(event -> {
                    int index = getIndex();
                    if (index < 0 || index >= purchasetable.getItems().size()) {
                        return;
                    }


                    PurchaseList purchaseList = (PurchaseList) purchasetable.getItems().get(index);

                    TableView<PurchaseStockList> purchaseTableView = new TableView<>();

                    TableColumn<PurchaseStockList, String> pstockname = new TableColumn<>("အမျိုးအမည်");

                    TableColumn<PurchaseStockList, Integer> ppriceCol = new TableColumn<>("ဈေးနှုန်း");

                    TableColumn<PurchaseStockList, Integer> pqtyCol = new TableColumn<>("အရေအတွက်");

                    TableColumn<PurchaseStockList, Integer> ptotalCol = new TableColumn<>("စုစုပေါင်း");



                    pstockname.prefWidthProperty().bind(purchaseTableView.widthProperty().multiply(0.4)); // 40% of table width
                    ppriceCol.prefWidthProperty().bind(purchaseTableView.widthProperty().multiply(0.2)); // 20% of table width
                    pqtyCol.prefWidthProperty().bind(purchaseTableView.widthProperty().multiply(0.2)); // 20% of table width
                    ptotalCol.prefWidthProperty().bind(purchaseTableView.widthProperty().multiply(0.2)); // 20% of table width


                    pstockname.setCellValueFactory(new PropertyValueFactory<>("stockname"));
                    pqtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
                    ppriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
                    ptotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

                    purchaseTableView.getColumns().addAll(pstockname,pqtyCol,ppriceCol,ptotalCol);




                    List<PurchaseStockList> purchaseStockLists = phsdb.getPurchaseStockList(purchaseList.getPuid());
                    purchaseTableView.getItems().setAll(purchaseStockLists);

                    double totalAmount = purchaseStockLists.stream()
                            .mapToDouble(PurchaseStockList::getTotal) // Assuming getTotal() returns an int
                            .sum();


                    Label totalLabel = new Label("စုစုပေါင်းကုန်ကျငွေ : ( " + convertToMyanmarCurrency(totalAmount) + " )");
                    Label labelcount = new Label("အရေအတွက်  :( " + purchaseStockLists.size() + " )ခုရှိပါသည်။");

                    totalLabel.setStyle("-fx-font-weight: bold; -fx-padding: 15;");
                    labelcount.setStyle("-fx-font-weight: bold; -fx-padding: 15;");

                    Popup popup = new Popup();
                    HBox hBox = new HBox(labelcount, totalLabel);
                    VBox vbox = new VBox(purchaseTableView, hBox);
                    vbox.setPadding(new Insets(10));
                    vbox.setStyle("-fx-background-color: white; -fx-border-color: gray;");
                    popup.getContent().add(vbox);

                    Point2D cellLocation = localToScreen(getLayoutBounds().getMinX(), getLayoutBounds().getMaxY());
                    popup.setX(cellLocation.getX());
                    popup.setY(cellLocation.getY());

                    popup.setAutoHide(true);
                    popup.show(getScene().getWindow());


                    AtomicReference<String> stockcode = new AtomicReference<>();
                    AtomicInteger qty = new AtomicInteger();
                    AtomicInteger price = new AtomicInteger();
                    AtomicInteger total = new AtomicInteger();


                    purchaseTableView.setOnMouseClicked(event1 -> {




                        if(event1.getClickCount()==2){


                            PurchaseStockList selectedItem= purchaseTableView.getSelectionModel().getSelectedItem();

                            if(selectedItem!=null){

                                stockcode.set(getStockCode(selectedItem.getStockname()));
                                qty.set(selectedItem.getQty());
                                price.set(selectedItem.getPrice());
                                total.set(selectedItem.getTotal());


                            }


                        }



                    });

                    purchaseTableView.setEditable(true);

                    pqtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

                    pqtyCol.setOnEditCommit(event1 -> {

                        try {

                            int netvalue = 0;

                            int newvalue = event1.getNewValue();
                            int oldvalue = event1.getOldValue();

                            int result = purchasehasStockdb.update(new PurchasehasStock(

                                    purchaseList.getPuid(),
                                    stockcode.get(),
                                    newvalue,
                                    stockdb.getAllList().stream().filter(stock -> stock.getStockcode().equals(stockcode.get())).map(Stock::getPrice).findFirst().orElse(null)

                            ));




                            if (oldvalue > newvalue) {

                                netvalue = oldvalue - newvalue;


                                if (stockdb.subQty(new Stock(stockcode, netvalue)) == 1 && result==1) {

                                    AlertBox.showInformation("အောင်မြင်ပါသည်", "ပြန်လည်ပြုပြခြင်း အောင်မြင်ပါသည်။");

                                }

                            } else if (oldvalue < newvalue) {


                                netvalue = newvalue - oldvalue;


                                if (stockdb.sumQty(new Stock(stockcode, netvalue)) == 1 && result==1) {

                                    AlertBox.showInformation("အောင်မြင်ပါသည်", "ပြန်လည်ပြုပြခြင်း အောင်မြင်ပါသည်။");

                                }

                            }
                        }catch (NumberFormatException e){

                            AlertBox.showWarning("သတိပြုပါ။", "Number မဟုတ်သည့်အရာများ ထည့်သွင်း၍မရပါ။");

                        }

                    });


                    ppriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
                    ppriceCol.setOnEditCommit(event2 -> {



                        try {



                        int newvalue = event2.getNewValue();

                            int result = purchasehasStockdb.update(new PurchasehasStock(

                                    purchaseList.getPuid(),
                                    stockcode.get(),
                                    purchasehasStockdb.getAllList().stream().filter(stock -> stock.getStockcode().equals(stockcode.get())).map(PurchasehasStock::getQty).findFirst().orElse(null),
                                    newvalue


                            ));

                            if(result==1){

                                AlertBox.showInformation("အောင်မြင်ပါသည်", "ပြန်လည်ပြုပြခြင်း အောင်မြင်ပါသည်။");

                                int b = JOptionPane.showConfirmDialog(null,"၎င်း‌ဈေးနှုန်းသည် ရောင်း‌ဈေးအဖြစ်သတ်မှတ်မည်လား ....."+"\nသတ်မှတ်မည်ဆိုပါက 'Yes' ကိုနှိပ်ပါ။");

                                switch (b){

                                    case JOptionPane.YES_NO_OPTION ->{

                                        Stock stock = new Stock();
                                        stock.setStockcode(stockcode.get());
                                        stock.setPrice(newvalue);

                                        if (stockdb.setPrice(stock) == 1) {
                                            AlertBox.showInformation("အောင်မြင်ပါသည်", "‌ဈေးနှုန်း ပြန်လည်ပြုပြခြင်း အောင်မြင်ပါသည်။");
                                        }

                                    }

                                    case JOptionPane.NO_OPTION -> AlertBox.showInformation("ပြောင်းလဲခြင်း","ဈေးနှုန်း ပြောင်းလဲခြင်း မလုပ်ဆောင်ပါ။");

                                    default -> {

                                        AlertBox.showInformation("ပယ်ဖျက်ခြင်း", "လုပ်ဆောင်မှုကို ပယ်ဖျက်ခဲ့ပါသည်။");

                                    }
                                }

                            }

                        }catch (NumberFormatException e){

                            AlertBox.showWarning("သတိပြုပါ။", "Number မဟုတ်သည့်အရာများ ထည့်သွင်း၍မရပါ။");

                        }

                    });












                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(editButton);
                    setText(null);
                }
            }
        };

        editCol.setCellFactory(cellFactory);
    }

    private void exportToPDF(List<PurchaseList> categoryList, Stage stage) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));


        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {

            Document document = new Document();

            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();


                Paragraph title = new Paragraph("Purchase List Export"+": "+ LocalDate.now());

                title.setSpacingBefore(20f);
                title.setSpacingAfter(20f);
                title.setLeading(1.5f * 12f);


                document.add(title);



                PdfPTable table = new PdfPTable(4);


                table.addCell("Date");
                table.addCell("Purchase Code");
                table.addCell("Supplier Name");
                table.addCell("Total");



                for (PurchaseList p : categoryList) {
                    table.addCell(String.valueOf(p.getPudate()));
                    table.addCell(p.getPuid());
                    table.addCell(p.getSuname());
                    table.addCell(String.valueOf(p.getTotal()));

                }


                document.add(table);

                document.close();
                AlertBox.showInformation("PDF","PDF created successfully!");

            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
