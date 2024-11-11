package org.expense_services;

import org.Alerts.AlertBox;
import org.expense_options.Exp_Typesdb;
import org.expense_options.Exp_View;
import org.expense_options.Exp_Viewdb;
import org.incomeoptions.Income_View;
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
    @Transactional
    public boolean useIncomeUpdateAmount(Exp_View expView, int oldAmount) {

        boolean bo = false;



        if(expViewdb.updateAmount(expView)==1){




            if(paymentdb.subAmount(new Payment(expView.getPaymentid(),oldAmount))==1){


                paymentdb.sumAmount(new Payment(expView.getPaymentid(),expView.getTotal()));

                bo =true;


            }
            else {

                System.out.println("Something Worong");
            }



        }





        return bo;

    }


    @Transactional
    public boolean useExpenseDelete(String exviewid, Payment payment) {
        try {

            if (expViewdb.getDeleteById(exviewid) != 1) {
                throw new RuntimeException("Expense delete failed");
            }


            if (paymentdb.sumAmount(new Payment(payment.getPayid(), payment.getTotal())) != 1) {
                throw new RuntimeException("Payment update failed");
            }

            return true;

        } catch (Exception e) {



            AlertBox.showError("အသုံးစရိတ်","ဆားဗစ်အမှား"+exviewid);


            throw e;
        }
    }



}







