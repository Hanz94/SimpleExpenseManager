package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hanz on 04/12/2015.
 */
public class DbOperator extends SQLiteOpenHelper {

    public static final String  transactionTable = "Transactions";
    public static final String accountTable = "Account";

    public static final String accountNo="accountNo";
    public static final String branchName="branchName";
    public static final String accountHolderName="accountHolderName";
    public static final String balance = "balance";

    public static final String expenseType="expenseType";
    public static final String amount ="amount";
    public static final String date="date";
    public static final String transactionId="transactionId";

    public static final String DATABASE_NAME = "130637L.db";
    public static final int DATABASE_VERSION = 1;

    public static final String ACCOUNT ="CREATE TABLE "+accountTable+"(" +
            accountNo+" varchar(15) PRIMARY KEY," +
            branchName+" varchar(30),"+
            accountHolderName+" varchar(30),"+
            balance+" double"+")";

    public static final String TRANSACTION ="CREATE TABLE "+transactionTable+"(" +
            accountNo+" varchar(15)," +
            transactionId+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            date+" date,"+
            expenseType+" varchar(7),"+
            amount+" double)";

    public DbOperator(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbOperator(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRANSACTION);
        db.execSQL(ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(db);
    }
}
