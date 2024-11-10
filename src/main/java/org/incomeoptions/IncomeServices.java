package org.incomeoptions;

import org.expense_options.Exp_Typesdb;
import org.expense_options.Exp_Viewdb;
import org.paymentoptions.Paymentdb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IncomeServices {


    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    Paymentdb paymentdb  = context.getBean("paymentdb",Paymentdb.class);

    Exp_Viewdb expViewdb = context.getBean("exp_viewdb", Exp_Viewdb.class);

    Exp_Typesdb expTypedb = context.getBean("exp_typesdb",Exp_Typesdb.class);

    Incometypedb incometypedb = context.getBean("incomedb",Incometypedb.class);






}
