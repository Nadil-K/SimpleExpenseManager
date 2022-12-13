package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "200294F.db";
    private static final int DB_VERSION = 1;

    protected static final String TABLE1_NAME = "accounts";
    protected static final String AC_NO_COL = "accountNo";
    protected static final String BANK_NAME_COL = "bankName";
    protected static final String AC_HOLDER_COL = "accountHolderName";
    protected static final String BALANCE_COL = "balance";

    protected static final String TABLE2_NAME = "transactions";
    protected static final String TRANS_ID_COL = "transID";
    protected static final String DATE_COL = "date";
    protected static final String EXP_TYPE_COL = "expenseType";
    protected static final String AMOUNT_COL = "amount";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query1 = "CREATE TABLE " + TABLE1_NAME + "(" +
                AC_NO_COL + " TEXT PRIMARY KEY, " +
                BANK_NAME_COL + " TEXT NOT NULL, " +
                AC_HOLDER_COL + " TEXT NOT NULL, " +
                BALANCE_COL + " REAL NOT NULL)";

        String query2 = "CREATE TABLE " + TABLE2_NAME + "(" +
                TRANS_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE_COL + " TEXT NOT NULL, " +
                AC_NO_COL + " TEXT NOT NULL, " +
                EXP_TYPE_COL + " TEXT NOT NULL, " +
                AMOUNT_COL + " REAL NOT NULL, " +
                "FOREIGN KEY(" + AC_NO_COL + ") REFERENCES " + TABLE1_NAME + "(" + AC_NO_COL + "))";

        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        onCreate(sqLiteDatabase);
    }
}
