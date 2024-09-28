package org.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.Alerts.AlertBox;
import org.databases.Categorydb;
import org.models.Category;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;




public class CategorController implements Initializable {

    @FXML
    private Button addbtn;

    @FXML
    private Button exportpdfbtn;

    @FXML
    private TableView categoryreftable;

    @FXML
    private TableView categorytable;

    @FXML
    private TableColumn<Category, String> cidCol;

    @FXML
    private TableColumn<Category, String> cnameCol;

    @FXML
    private TableColumn<Category, String> cnameeCol;

    @FXML
    private TextField code;

    @FXML
    private TableColumn<Category, String> crefCol;

    @FXML
    private TextField name;

    @FXML
    private ComboBox<String> refcombo;

    @FXML
    private TextField searchtxt;

    @FXML
    private TextField sizereftxt;

    @FXML
    private TextField sizetxt;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Categorydb dao = context.getBean("categorydb", Categorydb.class);

    private static int _stockcounter = 1;

    public static  String _cacode = null;

    public static  Boolean _status = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        tabelIni();

        code.setText(getCategoryID());
        getEventAction();

        getCategoryList(refcombo);

        categorytable.setEditable(true);
        categoryreftable.setEditable(true);



        ini();






    }

    private void tabelIni() {

        cidCol.setCellValueFactory(new PropertyValueFactory<>("cid"));
        cnameCol.setCellValueFactory(new PropertyValueFactory<>("cname"));
        cnameeCol.setCellValueFactory(new PropertyValueFactory<>("cname"));


    }


    private void ini() {



        ObservableList<Category> observableList  = FXCollections.observableArrayList();

        observableList.addAll(dao.getAllList());

        FilteredList<Category> filteredData = new FilteredList<>(observableList, b -> true);

        addTextFieldListener(searchtxt,filteredData );


        SortedList<Category> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(categorytable.comparatorProperty());

        categorytable.setItems(sortedData);

        sizetxt.setText(String.valueOf(sortedData.size()));

        categoryreftable.setOnMouseClicked(event -> {


            if(event.getClickCount()==2 && _status.equals(true)){

                Category category = (Category) categoryreftable.getSelectionModel().getSelectedItem();
                _cacode =category.getCid();
                Stage mainStage = (Stage) sizereftxt.getScene().getWindow();
                mainStage.close();

            }
            else if(event.getClickCount()==2 && _status.equals(false)){


                getRowUpdate2(cnameeCol);




            }

        });










    }


    private void getCategoryList(ComboBox<String> comboBox){

        ObservableList<String> observableList  = FXCollections.observableArrayList();

        observableList.addAll(dao.getAllList().stream()
                .map(Category::getCname)
                .collect(Collectors.toList()));
        comboBox.setItems(observableList);



    }

    private String getCategoryCode(String name){


        List<Category> cList = dao.getAllList();


        return cList.stream()
                .filter(c -> c.getCname().equals(name))
                .map(Category::getCid)
                .findFirst()
                .orElse(null);


    }

    private void addTextFieldListener (TextField textField, FilteredList<Category> filtereData){

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtereData.setPredicate(category -> {
                if (newValue == null || newValue.isEmpty()) {
                    sizetxt.setText(String.valueOf(filtereData.size()+1));
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(category.getCid().toLowerCase().contains(lowerCaseFilter)){

                    sizetxt.setText(String.valueOf(filtereData.size()));

                    return  true;
                }
                else if(category.getCname().toLowerCase().contains(lowerCaseFilter)){

                    sizetxt.setText(String.valueOf(filtereData.size()));

                    return  true;
                }


                sizetxt.setText(String.valueOf(filtereData.size()));


                return false;
            });


        });
    }

    private String getCategoryID(){

        String text = "#Cat";


        try {

            String st = dao.getAllListforCIDD().get(0).getCid();

            return getStockIDGenerate(text, st);

        }catch (IndexOutOfBoundsException e){

            return  getStockIDGenerate(text,dao.getAllList().get(dao.getAllList().size()-1).getCid());

        }

    }

    private void getEventAction(){

        categorytable.setOnMouseClicked(event -> {


            Category category = (Category) categorytable.getSelectionModel().getSelectedItem();

            ObservableList<Category> observableList  = FXCollections.observableArrayList();

            observableList.addAll(dao.getAllListforCIDD().stream()
                    .filter(cate->category.getCid().equals(cate.getCidd()))
                    .collect(Collectors.toList()));

            categoryreftable.setItems(observableList);
            sizereftxt.setText(String.valueOf(observableList.size()));





                if(event.getClickCount()==2 && _status.equals(true)){


                    _cacode =category.getCid();
                    Stage mainStage = (Stage) sizereftxt.getScene().getWindow();
                    mainStage.close();

                }
                else if(event.getClickCount()==2 && _status.equals(false)){

                    getRowUpdate(cnameCol);





                }





        });

        addbtn.setOnAction(event -> {


           String categorycode =  code.getText();
           String categoryname=  name.getText();
           String categoryref =refcombo.getValue();

           if(code.getText().isEmpty() || name.getText().isEmpty() ){

               AlertBox.showWarning("Notice","Please Insert Required Data !!!!");

           }

           Category category = new Category(categorycode,categoryname,getCategoryCode(categoryref));

           if(dao.insert(category)==1){

               AlertBox.showInformation("Category Options"," New Add Data Successful");
               ini();
               getCategoryList(refcombo);
               code.setText(getCategoryID());
               name.setText("");
               refcombo.setValue("");
           }
           else {

               AlertBox.showError("Category Options"," New Add Data Is not Successful");

           }








        });

        exportpdfbtn.setOnAction(event -> {



            exportToPDF(dao.getAllList(),new Stage());



        });

    }

    private void exportToPDF(List<Category> categoryList, Stage stage) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));


        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {

            Document document = new Document();

            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();


                Paragraph title = new Paragraph("Category List Export"+": "+ LocalDate.now());

                title.setSpacingBefore(20f);
                title.setSpacingAfter(20f);
                title.setLeading(1.5f * 12f);


                document.add(title);



                PdfPTable table = new PdfPTable(2);


                table.addCell("ID");
                table.addCell("Name");



                for (Category category : categoryList) {
                    table.addCell(String.valueOf(category.getCid()));
                    table.addCell(category.getCname());

                }


                document.add(table);

                document.close();
                AlertBox.showInformation("PDF","PDF created successfully!");

            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getStockIDGenerate(String prefix,String endID) {

        String currentDate = null;

        try {

            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            currentDate = now.format(formatter);


            if (currentDate.equals(endID.substring(5, 13))) {

                _stockcounter = Integer.parseInt(endID.substring(14));

                _stockcounter++;

            }

        } catch (NullPointerException | StringIndexOutOfBoundsException e_) {

            _stockcounter =1;

            return prefix + "-" + currentDate + "-" + _stockcounter;

        }


        return prefix + "-" + currentDate + "-" + _stockcounter;

    }

    private void getRowUpdate(TableColumn<Category,String> tableColumn){

        getableColumn(tableColumn, categorytable);

    }

    private void getRowUpdate2(TableColumn<Category,String> tableColumn){

        getableColumn(tableColumn, categoryreftable);

    }

    private void getableColumn(TableColumn<Category, String> tableColumn, TableView categoryreftable) {
        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableColumn.setOnEditCommit(event -> {

            String value = String.valueOf(event.getNewValue());

            event.getRowValue().setCname(value);

            Category category = (Category) categoryreftable.getSelectionModel().getSelectedItem();

            if(dao.update(category)==1){


                AlertBox.showInformation("Category", "Edit Successful");

                ini();

            }

        });
    }


}
