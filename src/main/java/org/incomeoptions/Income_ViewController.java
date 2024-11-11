package org.incomeoptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.Alerts.AlertBox;
import org.controllers.ApplicationViewController;
import org.expense_options.Exp_Types;
import org.expense_options.Exp_View;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLData;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.Generator.Currency.convertToMyanmarCurrency;

public class Income_ViewController implements Initializable {

    @FXML
    private TableColumn<Income_View, Integer> amountCol;

    @FXML
    private LineChart<String, Number> barChart;

    @FXML
    private TableColumn<Income_View, String > catCol;

    @FXML
    private TableColumn<Income_View, String> codeCol;

    @FXML
    private TableColumn<Income_View, Timestamp> createCol;

    @FXML
    private TableColumn<Income_View, Date> dateCol;

    @FXML
    private Spinner<Integer> dayPicker;

    @FXML
    private TableColumn<Income_View, String> descCol;

    @FXML
    private TableView incometable;

    @FXML
    private Button exportpdf;

    @FXML
    private Label lbCount;

    @FXML
    private Label lbTotal;

    @FXML
    private Spinner<Integer> monthPicker;

    @FXML
    private Button newExpbtn;

    @FXML
    private TableColumn<Income_View, String> payCol;

    @FXML
    private PieChart pieChart;

    @FXML
    private TableColumn<Income_View, Timestamp> updateCol;

    @FXML
    private Spinner<Integer> yearPicker;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);

    Income_Viewdb incomeViewdb = context.getBean("income_viewdb",Income_Viewdb.class);

    Incometypedb incometypedb = context.getBean("incomedb",Incometypedb.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        ini();
        tableIni();
        actionEvent();

    }

    private void actionEvent() {

        incometable.setEditable(true);


        newExpbtn.setOnAction(event -> {



            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/income_newIn.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) incometable.getScene().getWindow();
            stage.setTitle("ဝင်ငွေ");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();


            stage.setOnCloseRequest(event1 -> {


                ini();

            });






        });
        incometable.setOnMouseClicked(event -> {


            if(event.getClickCount()==2){

                getRowUpdate(catCol);
                getRowUpdate(descCol);
                getRowUpdateInteger(amountCol);
                getRowUpdate(payCol);


            }






        });

        dayPicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<Income_View> incomeViews = incomeViewdb.getAllList();
            ObservableList<Income_View> incomeLists = FXCollections.observableArrayList();

            incomeLists.addAll(incomeViews
                    .stream()
                    .filter(inList->inList .getIncome_date().toLocalDate().getYear()==yearPicker.getValue())
                    .filter(inList->inList .getIncome_date().toLocalDate().getMonthValue()==monthPicker.getValue())
                    .filter(inList->inList .getIncome_date().toLocalDate().getDayOfMonth()==newValue)
                    .toList());

            incometable.setItems(incomeLists);

            createChart(incomeLists);
            createLineChart(incomeLists);
            getAmount(incomeLists,lbTotal);
            getQtyItem(incomeLists,lbCount);



        });

        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<Income_View> incomeViews = incomeViewdb.getAllList();
            ObservableList<Income_View> incomeLists = FXCollections.observableArrayList();

            incomeLists.addAll(incomeViews
                    .stream()
                    .filter(inList->inList .getIncome_date().toLocalDate().getYear()==yearPicker.getValue())
                    .filter(inList->inList .getIncome_date().toLocalDate().getMonthValue()==newValue)
                    .toList());

            incometable.setItems(incomeLists);

            createChart(incomeLists);
            createLineChart(incomeLists);
            getAmount(incomeLists,lbTotal);
            getQtyItem(incomeLists,lbCount);




        });

        yearPicker.valueProperty().addListener((observable, oldValue, newValue)  -> {

            List<Income_View> incomeViews = incomeViewdb.getAllList();
            ObservableList<Income_View> incomeLists = FXCollections.observableArrayList();

            incomeLists.addAll(incomeViews
                    .stream()
                    .filter(inList->inList .getIncome_date().toLocalDate().getYear()==newValue)
                    .toList());

            incometable.setItems(incomeLists);

            createChart(incomeLists);
            createLineChart(incomeLists);
            getAmount(incomeLists,lbTotal);
            getQtyItem(incomeLists,lbCount);


        });


    }

    private void tableIni() {


        codeCol.setCellValueFactory(new PropertyValueFactory<>("income_id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("income_date"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("income_name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        payCol.setCellValueFactory(new PropertyValueFactory<>("paymentmethod"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        createCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateCol.setCellValueFactory(new PropertyValueFactory<>("updated_at"));



    }

    private void ini() {

        incometable.setItems(getList());

        getAmount(getList(),lbTotal);
        getQtyItem(getList(),lbCount);

        createChart(getList());
        createLineChart(getList());




        int day = LocalDate.now().getDayOfMonth();
        dayPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, day));

        int year = LocalDate.now().getYear();
        yearPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, year));

        int month = LocalDate.now().getMonthValue();
        monthPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        monthPicker.getValueFactory().setValue(month);




    }

    private void createChart(ObservableList<Income_View> list) {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        list.stream()
                .collect(Collectors.groupingBy(
                       Income_View::getIncome_name,
                        Collectors.summingInt(Income_View::getAmount)))  // Summing up the 'total' field
                .forEach((category, total) -> {
                    pieChartData.add(new PieChart.Data(category+"\n"+total+" MMK", total));
                });

        pieChart.setData(pieChartData);

    }

    private void createLineChart(ObservableList<Income_View> list) {

        XYChart.Series<String, Number> series = new XYChart.Series<>();


        list.stream()
                .collect(Collectors.groupingBy(
                       Income_View::getIncome_name,
                        Collectors.summingInt(Income_View::getAmount)))
                .forEach((category, total) -> {

                    series.getData().add(new XYChart.Data<>(category, total));
                });


        barChart.getData().clear();
        barChart.getData().add(series);
    }

    private ObservableList<Income_View> getList(){

        LocalDate today = LocalDate.now();

        List<Income_View> incomeViews = incomeViewdb.getAllList();
        ObservableList<Income_View> incomeLists = FXCollections.observableArrayList();

        incomeLists.addAll(incomeViews.stream()
                .filter(incomeView -> incomeView.getIncome_date().toLocalDate().equals(today))
                .toList());

        return incomeLists;

    }

    private void getAmount(ObservableList<Income_View> list,Label label){

        int sum =   list.stream()
                .mapToInt(Income_View::getAmount)
                .sum();
        label.setText(convertToMyanmarCurrency(sum));

    }
    private void getQtyItem(List<Income_View> list,Label label){

        label.setText(String.valueOf(list.size()));

    }

    private ObservableList<String> getIncomeTypesList(){

        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll(incometypedb .getAllList().stream()
                .map(Incometype::getCat_name)
                .toList());

        return list;

    }

    private ObservableList<String> getPayTypeList(){

        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll(paymentdb.getAllList().stream()
                .map(Payment::getPaymethodname)
                .toList());

        return list;

    }

    private int getIncomeTypeName(String type){


        return  incometypedb.getAllList().stream()
                .filter(expTypes -> expTypes.getCat_name().equals(type))
                .map(Incometype::getCat_id)
                .findFirst().orElse(-1);


    }

    private int getPayId(String payname){

        return paymentdb.getAllList().stream()
                .filter(payment -> payment.getPaymethodname().equals(payname))
                .map(Payment::getPayid)
                .findFirst().orElse(-1);

    }

    private void getRowUpdate(TableColumn<Income_View, String> tableColumn) {
        getableColumn(tableColumn, incometable);
    }

    private void getRowUpdateInteger(TableColumn<Income_View, Integer> tableColumn) {
        getableColumnInteger(tableColumn, incometable);
    }

    private void getableColumn(TableColumn<Income_View, String> tableColumn,TableView<Exp_Types> exptypetable) {



        JComboBox<String> comboBox = new JComboBox<>(getIncomeTypesList().toArray(new String[0]));
        JComboBox<String> paybox = new JComboBox<>(getPayTypeList().toArray(new String[0]));

        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableColumn.setOnEditCommit(event -> {


            if (Objects.equals(tableColumn.getText(), "အမျိုးအစား")) {


                int result = JOptionPane.showConfirmDialog(null, comboBox, "ဝင်ငွေ အမျိုးအစား တစ်ခုရွေးချယ်ပါ။", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);


                if (result == JOptionPane.OK_OPTION) {


                    String comboxdata = (String) comboBox.getSelectedItem();


                    String value = String.valueOf(event.getNewValue());
                    Income_View incomeView = event.getRowValue();


                    event.getRowValue().setIncome_name(value);

                   Income_View updateView = new Income_View(

                            incomeView.getIncome_id(),
                            incomeView.getIncome_date(),
                            getIncomeTypeName(comboxdata),
                            incomeView.getAmount(),
                            getPayId(incomeView.getPaymentmethod()),
                            incomeView.getDescription(),
                            incomeView.getCreated_at(),
                            new Timestamp(System.currentTimeMillis())

                    );

                    if (incomeViewdb.update(updateView) == 1) {

                        AlertBox.showInformation("ဝင်ငွေ", "ဝင်ငွေ အမျိုးအစားကို အောင်မြင်စွာပြုပြင် ပြီးပါပြီ။");

                        ini();


                    }


                }
            } else if (Objects.equals(tableColumn.getText(), "အကြောင်းအရာ")) {

                String value = String.valueOf(event.getNewValue());
                Income_View incomeView = event.getRowValue();


                event.getRowValue().setDescription(value);

                Income_View updateView = new Income_View(

                        incomeView.getIncome_id(),
                        incomeView.getIncome_date(),
                        incomeView.getDescription(),
                        new Timestamp(System.currentTimeMillis())
                );

                if (incomeViewdb.updateDesc(updateView) == 1) {

                    AlertBox.showInformation("ဝင်ငွေ", "ဝင်ငွေ အကြောင်းအရာကို အောင်မြင်စွာပြုပြင် ပြီးပါပြီ။");

                    ini();


                }

            }
            else if (Objects.equals(tableColumn.getText(), "ငွေပေးချေခြင်း")) {


                int result = JOptionPane.showConfirmDialog(null, paybox, "ငွေပေးချေခြင်း အမျိုးအစား တစ်ခုရွေးချယ်ပါ။", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);


                if (result == JOptionPane.OK_OPTION) {



                    String comboxdata = (String) paybox.getSelectedItem();

                    String paymethodmethodname = event.getOldValue();


                    String value = String.valueOf(event.getNewValue());
                    Income_View incomeView = event.getRowValue();


                    event.getRowValue().setPaymentmethod(value);

                    System.out.println(getPayId(comboxdata));



                    Income_View updateView = new Income_View(

                            incomeView.getIncome_id(),
                            getPayId(comboxdata)


                    );

                    if (incomeViewdb.paymentupdate(updateView) == 1) {

                        if(paymentdb.subAmount(new Payment(getPayId(paymethodmethodname),incomeView.getAmount()))==1 && paymentdb.sumAmount(new Payment( updateView.getPayid(),incomeView.getAmount()))==1) {

                            AlertBox.showInformation("ဝင်ငွေ", "ဝင်ငွေ အမျိုးအစားကို အောင်မြင်စွာပြုပြင် ပြီးပါပြီ။");

                            ini();

                        }

                    }


                }
            }





        });






    }

    private void getableColumnInteger(TableColumn<Income_View, Integer> tableColumn,TableView<Incometype> exptypetable) {


        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));

        tableColumn.setOnEditCommit(event -> {



            if (Objects.equals(tableColumn.getText(), "ပမာဏ")) {

                    int oldamount = event.getOldValue();
                    int newamount = event.getNewValue();

                    Income_View incomeView = event.getRowValue();


                    event.getRowValue().setAmount(newamount);



                    Income_View updateView = new Income_View(

                            incomeView.getIncome_id(),
                            incomeView.getAmount(),
                            getPayId(incomeView.getPaymentmethod()),
                            new Timestamp(System.currentTimeMillis())

                    );



                    if (new IncomeServices().useIncomeUpdateAmount(updateView, oldamount)) {

                        AlertBox.showInformation("ဝင်ငွေ", "ဝင်ငွေ ပမမာဏ ကိုအောင်မြင်စွာပြုပြင် ပြီးပါပြီ။");

                        ini();


                    }



            }

        });






    }
}
