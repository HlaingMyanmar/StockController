package org.saleorderoptions;

import org.databases.Stockdb;
import org.models.Stock;
import org.orderoptions.Orderdb;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
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
    public void deleteSaleAndUpdatePayment(String __oid, SaleOrder saleOrder) {

        saledb.getDeleteById(__oid, saleOrder.getStockcode());
        orderdb.getDeleteById(__oid);
        paymentdb.subAmount(new Payment(saleOrder.getPayid(), saleOrder.getTotal()));
        stockdb.sumQty(new Stock(saleOrder.getStockcode(),saleOrder.getStockname(),saleOrder.getQty()));
    }

}
