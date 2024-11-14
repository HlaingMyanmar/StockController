package org.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.databases.PurchasehasStockdb;
import org.databases.Stockdb;
import org.expense_options.Exp_Typesdb;
import org.expense_options.Exp_View;
import org.expense_options.Exp_Viewdb;
import org.incomeoptions.Income_View;
import org.incomeoptions.Income_Viewdb;
import org.incomeoptions.Incometypedb;
import org.models.PurchaseList;
import org.orderoptions.OrderDataView;
import org.orderoptions.Orderdb;
import org.paymentoptions.Paymentdb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.Generator.Currency.convertToMyanmarCurrency;
import static org.usersoptions.LoginController._level;

public class ApplicationViewController  implements Initializable {

    @FXML
    private Label loginlevel;

    @FXML
    private MenuItem showStockbtn;

    @FXML
    private MenuItem showbrandbtn;

    @FXML
    private MenuItem showcategorybtn;

    @FXML
    private MenuItem showsupplierbtn;


    @FXML
    private MenuItem purchaseListsbtn;

    @FXML
    private MenuItem paymentbtn;

    @FXML
    private MenuItem warrantybtn;

    @FXML
    private MenuItem orderListbtn;


    @FXML
    private MenuItem exptype_btn;


    @FXML
    private MenuItem dailyexpe_btn;


    @FXML
    private MenuItem incometypes;

    @FXML
    private MenuItem dailyincome;



    @FXML
    private AnchorPane switchPane;

    @FXML
    private MenuItem dashboardbtn;


    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private PieChart incomepipeChart;

    @FXML
    private PieChart exppipeChart;


    @FXML
    private Label exptotal;

    @FXML
    private Label incometotal;

    @FXML
    private Label nettotal;






    @FXML
    private Spinner<Integer> monthPicker;

    @FXML
    private Spinner<Integer> yearPicker;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);

    Income_Viewdb incomeViewdb = context.getBean("income_viewdb",Income_Viewdb.class);

    Incometypedb incometypedb = context.getBean("incomedb",Incometypedb.class);

    PurchasehasStockdb phsdb = context.getBean("purchasehastockdb", PurchasehasStockdb.class);

    Stockdb stockdb = context.getBean("stockdb", Stockdb.class);

    PurchasehasStockdb purchasehasStockdb = context.getBean("purchasehastockdb",PurchasehasStockdb.class);

    Exp_Viewdb expViewdb = context.getBean("exp_viewdb", Exp_Viewdb.class);

    Exp_Typesdb expTypedb = context.getBean("exp_typesdb",Exp_Typesdb.class);

    Orderdb orderdb  = context.getBean("orderdb",Orderdb.class);



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginlevel.setText(_level);





        expensePicker();

        incomePicker();


        ini();

    }

    private void incomePicker() {


        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateIncomeData(yearPicker.getValue(), newValue);

        });

        yearPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateIncomeData(newValue, monthPicker.getValue());
        });
    }

    private void expensePicker() {
        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateExpenseData(yearPicker.getValue(), newValue);
        });

        yearPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateExpenseData(newValue, monthPicker.getValue());
        });
    }

    private void updateIncomeData(Integer year, Integer month) {


        List<Income_View> incomeViews = incomeViewdb.getAllList();
        List<Income_View> filteredIncome = incomeViews.stream()
                .filter(in -> in.getIncome_date().toLocalDate().getYear() == year &&
                        (month == null || in.getIncome_date().toLocalDate().getMonthValue() == month))
                .toList();

        List<OrderDataView> orderViews = orderdb.getViewList();
        List<OrderDataView> filteredOrders = orderViews.stream()
                .filter(order -> order.getOdate().toLocalDate().getYear() == year &&
                        (month == null || order.getOdate().toLocalDate().getMonthValue() == month))
                .toList();

        int totalIncome = filteredIncome.stream().mapToInt(Income_View::getAmount).sum();
        int totalOrderIncome = filteredOrders.stream().mapToInt(OrderDataView::getTotal).sum();

        int totalSum = totalIncome + totalOrderIncome;


        incomepipeChart.getData().clear();

        incomepipeChart.getData().addAll(
                new PieChart.Data("အခြားဝင်ငွေ - " + totalIncome, totalIncome),
                new PieChart.Data("အရောင်း ဝင်ငွေ - " + totalOrderIncome, totalOrderIncome)
        );

        incometotal.setText(convertToMyanmarCurrency(totalSum));

        populateLineChart2(lineChart,year,month);




    }

    private void updateExpenseData(Integer year, Integer month) {
        List<PurchaseList> purchaseList = phsdb.getPurchaseDashboardList();
        List<PurchaseList> filteredPurchases = purchaseList.stream()
                .filter(purchase -> purchase.getPudate().toLocalDate().getYear() == year &&
                        (month == null || purchase.getPudate().toLocalDate().getMonthValue() == month))
                .toList();

        List<Exp_View> expViewList = expViewdb.getAllListView();
        List<Exp_View> filteredExpenses = expViewList.stream()
                .filter(exp -> exp.getExpense_date().toLocalDate().getYear() == year &&
                        (month == null || exp.getExpense_date().toLocalDate().getMonthValue() == month))
                .toList();

        int totalPurchase = filteredPurchases.stream().mapToInt(PurchaseList::getTotal).sum();
        int totalExpense = filteredExpenses.stream().mapToInt(Exp_View::getTotal).sum();
        int totalSum = totalPurchase + totalExpense;


        exppipeChart.getData().clear();

        exppipeChart.getData().addAll(
                new PieChart.Data("အဝယ် သုံးငွေ - " + totalPurchase, totalPurchase),
                new PieChart.Data("အထွေထွေ သုံးငွေ - " + totalExpense, totalExpense)

        );

        exptotal.setText(convertToMyanmarCurrency(totalSum));

    }

    public void populateLineChart(LineChart<String, Number> lineChart, int year, Integer month) {

        List<Income_View> incomeViews = incomeViewdb.getAllList();
        List<Income_View> filteredIncome = incomeViews.stream()
                .filter(in -> in.getIncome_date().toLocalDate().getYear() == year &&
                        (month == null || in.getIncome_date().toLocalDate().getMonthValue() == month))
                .toList();


        Map<Integer, Double> monthlyIncomeTotals = filteredIncome.stream()
                .collect(Collectors.groupingBy(
                        in -> in.getIncome_date().toLocalDate().getMonthValue(),
                        Collectors.summingDouble(Income_View::getAmount)
                ));


        List<OrderDataView> orderViews = orderdb.getViewList();
        List<OrderDataView> filteredOrders = orderViews.stream()
                .filter(order -> order.getOdate().toLocalDate().getYear() == year &&
                        (month == null || order.getOdate().toLocalDate().getMonthValue() == month))
                .toList();


        Map<Integer, Double> monthlyOrderTotals = filteredOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOdate().toLocalDate().getMonthValue(),
                        Collectors.summingDouble(OrderDataView::getTotal)
                ));


        lineChart.getData().clear();


        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Monthly Income");
        for (int i = 1; i <= 12; i++) {
            incomeSeries.getData().add(new XYChart.Data<>(String.valueOf(i), monthlyIncomeTotals.getOrDefault(i, 0.0)));
        }


        XYChart.Series<String, Number> orderSeries = new XYChart.Series<>();
        orderSeries.setName("Monthly Orders");
        for (int i = 1; i <= 12; i++) {
            orderSeries.getData().add(new XYChart.Data<>(String.valueOf(i), monthlyOrderTotals.getOrDefault(i, 0.0)));
        }

        // Add series to line chart
        lineChart.getData().addAll(incomeSeries, orderSeries);
    }

    public void populateLineChart2(LineChart<String, Number> lineChart, int year, Integer month) {
        // Fetch the lists of data for purchases, expenses, and income
        List<PurchaseList> purchaseList = phsdb.getPurchaseDashboardList();
        List<PurchaseList> filteredPurchases = purchaseList.stream()
                .filter(purchase -> purchase.getPudate().toLocalDate().getYear() == year &&
                        (month == null || purchase.getPudate().toLocalDate().getMonthValue() == month))
                .toList();

        // Fetch expense data
        List<Exp_View> expViewList = expViewdb.getAllListView();
        List<Exp_View> filteredExpenses = expViewList.stream()
                .filter(exp -> exp.getExpense_date().toLocalDate().getYear() == year &&
                        (month == null || exp.getExpense_date().toLocalDate().getMonthValue() == month))
                .toList();

        // Fetch income data
        List<Income_View> incomeViews = incomeViewdb.getAllList();
        List<Income_View> filteredIncome = incomeViews.stream()
                .filter(in -> in.getIncome_date().toLocalDate().getYear() == year &&
                        (month == null || in.getIncome_date().toLocalDate().getMonthValue() == month))
                .toList();

        // Calculate total income by month
        Map<Integer, Double> monthlyIncomeTotals = filteredIncome.stream()
                .collect(Collectors.groupingBy(
                        in -> in.getIncome_date().toLocalDate().getMonthValue(),
                        Collectors.summingDouble(Income_View::getAmount)
                ));

        // Calculate total orders by month
        List<OrderDataView> orderViews = orderdb.getViewList();
        List<OrderDataView> filteredOrders = orderViews.stream()
                .filter(order -> order.getOdate().toLocalDate().getYear() == year &&
                        (month == null || order.getOdate().toLocalDate().getMonthValue() == month))
                .toList();

        Map<Integer, Double> monthlyOrderTotals = filteredOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOdate().toLocalDate().getMonthValue(),
                        Collectors.summingDouble(OrderDataView::getTotal)
                ));

        // Calculate total expenses by month
        Map<Integer, Double> monthlyExpenseTotals = filteredExpenses.stream()
                .collect(Collectors.groupingBy(
                        exp -> exp.getExpense_date().toLocalDate().getMonthValue(),
                        Collectors.summingDouble(Exp_View::getTotal)
                ));

        // Calculate total purchase amounts by month
        Map<Integer, Double> monthlyPurchaseTotals = filteredPurchases.stream()
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getPudate().toLocalDate().getMonthValue(),
                        Collectors.summingDouble(PurchaseList::getTotal)
                ));

        // Clear the existing chart data
        lineChart.getData().clear();

        // Create a series for net profit (Income + Orders - Expenses - Purchases)
        XYChart.Series<String, Number> netProfitSeries = new XYChart.Series<>();
        netProfitSeries.setName("လက်ကျန်ရှိငွေ");



        // Populate the series for each month
        for (int i = 1; i <= 12; i++) {
            // Get the totals for income, orders, expenses, and purchases
            double income = monthlyIncomeTotals.getOrDefault(i, 0.0);
            double order = monthlyOrderTotals.getOrDefault(i, 0.0);
            double expense = monthlyExpenseTotals.getOrDefault(i, 0.0);
            double purchase = monthlyPurchaseTotals.getOrDefault(i, 0.0);


            double netProfit = (income + order) - (expense + purchase);

            if(!(netProfit==0.0)){


                nettotal.setText(convertToMyanmarCurrency(netProfit));

            }


            netProfitSeries.getData().add(new XYChart.Data<>(String.valueOf(i), netProfit));



        }

        // Add the series to the chart
        lineChart.getData().addAll(netProfitSeries);
    }






    private void ini() {

        int year = LocalDate.now().getYear();
        yearPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, year));

        int month = LocalDate.now().getMonthValue();
        monthPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        monthPicker.getValueFactory().setValue(month);



        dashboardbtn.setOnAction(event -> {




            Stage stage = new Stage();


            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/applicationview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("ပင်မ စာမျက်နှာ");
            stage.setScene(scene);
            mainStage.close();
            stage.show();






        });




        showbrandbtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/brandview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("Brand");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

        });

        showcategorybtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/categoryview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("Category");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

        });

        incometypes.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/income_types.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("ဝင်ငွေ အမျိုးအစားများ");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

        });
        exptype_btn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/exp_types.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("အသုံးစရိတ်");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();

        });


        warrantybtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/warrantyview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("Warranty");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();


        });

        paymentbtn.setOnAction(event -> {

            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/paymentview.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) switchPane.getScene().getWindow();
            stage.setTitle("ငွေပေး‌ချေခြင်းပုံစံ");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();



        });

        purchaseListsbtn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/purchasedashboardview.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }




        });

        dailyincome.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/income_daily_view.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }




        });

        orderListbtn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/saledashboardview.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }




        });



        showsupplierbtn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/supplierview.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }

        });

        dailyexpe_btn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/exp_daily_view.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }

        });





        showStockbtn.setOnAction(event -> {

            FXMLLoader fxmlLoader2 = new FXMLLoader(ApplicationViewController.class.getResource("/layout/stockview.fxml"));
            Node node = null;

            try {

                node = fxmlLoader2.load();
                switchPane.getChildren().clear();
                switchPane.getChildren().add(node);



            } catch (IOException e) {

                throw new RuntimeException(e);
            }

        });

    }

}
