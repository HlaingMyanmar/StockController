package org.Generator;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IDGenerate {

    private static int _counter = 1;

    private static int _stockcounter = 1;


    public static String getID(String prefix, String endID) {

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












}
