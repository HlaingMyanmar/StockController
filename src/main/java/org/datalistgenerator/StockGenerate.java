package org.datalistgenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.Alerts.AlertBox;
import org.databases.Stockdb;
import org.models.Stock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StockGenerate {

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Stockdb dao = context.getBean("stockdb", Stockdb.class);


    private static int _stockcounter = 1;


    protected void exportToPDF(List<Stock> stockList, Stage stage) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));


        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {

            Document document = new Document();
           // Document document = new Document(new com.itextpdf.text.Rectangle(595, 842), 36, 36, 50, 50);

            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();


                Paragraph title = new Paragraph("Stock List Export" + ": " + LocalDate.now());

                title.setSpacingBefore(20f);
                title.setSpacingAfter(20f);
                title.setLeading(1.5f * 12f);


                document.add(title);


                PdfPTable table = getPdfPTable(stockList);
                table.setWidthPercentage(100);

                float[] columnWidths = {1f, 2f, 2f, 1f, 1f, 1f, 1.5f}; // Adjust as needed
                table.setWidths(columnWidths);




                document.add(table);

                document.close();
                AlertBox.showInformation("PDF", "PDF created successfully!");

            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private static PdfPTable getPdfPTable(List<Stock> stockList) {

        PdfPTable table = new PdfPTable(7);


        Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);
        Font titlefont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);

        table.addCell(createCenterAlignedCell("Code", titlefont));
        table.addCell(createCenterAlignedCell("Description", titlefont));
        table.addCell(createCenterAlignedCell("Category", titlefont));
        table.addCell(createCenterAlignedCell("Brand", titlefont));
        table.addCell(createCenterAlignedCell("Qty", titlefont));
        table.addCell(createCenterAlignedCell("Price", titlefont));
        table.addCell(createCenterAlignedCell("Total", titlefont));


        for (Stock stock : stockList) {
            table.addCell(createCell(stock.getStockcode(), font));
            table.addCell(createCell(stock.getStockname(), font));
            table.addCell(createCell(stock.getCcode(), font));
            table.addCell(createCell(stock.getBid(), font));
            table.addCell(createCell(String.valueOf(stock.getQty()), font));
            table.addCell(createCell(String.valueOf(stock.getPrice()), font));
            table.addCell(createCell(String.valueOf(stock.getTotal()), font));
        }

        return table;
    }
    private static PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5f);
        return cell;
    }
    private static PdfPCell createCenterAlignedCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5f); // Optional: Add some padding to the cell
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Center horizontally
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);   // Center vertically
        return cell;
    }
    protected String convertToMyanmarCurrency(double amount) {
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

    public static String getStockIDGenerate(String prefix,String endID) {

        String currentDate = null;

        try {

            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            currentDate = now.format(formatter);



            if (currentDate.equals(endID.substring(3, 11))) {

                _stockcounter = Integer.parseInt(endID.substring(12));

                _stockcounter++;

            }

        } catch (NullPointerException | StringIndexOutOfBoundsException e_) {

            _stockcounter =1;

            return prefix + "-" + currentDate + "-" + _stockcounter;

        }


        return prefix + "-" + currentDate + "-" + _stockcounter;

    }

    public String getStockID(){


        String id = (dao.getAllList().isEmpty())?null: (dao.getAllList().getFirst().getStockcode());

        System.out.println(id);


        return getStockIDGenerate("#S",id);

    }


}
