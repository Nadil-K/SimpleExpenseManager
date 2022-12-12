package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DBTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {

    private static final String DB_NAME = "TransactionsDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "transactions";
    private static final String TRANS_ID_COL = "transID";
    private static final String DATE_COL = "date";
    private static final String AC_NO_COL = "accountNo";
    private static final String EXP_TYPE_COL = "expenseType";
    private static final String AMOUNT_COL = "amount";

    public DBTransactionDAO(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "create table "+TABLE_NAME + " (" +
                TRANS_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DATE_COL + " INTEGER,"+
                AC_NO_COL + " varchar(20),"+
                EXP_TYPE_COL + " varchar(25),"+
                AMOUNT_COL + " numeric(12,2) check("+AMOUNT_COL+">0)"+
                " );";

//        String query = "CREATE TABLE " + TABLE_NAME + " ("
//                + TRANS_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + DATE_COL + " TEXT,"
//                + AC_NO_COL + " TEXT,"
//                + EXP_TYPE_COL + " TEXT,"
//                + AMOUNT_COL + " REAL);";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATE_COL, date.toString());
        values.put(AC_NO_COL, accountNo);
        values.put(EXP_TYPE_COL, expenseType.toString());
        values.put(AMOUNT_COL, amount);

        sqLiteDatabase.insert(TABLE_NAME, null, values);
        sqLiteDatabase.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursorTransactions = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        List<Transaction> transactionsList = new ArrayList<>();

        if (cursorTransactions.moveToFirst()) {
            do {
                try {
                    transactionsList.add(new Transaction(new SimpleDateFormat("dd/MM/yy").parse(cursorTransactions.getString(2)),
                            cursorTransactions.getString(3),
                            ExpenseType.valueOf(cursorTransactions.getString(4)),
                            cursorTransactions.getInt(5)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursorTransactions.moveToNext());
        }
        cursorTransactions.close();
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursorTransactions = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        List<Transaction> selectedTransactionsList = new ArrayList<>();

        if (cursorTransactions.moveToPosition(cursorTransactions.getCount()-limit)) {
            do {
                try {
                    selectedTransactionsList.add(new Transaction(new SimpleDateFormat("dd/MM/yy").parse(cursorTransactions.getString(2)),
                            cursorTransactions.getString(3),
                            ExpenseType.valueOf(cursorTransactions.getString(4)),
                            cursorTransactions.getInt(5)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursorTransactions.moveToNext());
        }
        cursorTransactions.close();
        return selectedTransactionsList;
    }
}
