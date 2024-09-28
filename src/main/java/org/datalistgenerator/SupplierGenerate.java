package org.datalistgenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.databases.Supplierdb;
import org.models.Supplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierGenerate {

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Supplierdb dao = context.getBean("supplierdb", Supplierdb.class);

    public void getSupplierName(ComboBox<String> comboBox, TextField reciveBox){

        ObservableList<String> list = FXCollections.observableArrayList();


        List<Supplier> cList = dao.getAllList();

        Map<String, String> supplierMap = new HashMap<>();

        for (Supplier c : cList) {
            comboBox.getItems().add(c.getSuname());
            supplierMap.put(c.getSuname(), c.getSid());
        }

        comboBox.setOnAction(event -> {

            String selectSupplier  = comboBox.getValue();

            String selectedID =supplierMap.get(selectSupplier);
            reciveBox.setText(selectedID);


        });



    }

}
