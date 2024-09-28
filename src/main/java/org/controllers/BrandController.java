package org.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.Alerts.AlertBox;
import org.DAO.DataAccessObject;
import org.databases.Branddb;
import org.models.Brand;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BrandController implements Initializable {

    @FXML
    private TableView brandtable;

    @FXML
    private TextField code;

    @FXML
    private TableColumn<Brand, String> codeCol;

    @FXML
    private TextField name;

    @FXML
    private TableColumn<Brand, String> nameCol;

    @FXML
    private Button savebtn;

    @FXML
    private TextField searchtxt;

    @FXML
    private TextField sizetxt;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    DataAccessObject<Brand> dao = context.getBean("brandb", Branddb.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ini();
        tabelIni();
        code.setText(getBrandID());
        getActionEvent();

        brandtable.setEditable(true);

    }

    private void ini() {

        ObservableList<Brand> observableList  = FXCollections.observableArrayList();

        observableList.addAll(dao.getAllList());

        FilteredList<Brand> filteredData = new FilteredList<>(observableList, b -> true);

        addTextFieldListener(searchtxt,filteredData );


        SortedList<Brand> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(brandtable.comparatorProperty());

        brandtable.setItems(sortedData);

        sizetxt.setText(String.valueOf(sortedData.size()));

    }

    private void addTextFieldListener (TextField textField,FilteredList<Brand> filtereData){

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtereData.setPredicate(brand -> {
                if (newValue == null || newValue.isEmpty()) {
                    sizetxt.setText(String.valueOf(filtereData.size()+1));
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(brand.getBid().toLowerCase().contains(lowerCaseFilter)){

                    sizetxt.setText(String.valueOf(filtereData.size()));

                    return  true;
                }
                else if(brand.getBname().toLowerCase().contains(lowerCaseFilter)){

                    sizetxt.setText(String.valueOf(filtereData.size()));

                    return  true;
                }

                sizetxt.setText(String.valueOf(filtereData.size()));


                return false;
            });


        });
    }

    private void tabelIni(){


        codeCol.setCellValueFactory(new PropertyValueFactory<>("bid"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("bname"));

    }

    private  String getBrandID(){


        String text = "#br"+LocalDate.now();

        try {

            String st = dao.getAllList().get(0).getBid();

            return text + "_"+(Integer.parseInt(st.substring(14)) + 1);

        }catch (IndexOutOfBoundsException e){

            return "#br"+LocalDate.now()+"_"+1;

        }


    }

    private void getActionEvent(){


        savebtn.setOnAction(event -> {

           String brandcode =  code.getText();
           String brandname = name.getText();

           Brand brand = new Brand(brandcode,brandname);

           if(dao.insert(brand)==1){

               AlertBox.showInformation("Brand Options"," New Add Data Successful");
               name.setText("");
               code.setText(getBrandID());
               ini();

           }


        });


        getRowUpdate(nameCol);

    }

    private void getRowUpdate(TableColumn<Brand,String> tableColumn){

        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableColumn.setOnEditCommit(event -> {

            String value = String.valueOf(event.getNewValue());

            event.getRowValue().setBname(value);

            Brand brand = (Brand) brandtable.getSelectionModel().getSelectedItem();

            if(dao.update(brand)==1){


                AlertBox.showInformation("Brand", "Edit Successful");

                ini();

            }

        });

    }





}
