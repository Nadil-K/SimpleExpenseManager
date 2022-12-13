package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO implements AccountDAO {

    private final DatabaseHelper dbHelper;

    public PersistentAccountDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.AC_NO_COL + " FROM " + DatabaseHelper.TABLE1_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        List<String> accountNumbers = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                accountNumbers.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.AC_NO_COL)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE1_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                accounts.add(new Account(cursor.getString(cursor.getColumnIndex(DatabaseHelper.AC_NO_COL)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.BANK_NAME_COL)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.AC_HOLDER_COL)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.BALANCE_COL))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE1_NAME + " WHERE accountNo = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{accountNo});

        if (!cursor.moveToFirst()) { throw new InvalidAccountException(new String("Account " + accountNo + " is invalid.")); }

        Account account = new Account(accountNo, cursor.getString(cursor.getColumnIndex(DatabaseHelper.BANK_NAME_COL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.AC_HOLDER_COL)),
                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.BALANCE_COL)));

        cursor.close();
        sqLiteDatabase.close();

        return account;    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.AC_NO_COL, account.getAccountNo());
        contentValues.put(DatabaseHelper.BANK_NAME_COL, account.getBankName());
        contentValues.put(DatabaseHelper.AC_HOLDER_COL, account.getAccountHolderName());
        contentValues.put(DatabaseHelper.BALANCE_COL, account.getBalance());

        sqLiteDatabase.insert(DatabaseHelper.TABLE1_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        if ((sqLiteDatabase.delete(DatabaseHelper.TABLE1_NAME, DatabaseHelper.AC_NO_COL + " = ?", new String[]{accountNo}))==0) {
            throw new InvalidAccountException(new String("Account " + accountNo + " is invalid."));
        }
        sqLiteDatabase.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.BALANCE_COL + " FROM " + DatabaseHelper.TABLE1_NAME + " WHERE " + DatabaseHelper.AC_NO_COL + "= ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{accountNo});

        if (!cursor.moveToFirst()) { throw new InvalidAccountException(new String("Account " + accountNo + " is invalid.")); }

        double currentBal = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.BALANCE_COL));
        cursor.close();

        if (expenseType==ExpenseType.EXPENSE) {
            currentBal -= amount;
        }
        else{
            currentBal += amount;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.BALANCE_COL, currentBal);

        sqLiteDatabase.update(DatabaseHelper.TABLE1_NAME, contentValues, DatabaseHelper.AC_NO_COL + " = ?", new String[]{accountNo});
        sqLiteDatabase.close();
    }
}
