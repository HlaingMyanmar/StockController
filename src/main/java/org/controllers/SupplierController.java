package org.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.Alerts.AlertBox;
import org.databases.Supplierdb;
import org.models.Category;
import org.models.Supplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController  implements Initializable {

    @FXML
    private TextArea address;

    @FXML
    private TableColumn<Supplier, String> addressCol;

    @FXML
    private TableColumn<Supplier, String> codeCol;

    @FXML
    private TableColumn<Supplier, String> nameCol;

    @FXML
    private TableColumn<Supplier, String> phoneCol;

    @FXML
    private TableColumn<Supplier, String> remarkCol;

    @FXML
    private TextField code;

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    @FXML
    private TextField remarktxt;

    @FXML
    private Button savebtn;

    @FXML
    private Button exportpdfbtn;

    @FXML
    private Label sizetxt;

    @FXML
    private TableView suppliertable;


    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Supplierdb dao = context.getBean("supplierdb", Supplierdb.class);




    @Override
    public void initialize(URL location, ResourceBundle resources) {



        tabelIni();

        ini();


       suppliertable.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {

            String value = String.valueOf(event.getNewValue());

            if(null!=value && !value.isEmpty()){

                event.getRowValue().setSuname(value);
                Supplier supplier = (Supplier) suppliertable.getSelectionModel().getSelectedItem();


                if(dao.update(supplier)==1){


                    AlertBox.showInformation("‌ထောက်ပံ့သူ", "ပြင်ဆင်ခြင်းအောင်မြင်ပါသည်။");
                    getLoadData(suppliertable);

                }



            }

        });

        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setOnEditCommit(event -> {

            String value = String.valueOf(event.getNewValue());

            if(null!=value && !value.isEmpty()){


                Supplier supplier = (Supplier) suppliertable.getSelectionModel().getSelectedItem();
                event.getRowValue().setSuphone(value);

                if(dao.update(supplier)==1){


                    AlertBox.showInformation("‌ထောက်ပံ့သူ", "ပြင်ဆင်ခြင်းအောင်မြင်ပါသည်။");
                    getLoadData(suppliertable);

                }



            }

        });

        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setOnEditCommit(event -> {

            String value = String.valueOf(event.getNewValue());

            if(null!=value && !value.isEmpty()){

                event.getRowValue().setSuaddress(value);
                Supplier supplier = (Supplier) suppliertable.getSelectionModel().getSelectedItem();


                if(dao.update(supplier)==1){


                    AlertBox.showInformation("‌ထောက်ပံ့သူ", "ပြင်ဆင်ခြင်းအောင်မြင်ပါသည်။");
                    getLoadData(suppliertable);

                }



            }

        });


    }



    private void ini() {


         getLoadData(suppliertable);
        code.setText(getIDGenerate());




        savebtn.setOnAction(event -> {

            String scode = code.getText();
            String sname = name.getText();
            String sphone =phone.getText();
            String saddress = address.getText();
            String saremark= remarktxt.getText();

            Supplier supplier  = new Supplier(scode,sname,sphone,saddress,saremark);

            if(dao.insert(supplier)==1){


                AlertBox.showInformation("‌ထောက်ပံ့သူ", "အသစ်ထည့်သွင်းခြင်းအောင်မြင်ပါသည်။");
                getLoadData(suppliertable);
                code.setText(getIDGenerate());

                name.setText("");
                phone.setText("");
                address.setText("");
                remarktxt.setText("");


            }


        });

        exportpdfbtn.setOnAction(event -> {

            exportToPDF(dao.getAllList(),new Stage());

        });



    }

    private void getLoadData(TableView tableView){




        tableView.getItems().setAll(dao.getAllList());

        sizetxt.setText(String.valueOf(dao.getAllList().size()));

    }

    private void tabelIni(){

        codeCol.setCellValueFactory(new PropertyValueFactory<>("sid"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("suname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("suphone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("suaddress"));
        remarkCol.setCellValueFactory(new PropertyValueFactory<>("remarks"));

    }

    private String getIDGenerate(){


        String text = "#su";

        String lastsupplier =  dao.getAllList().get(0).getSid();


        return text+(Integer.parseInt(lastsupplier.substring(3))+1);

    }

    private void exportToPDF(List<Supplier> SupplierList, Stage stage) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));


        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {

            Document document = new Document();

            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();


                Paragraph title = new Paragraph("Supplier List Export" + ": " + LocalDate.now());

                title.setSpacingBefore(20f);
                title.setSpacingAfter(20f);
                title.setLeading(1.5f * 12f);


                document.add(title);


                PdfPTable table = getPdfPTable(SupplierList);


                document.add(table);

                document.close();
                AlertBox.showInformation("PDF", "PDF created successfully!");

            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private static PdfPTable getPdfPTable(List<Supplier> SupplierList) {
        PdfPTable table = new PdfPTable(5);




        table.addCell("Code");
        table.addCell("Name");
        table.addCell("phone");
        table.addCell("Address");
        table.addCell("Remark");


        for (Supplier supplier : SupplierList) {
            table.addCell(supplier.getSid());
            table.addCell(supplier.getSuname());
            table.addCell(supplier.getSuphone());
            table.addCell(supplier.getSuaddress());
            table.addCell(supplier.getRemarks());

        }
        return table;
    }

}
