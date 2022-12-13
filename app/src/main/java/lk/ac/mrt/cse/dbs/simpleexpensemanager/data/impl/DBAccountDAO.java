//package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.Nullable;
//
//public class DBAccountDAO extends SQLiteOpenHelper implements AccountDAO {
//
//    private static final String DB_NAME = "AccountsDB";
//    private static final int DB_VERSION = 1;
//    private static final String TABLE_NAME = "accounts";
//    private static final String AC_NO_COL = "accountNo";
//    private static final String BANK_NAME_COL = "bankName";
//    private static final String AC_HOLDER_COL = "accountHolderName";
//    private static final String BALANCE_COL = "balance";
//
//    public DBAccountDAO(@Nullable Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//
//        String query = "create table "+TABLE_NAME + " (" +
//                AC_NO_COL + " varchar(15),"+
//                BANK_NAME_COL + " varchar(20),"+
//                AC_HOLDER_COL + " varchar(25),"+
//                BALANCE_COL + " numeric(12,2) check("+BALANCE_COL+">0),"+
//                "primary key("+AC_NO_COL+") );";
//
////        String query = "CREATE TABLE " + TABLE_NAME + " ("
////                + AC_NO_COL + " TEXT PRIMARY KEY,"
////                + BANK_NAME_COL + " TEXT,"
////                + AC_HOLDER_COL + " TEXT,"
////                + BALANCE_COL + " DOUBLE);";
//
//        sqLiteDatabase.execSQL(query);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(sqLiteDatabase);
//
//    }
//
//    @Override
//    public List<String> getAccountNumbersList() {
//
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursorACNo = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
////        Cursor cursorACNo = sqLiteDatabase.rawQuery("SELECT AC_NO_COL FROM " + TABLE_NAME, null);
//
//        List<String> accountNumbersList = new ArrayList<>();
//
//        if (cursorACNo.moveToFirst()) {
//            do {
//                accountNumbersList.add(new String(cursorACNo.getString(1)));
//            } while (cursorACNo.moveToNext());
//        }
//        cursorACNo.close();
//        return accountNumbersList;
//    }
//
//    @Override
//    public List<Account> getAccountsList() {
//
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursorAccounts = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//
//        List<Account> accountsList = new ArrayList<>();
//
//        if (cursorAccounts.moveToFirst()) {
//            do {
//                accountsList.add(new Account(cursorAccounts.getString(1),
//                        cursorAccounts.getString(2),
//                        cursorAccounts.getString(3),
//                        cursorAccounts.getInt(4)));
//            } while (cursorAccounts.moveToNext());
//        }
//        cursorAccounts.close();
//        return accountsList;
//    }
//
//    @Override
//    public Account getAccount(String accountNo) throws InvalidAccountException {
//
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursorDB = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//
//        if (cursorDB.moveToFirst()) {
//            do {
//                if (cursorDB.getString(1)==accountNo){
//                    return new Account(cursorDB.getString(1),
//                            cursorDB.getString(2),
//                            cursorDB.getString(3),
//                            cursorDB.getInt(4));
//                }
//
//            } while (cursorDB.moveToNext());
//        }
//        cursorDB.close();
//        return null;
//    }
//
//    @Override
//    public void addAccount(Account account) {
//
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(AC_NO_COL, account.getAccountNo());
//        values.put(BANK_NAME_COL, account.getBankName());
//        values.put(AC_HOLDER_COL, account.getAccountHolderName());
//        values.put(BALANCE_COL, account.getBalance());
//
//        sqLiteDatabase.insert(TABLE_NAME, null, values);
//        sqLiteDatabase.close();
//    }
//
//    @Override
//    public void removeAccount(String accountNo) throws InvalidAccountException {
//
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        sqLiteDatabase.delete(TABLE_NAME, AC_NO_COL + "=?", new String[]{accountNo});
//        sqLiteDatabase.close();
//    }
//
//    @Override
//    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
//
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        Cursor cursorDB = sqLiteDatabase.rawQuery("SELECT AC_NO_COL,BALANCE_COL FROM " + TABLE_NAME, null);
//        double currentBal = 0;
//        if (cursorDB.moveToFirst()) {
//            do {
//                if (cursorDB.getString(1)==accountNo){
//                    currentBal = cursorDB.getDouble(2);
//                }
//            } while (cursorDB.moveToNext());
//        }
//        cursorDB.close();
//
//        ContentValues values = new ContentValues();
//
//        if (expenseType==ExpenseType.INCOME){
//            values.put(BALANCE_COL, currentBal+amount);
//        }
//        else{ values.put(BALANCE_COL, currentBal-amount); }
//
//        sqLiteDatabase.update(TABLE_NAME, values, "name=?", new String[]{accountNo});
//        sqLiteDatabase.close();
//    }
//
//
//}
