package org.incomeoptions;

import org.expense_options.Exp_Typesdb;
import org.expense_options.Exp_View;
import org.expense_options.Exp_Viewdb;
import org.paymentoptions.Payment;
import org.paymentoptions.Paymentdb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class IncomeServices {


    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);

    Income_Viewdb incomeViewdb = context.getBean("income_viewdb",Income_Viewdb.class);

    Incometypedb incometypedb = context.getBean("incomedb",Incometypedb.class);

    @Transactional
    public boolean useIncomeAddPaymentUpdate(Income_View incomeView, Payment payment) {

        boolean bo = false;

        if(incomeViewdb.insert(incomeView)==1){

            if(paymentdb.sumAmount(new Payment(payment.getPayid(),incomeView.getAmount()))==1){


                bo =true;


            }


        }

        return bo;

    }

    @Transactional
    public boolean useIncomeUpdateAmount(Income_View incomeView,int oldAmount) {

        boolean bo = false;



        if(incomeViewdb.updateAmount(incomeView)==1){




            if(paymentdb.subAmount(new Payment(incomeView.getPayid(),oldAmount))==1){


                paymentdb.sumAmount(new Payment(incomeView.getPayid(),incomeView.getAmount()));

                bo =true;


            }
            else {

                System.out.println("Something Worong");
            }



        }





return bo;

    }






}
