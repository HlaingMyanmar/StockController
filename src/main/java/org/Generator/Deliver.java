package org.Generator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.DAO.DataAccessObject;
import org.databases.Branddb;
import org.databases.Categorydb;
import org.databases.Stockdb;
import org.models.Brand;
import org.models.Category;
import org.models.Stock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deliver implements Generatorass{

    private static int _counter = 1;

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    DataAccessObject<Brand> branddb = context.getBean("brandb", Branddb.class);
    Stockdb stockdb = context.getBean("stockdb", Stockdb.class);
    Categorydb categorydb = context.getBean("categorydb", Categorydb.class);



    protected ObservableList<String> getBrandNameList() {

        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll( branddb.getAllList().stream()
                .map(Brand::getBname)
                .toList());

        return list;

    }
    protected ObservableList<String> getBrandCodeList() {

        ObservableList<String> list = FXCollections.observableArrayList();

        list.addAll( branddb.getAllList().stream()
                .map(Brand::getBid)
                .toList());

        return list;

    }

    protected String getBrandCode(String brandname){


        return branddb.getAllList().stream()
                .filter(brand -> brand.getBname().equals(brandname))
                .map(Brand::getBid)
                .findFirst()
                .orElse(null);

    }

    protected String getCategoryName(String brandcode){


        return categorydb.getAllList().stream()
                .filter(category -> category.getCid().equals(brandcode))
                .map(Category::getCname)
                .findFirst()
                .orElse(null);

    }

    protected String getCategoryCode(String categoryname){


        return categorydb.getAllList().stream()
                .filter(category -> category.getCname().equals(categoryname))
                .map(Category::getCid)
                .findFirst()
                .orElse(null);

    }

    protected String getBrandName(String brandcode){


        return branddb.getAllList().stream()
                .filter(brand -> brand.getBid().equals(brandcode))
                .map(Brand::getBname)
                .findFirst()
                .orElse(null);

    }



    protected static String getID(String prefix, String endID) {

        String currentDate = null;

        try {

            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            currentDate = now.format(formatter);


            if (currentDate.equals(endID.substring(3, 11))) {

                _counter = Integer.parseInt(endID.substring(12));

                _counter++;

            }

        } catch (NullPointerException | StringIndexOutOfBoundsException e_) {

            _counter =1;

            return prefix + "-" + currentDate + "-" + _counter;

        }


        return prefix + "-" + currentDate + "-" + _counter;
    }

    protected String getDateFormat(){


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = LocalDate.now().format(formatter);

        return formattedDate;
    }

    protected Stock getStockList (String code){

        return  stockdb.getAllList().stream()
                .filter(stock -> stock.getStockcode().equals(code))
                .findFirst()
                .orElse(null);



    }
}
