package org.saleorderoptions;


import javafx.scene.control.TableView;
import org.databases.Stockdb;
import org.models.Stock;
import org.orderoptions.Order;
import org.orderoptions.Orderdb;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.saleoptions.Sale;
import org.saleoptions.Saledb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;




public class SaleServices {

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    SaleOrderdb saleOrderdb  = context.getBean("saleorderdb",SaleOrderdb.class);

    Saledb saledb  = context.getBean("saledb",Saledb.class);

    Orderdb orderdb  = context.getBean("orderdb",Orderdb.class);

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);

    Stockdb stockdb  = context.getBean("stockdb",Stockdb.class);



    @Transactional
    public int deleteSaleAndUpdatePayment(String __oid, SaleOrder saleOrder, TableView tableView) {

        int result = 0;


        if( saledb.getDeleteById(__oid, saleOrder.getStockcode())==1 ){

            if(paymentdb.subAmount(new Payment(saleOrder.getPayid(), saleOrder.getTotal()))==1){

               if( stockdb.sumQty(new Stock(saleOrder.getStockcode(),saleOrder.getStockname(),saleOrder.getQty()))==1){


                   if(tableView.getItems().size()==1){

                       orderdb.getDeleteById(__oid);

                   }

                   result =1;





               }


            }






        }

        return result;

    }

    @Transactional
    public int updateSaleAndUpdatePayment(Sale sale, int qtyOldValue, int priceOldValue, int discountOldValue, Payment payment) {

        saledb.update(sale);
        adjustStockQuantity(sale, qtyOldValue);
        adjustPaymentAmount(sale, qtyOldValue, priceOldValue, discountOldValue, payment);

        return 1;
    }

    @Transactional
    public int updateOrderAndUpdatePaymentMethod(Order order,int oldPaymentID,int oldAmount) {


        orderdb.update(order);

        adjuctPaymentAmounts(oldPaymentID,order.getPayid(),oldAmount);

     return 1;

    }

    @Transactional
    public void adjuctPaymentAmounts(int oldPaymentID,int newPaymentID,int amount){


        paymentdb.subAmount(new Payment(oldPaymentID, amount));

        paymentdb.sumAmount(new Payment(newPaymentID, amount));

    }



    private void adjustStockQuantity(Sale sale, int oldQty) {

        int qtyDifference = sale.getQty() - oldQty;
        if (qtyDifference > 0) {
            stockdb.sumQty(new Stock(sale.getStockcode(), sale.getStockcode(), qtyDifference));
        } else if (qtyDifference < 0) {
            stockdb.subQty(new Stock(sale.getStockcode(), sale.getStockcode(), -qtyDifference));
        }
    }

    private void adjustPaymentAmount(Sale sale, int oldQty, int oldPrice, int oldDiscount, Payment payment) {

        int oldTotalPrice = (oldQty * oldPrice) - oldDiscount;

        int newTotalPrice = (sale.getQty() * sale.getPrice()) - sale.getDiscount();

        int priceDifference = newTotalPrice - oldTotalPrice;

        if (priceDifference > 0) {
            paymentdb.sumAmount(new Payment(payment.getPayid(), priceDifference));
        } else if (priceDifference < 0) {
            paymentdb.subAmount(new Payment(payment.getPayid(), -priceDifference));
        }
    }



}
