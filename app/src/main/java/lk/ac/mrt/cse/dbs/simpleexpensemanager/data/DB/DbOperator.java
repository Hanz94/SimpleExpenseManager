package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hanz on 04/12/2015.
 */
public class DbOperator extends SQLiteOpenHelper {

    public static final String  transactionTable = "Transaction";
    public static final String accountTable = "Account";

    public static final String accountNo="accountNo";
    public static final String branchName="branchName";
    public static final String accountHolderName="accountHolderName";
    public static final String balance = "balance";

    public static final String expenseType="expenseType";
    public static final String amount ="amount";
    public static final String date="date";
    public static final String transactionId="transactionId";

    public static final String databasename = "130637L.db";
    public static final int database_version = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
