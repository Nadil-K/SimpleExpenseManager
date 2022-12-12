package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private final DatabaseHelper dbHelper;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));

    public PersistentTransactionDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DATE_COL, date.toString());
        contentValues.put(DatabaseHelper.AC_NO_COL, accountNo);
        contentValues.put(DatabaseHelper.EXP_TYPE_COL, expenseType.toString());
        contentValues.put(DatabaseHelper.AMOUNT_COL, amount);

        sqLiteDatabase.insert(DatabaseHelper.TABLE2_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE2_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AC_NO_COL));

                Date date = null;
                try {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_COL)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ExpenseType expenseType = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXP_TYPE_COL)).equals("INCOME") ? ExpenseType.INCOME : ExpenseType.EXPENSE;

                transactions.add(new Transaction(date, accountNo, expenseType,  cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.AMOUNT_COL))));

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return transactions;    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }
}
