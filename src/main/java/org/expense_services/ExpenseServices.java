package org.expense_services;

import org.expense_options.Exp_Typesdb;
import org.expense_options.Exp_View;
import org.expense_options.Exp_Viewdb;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class ExpenseServices {

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);

    Exp_Viewdb expViewdb = context.getBean("exp_viewdb", Exp_Viewdb.class);

    Exp_Typesdb expTypedb = context.getBean("exp_typesdb",Exp_Typesdb.class);

    @Transactional
    public boolean useExpense(Exp_View expView,Payment payment) {

        boolean bo = false;

        if(expViewdb.insert(expView)==1){

            if(paymentdb.subAmount(new Payment(payment.getPayid(),expView.getTotal()))==1){


                bo =true;


            }


        }

        return bo;

    }



}
