package org.expense_options;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.Alerts.AlertBox;
import org.controllers.ApplicationViewController;
import org.models.PurchaseList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private LineChart<String, Number> barChart;

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
    private Button exportpdf;



    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Exp_Viewdb expViewdb = context.getBean("exp_viewdb", Exp_Viewdb.class);

    Exp_Typesdb expTypedb = context.getBean("exp_typesdb",Exp_Typesdb.class);



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        expensetable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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

        exportpdf.setOnAction(event -> {

            ObservableList<Exp_View>expViews = expensetable.getSelectionModel().getSelectedItems();
            exportToPDF(expViews,new Stage());


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

            createChart(purchaseLists);
            createLineChart(purchaseLists);
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

            createChart(purchaseLists);
            createLineChart(purchaseLists);
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
            createChart(purchaseLists);
            createLineChart(purchaseLists);

            getAmount(purchaseLists,lbTotal);
            getQtyItem(purchaseLists,lbCount);


        });




    }

    private void ini() {

        expensetable.setItems(getList());
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

    private ObservableList<Exp_View> getList(){

        LocalDate today = LocalDate.now();

        List<Exp_View> expViewList = expViewdb.getAllListView();
        ObservableList<Exp_View> purchaseLists = FXCollections.observableArrayList();

        purchaseLists.addAll(expViewList .stream()
                .filter(purchaseList -> purchaseList.getExpense_date().toLocalDate().equals(today))
                .toList());

        return purchaseLists;

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

    private void createChart(ObservableList<Exp_View> list) {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        list.stream()
                .collect(Collectors.groupingBy(
                        Exp_View::getCategory_name,
                        Collectors.summingInt(Exp_View::getTotal)))  // Summing up the 'total' field
                .forEach((category, total) -> {
                    pieChartData.add(new PieChart.Data(category+"\n"+total+" MMK", total));
                });

        pieChart.setData(pieChartData);

    }

    private void createLineChart(ObservableList<Exp_View> list) {

        XYChart.Series<String, Number> series = new XYChart.Series<>();


        list.stream()
                .collect(Collectors.groupingBy(
                        Exp_View::getCategory_name,
                        Collectors.summingInt(Exp_View::getTotal)))
                .forEach((category, total) -> {

                    series.getData().add(new XYChart.Data<>(category, total));
                });


        barChart.getData().clear();
        barChart.getData().add(series);
    }



    private void exportToPDF(List<Exp_View> categoryList, Stage stage) {

        Document document = new Document(PageSize.A4.rotate());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {



            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();


                BaseFont unicodeFont = BaseFont.createFont(getClass().getResource("/fonts/Pyidaungsu-2.5.3_Regular.ttf").toURI().getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

                Font font = new Font(unicodeFont, 10, Font.NORMAL);

                Paragraph title = new Paragraph("Expense List Export: " + LocalDate.now().getMonth(), font);
                title.setSpacingBefore(20f);
                title.setSpacingAfter(20f);
                title.setLeading(1.5f * 12f);

                document.add(title);

                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);

                // Adding headers with the Burmese-compatible font
                Stream.of("Code", "Date", "Expense Type", "Total", "Description", "Created_At", "Updated_At")
                        .forEach(columnTitle -> {
                            PdfPCell header = new PdfPCell(new Phrase(columnTitle, font));
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(header);
                        });

                // Adding data rows
                for (Exp_View p : categoryList) {
                    table.addCell(new Phrase(p.getExpense_id(), font));
                    table.addCell(new Phrase(p.getExpense_date().toLocalDate().toString(), font));
                    table.addCell(new Phrase(p.getCategory_name(), font));
                    table.addCell(new Phrase(String.valueOf(p.getTotal()), font));
                    table.addCell(new Phrase(p.getDescription(), font));
                    table.addCell(new Phrase(String.valueOf(p.getCreated_at()), font));
                    table.addCell(new Phrase(String.valueOf(p.getUpdated_at()), font));
                }

                document.add(table);
                document.close();
                AlertBox.showInformation("PDF", "PDF created successfully!");

            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }






}
