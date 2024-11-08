package org.expense_options;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controllers.ApplicationViewController;
import org.models.PurchaseList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static org.Generator.Currency.convertToMyanmarCurrency;

public class Exp_ViewController implements Initializable {

    @FXML
    private TableColumn<Exp_View, Integer> amountCol;


    @FXML
    private Spinner<Integer> dayPicker;

    @FXML
    private Spinner<Integer> monthPicker;

    @FXML
    private Spinner<Integer> yearPicker;

    @FXML
    private LineChart<?, ?> barChart;

    @FXML
    private TableColumn<Exp_View, String> catCol;

    @FXML
    private TableColumn<Exp_View, String> codeCol;

    @FXML
    private TableColumn<Exp_View, Timestamp> createCol;

    @FXML
    private TableColumn<Exp_View, Date> dateCol;

    @FXML
    private TableColumn<Exp_View, Timestamp> updateCol;


    @FXML
    private TableColumn<Exp_View, String> descCol;

    @FXML
    private TableView expensetable;

    @FXML
    private Label lbCount;

    @FXML
    private Label lbTotal;


    @FXML
    private Button newExpbtn;

    @FXML
    private PieChart pieChart;

    @FXML
    private TextField searchtxt;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Exp_Viewdb expViewdb = context.getBean("exp_viewdb", Exp_Viewdb.class);

    Exp_Typesdb expTypedb = context.getBean("exp_typesdb",Exp_Typesdb.class);



    @Override
    public void initialize(URL location, ResourceBundle resources) {




        ini();
        actionEvent();

        tableini();



    }

    private void tableini() {

        codeCol.setCellValueFactory(new PropertyValueFactory<>("expense_id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("expense_date"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("category_name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        createCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateCol.setCellValueFactory(new PropertyValueFactory<>("updated_at"));



    }

    private void actionEvent() {



        newExpbtn.setOnAction(event -> {


            Stage stage = new Stage();



            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationViewController.class.getResource("/layout/exp_newExp.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) expensetable.getScene().getWindow();
            stage.setTitle("အသုံးစရိတ်");
            stage.initOwner(mainStage);
            stage.setScene(scene);
            stage.show();


            stage.setOnCloseRequest(event1 -> {


                ini();

            });




        });

        dayPicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<Exp_View> expViewList = expViewdb.getAllListView();
            ObservableList<Exp_View> purchaseLists = FXCollections.observableArrayList();

            purchaseLists.addAll(expViewList
                    .stream()
                    .filter(expList ->expList .getExpense_date().toLocalDate().getYear()==yearPicker.getValue())
                    .filter(expList ->expList.getExpense_date().toLocalDate().getMonthValue()==monthPicker.getValue())
                    .filter(expList ->expList.getExpense_date().toLocalDate().getDayOfMonth()==newValue)
                    .toList());

            expensetable.setItems(purchaseLists);


            getAmount(purchaseLists,lbTotal);
            getQtyItem(purchaseLists,lbCount);



        });

        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            List<Exp_View> expViewList = expViewdb.getAllListView();
            ObservableList<Exp_View> purchaseLists = FXCollections.observableArrayList();

            purchaseLists.addAll(expViewList
                    .stream()
                    .filter(expList ->expList .getExpense_date().toLocalDate().getYear()==yearPicker.getValue())
                    .filter(expList ->expList .getExpense_date().toLocalDate().getMonthValue()==newValue)
                    .toList());

            expensetable.setItems(purchaseLists);


            getAmount(purchaseLists,lbTotal);
            getQtyItem(purchaseLists,lbCount);




        });

        yearPicker.valueProperty().addListener((observable, oldValue, newValue)  -> {

            List<Exp_View> expViewList = expViewdb.getAllListView();
            ObservableList<Exp_View> purchaseLists = FXCollections.observableArrayList();

            purchaseLists.addAll(expViewList
                    .stream()
                    .filter(expList ->expList .getExpense_date().toLocalDate().getYear()==newValue)
                    .toList());

            expensetable.setItems(purchaseLists);

            getAmount(purchaseLists,lbTotal);
            getQtyItem(purchaseLists,lbCount);


        });




    }

    private void ini() {

        int day = LocalDate.now().getDayOfMonth();
        dayPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, day));

        int year = LocalDate.now().getYear();
        yearPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, year));

        int month = LocalDate.now().getMonthValue();
        monthPicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        monthPicker.getValueFactory().setValue(month);








    }

    private void getAmount(ObservableList<Exp_View> list,Label label){

        int sum =   list.stream()
                .mapToInt(Exp_View::getTotal)
                .sum();
        label.setText(convertToMyanmarCurrency(sum));

    }
    private void getQtyItem(List<Exp_View> list,Label label){

        label.setText(String.valueOf(list.size()));

    }




}
