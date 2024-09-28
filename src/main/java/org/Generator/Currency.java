package org.Generator;

public class Currency {

    public static String convertToMyanmarCurrency(double amount) {
        long kyat = (long) amount;
        long lan = kyat / 100000;
        kyat = kyat % 100000;
        long thousandTen = kyat / 10000;
        kyat = kyat % 10000;
        long thousand = kyat / 1000;
        kyat = kyat % 1000;
        long hundred = kyat / 100;
        kyat = kyat % 100;

        StringBuilder result = new StringBuilder();

        if (lan > 0) {
            result.append(lan).append(" သိန်း ");
        }
        if (thousandTen > 0) {
            result.append(thousandTen).append(" သောင်း ");
        }
        if (thousand > 0) {
            result.append(thousand).append(" ထောင် ");
        }
        if (hundred > 0) {
            result.append(hundred).append(" ရာ ");
        }
        result.append(kyat).append(" ကျပ်");

        return result.toString();
    }



}
