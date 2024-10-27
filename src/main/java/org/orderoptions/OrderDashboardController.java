package org.orderoptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controllers.ApplicationViewController;
import org.databases.Stockdb;
import org.paymentoptions.Paymentdb;
import org.saleoptions.Saledb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.warrantyoptions.Warrantydb;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.Generator.Currency.convertToMyanmarCurrency;

public class OrderDashboardController implements Initializable {

    @FXML
    private TableColumn<OrderDataView, String> cunameCol;

    @FXML
    private TableColumn<OrderDataView, Date> dateCol;

    @FXML
    private TableColumn<OrderDataView, String> noteCol;

    @FXML
    private TableColumn<OrderDataView, String> orcodeCol;

    @FXML
    private TableColumn<OrderDataView, String> paymethodNameCol;

    @FXML
    private TableColumn<OrderDataView, Integer> totalCol;

    @FXML
    private TableView ordertable;

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
    private TextField searchtxt;


    @FXML
    private Button neworderbtn;


    public static  String __oid= null;
    public static  Date __odate= null;




    @FXML
    private BarChart<String, Number> barChart;


    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Stockdb stockdb = context.getBean("stockdb", Stockdb.class);
    Saledb saledb  = context.getBean("saledb",Saledb.class);
    Warrantydb warrantydb  = context.getBean("warrantydb",Warrantydb.class);
    Orderdb orderdb  = context.getBean("orderdb",Orderdb.class);
    Paymentdb paymentdb  = context.getBean("paymentdb", Paymentdb.class);


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        tableIni();
        actionEvent();
        ini();


    }

    private void ini() {


        getAmount(getLocalList(),lbTotal);
        getQtyItem(getLocalList(),lbCount);

        int day = LocalDate.now().getDayOfMonth();
        dayPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, day));

        int year = LocalDate.now().getYear();
        yearPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, year));

        int month = LocalDate.now().getMonthValue();
        monthPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        monthPicker.getValueFactory().setValue(month);

        ObservableList<OrderDataView> observableList = FXCollections.observableArrayList();


        observableList.addAll(getLocalList());


        FilteredList<OrderDataView> filteredData = new FilteredList<>(observableList, b -> true);

        addTextFieldListener(searchtxt, filteredData);



        SortedList<OrderDataView> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(ordertable.comparatorProperty());

        ordertable.setItems(sortedData);




    }

    private void actionEvent() {



        ordertable.setOnMouseClicked(event -> {



            if(event.getClickCount()==2){


                OrderDataView orderDataView = (OrderDataView) ordertable.getSelectionModel().getSelectedItem();


                __oid =orderDataView.getOid();
                __odate = orderDataView.getOdate();


                Stage stage = new Stage();



                FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/saleorderview.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.initStyle(StageStyle.UTILITY);
                stage.initModality(Modality.WINDOW_MODAL);
                Stage mainStage = (Stage) neworderbtn.getScene().getWindow();
                stage.setTitle("အရောင်း ပြုပြင်ခြင်း စာမျက်နှာ");
                stage.initOwner(mainStage);
                stage.setScene(scene);
                stage.show();

                stage.setOnCloseRequest(closeEvent -> {


                    ini();

                });



            }



        });

        neworderbtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/saleview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) neworderbtn.getScene().getWindow();
            stage.setTitle("အရောင်း စာမျက်နှာ");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(closeEvent -> {


                ini();

            });


        });

        dayPicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<OrderDataView> pList =orderdb.getViewList();
            ObservableList<OrderDataView> orderDataViews = FXCollections.observableArrayList();

            orderDataViews .addAll(pList
                    .stream()
                    .filter(orderDataView -> orderDataView.getOdate().toLocalDate().getYear()==yearPicker.getValue())
                    .filter(orderDataView -> orderDataView.getOdate().toLocalDate().getMonthValue()==monthPicker.getValue())
                    .filter(orderDataView -> orderDataView.getOdate().toLocalDate().getDayOfMonth()==newValue)
                    .toList());



            FilteredList<OrderDataView> filteredData = new FilteredList<>(orderDataViews, b -> true);

            addTextFieldListener(searchtxt, filteredData);



            SortedList<OrderDataView> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(ordertable.comparatorProperty());

            ordertable.setItems(sortedData);

            getAmount(filteredData,lbTotal);
            getQtyItem(filteredData,lbCount);
            createBarChart(filteredData);




        });

        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<OrderDataView> pList =orderdb.getViewList();
            ObservableList<OrderDataView> orderDataViews = FXCollections.observableArrayList();

            orderDataViews.addAll(pList
                    .stream()
                    .filter(orderDataView -> orderDataView.getOdate().toLocalDate().getYear()==yearPicker.getValue())
                    .filter(orderDataView -> orderDataView.getOdate().toLocalDate().getMonthValue()==newValue)
                    .toList());

            FilteredList<OrderDataView> filteredData = new FilteredList<>(orderDataViews, b -> true);

            addTextFieldListener(searchtxt, filteredData);



            SortedList<OrderDataView> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(ordertable.comparatorProperty());

            ordertable.setItems(sortedData);

            getAmount(filteredData,lbTotal);
            getQtyItem(filteredData,lbCount);

            createBarChart(filteredData);





        });

        yearPicker.valueProperty().addListener((observable, oldValue, newValue)  -> {

            List<OrderDataView> pList =orderdb.getViewList();
            ObservableList<OrderDataView> orderDataViews = FXCollections.observableArrayList();


            orderDataViews.addAll(pList
                    .stream()
                    .filter(orderDataView -> orderDataView.getOdate().toLocalDate().getYear()==newValue)
                    .toList());

            FilteredList<OrderDataView> filteredData = new FilteredList<>(orderDataViews, b -> true);

            addTextFieldListener(searchtxt, filteredData);



            SortedList<OrderDataView> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(ordertable.comparatorProperty());

            ordertable.setItems(sortedData);

            getAmount(filteredData,lbTotal);
            getQtyItem(filteredData,lbCount);

            createBarChart(filteredData);



        });


    }

//    private void createChart(ObservableList<OrderDataView> list) {
//
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
//
//        list.stream()
//                .collect(Collectors.groupingBy(
//                        OrderDataView::getCuname,
//                        Collectors.summingInt(OrderDataView::getTotal)))  // Summing up the 'total' field
//                .forEach((category, total) -> {
//                    pieChartData.add(new PieChart.Data(category+"\n"+total+" MMK", total));
//                });
//
//        pipeChart.setData(pieChartData);
//    }


//    private void createLineChart(ObservableList<OrderDataView> list) {
//
//
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("Order Summary");
//
//
//        list.stream()
//                .collect(Collectors.groupingBy(
//                        OrderDataView::getCuname,
//                        Collectors.summingInt(OrderDataView::getTotal)))
//                .forEach((category, total) -> {
//
//                    series.getData().add(new XYChart.Data<>(category, total));
//                });
//
//
//        lineChart.getData().clear();
//        lineChart.getData().add(series);
//    }

    private void createBarChart(ObservableList<OrderDataView> list) {


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Order Summary");


        list.stream()
                .collect(Collectors.groupingBy(
                        OrderDataView::getCuname,
                        Collectors.summingInt(OrderDataView::getTotal)))
                .forEach((category, total) -> {

                    series.getData().add(new XYChart.Data<>(category, total));
                });


        barChart.getData().clear();
        barChart.getData().add(series);
    }



    private void tableIni() {

        orcodeCol.setCellValueFactory(new PropertyValueFactory<>("oid"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("odate"));
        cunameCol.setCellValueFactory(new PropertyValueFactory<>("cuname"));
        paymethodNameCol.setCellValueFactory(new PropertyValueFactory<>("paymethodname"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("remarks"));


    }

    private ObservableList<OrderDataView> getLocalList(){

        LocalDate today = LocalDate.now();

        ObservableList<OrderDataView> orderDataViews= FXCollections.observableArrayList();

        orderDataViews.addAll(orderdb.getViewList() .stream()
                .filter(purchaseList -> purchaseList.getOdate().toLocalDate().equals(today))
                .toList());

        createBarChart(orderDataViews);


        return  orderDataViews;

    }



    private void getAmount(ObservableList<OrderDataView> list,Label label){

        int sum =   list.stream()
                .mapToInt(OrderDataView::getTotal)
                .sum();
        label.setText(convertToMyanmarCurrency(sum));

    }
    private void getQtyItem(ObservableList<OrderDataView> list,Label label){

        label.setText(String.valueOf(list.size()));

    }
    private void addTextFieldListener(TextField textField, FilteredList<OrderDataView> filteredData) {


        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(stock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (stock.getOid().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (stock.getCuname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (stock.getPaymethodname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(stock.getTotal()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                }


                return false;
            });

            getAmount(filteredData,lbTotal);
            getQtyItem(filteredData,lbCount);

        });


    }
}