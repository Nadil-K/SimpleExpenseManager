package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseHelper;

public class PersistentExpenseManager extends ExpenseManager {

    public PersistentExpenseManager(Context context) {
        setup(context);
    }

    @Override
    public void setup(Context context) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        TransactionDAO PersistentTransactionDAO = new PersistentTransactionDAO(dbHelper);
        setTransactionsDAO(PersistentTransactionDAO);

        AccountDAO PersistentAccountDAO = new PersistentAccountDAO(dbHelper);
        setAccountsDAO(PersistentAccountDAO);


        // dummy data
//        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
//        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
//        getAccountsDAO().addAccount(dummyAcct1);
//        getAccountsDAO().addAccount(dummyAcct2);
    }
}
