package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.DbOperator;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Hanz on 05/12/2015.
 */

public class PersistentTransactionDAO implements TransactionDAO {

    SQLiteDatabase db=null;
    DbOperator dbOperator=null;


    public PersistentTransactionDAO(Context context) {
        if(dbOperator ==null){
            dbOperator=new DbOperator(context);
        }
        db=dbOperator.getWritableDatabase();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues values = new ContentValues();
        DateFormat d = new SimpleDateFormat("yyyy-MM-dd");

        values.put(DbOperator.date,d.format(date) );
        values.put(DbOperator.accountNo, accountNo);
        values.put(DbOperator.expenseType, String.valueOf(expenseType));
        values.put(DbOperator.amount, amount);
        db.insert(DbOperator.transactionTable, null, values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> array_list = new ArrayList<Transaction>();

        Cursor res =  db.rawQuery( "select * from Transactions", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            String date=res.getString(res.getColumnIndex(DbOperator.date));
            String accountNo=res.getString(res.getColumnIndex(DbOperator.accountNo));
            String expenseType=res.getString(res.getColumnIndex(DbOperator.expenseType));
            double amount=res.getDouble(res.getColumnIndex(DbOperator.amount));
            DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            ExpenseType extType = null;
            switch (expenseType) {
                case "EXPENSE":
                    extType = ExpenseType.EXPENSE;
                    break;
                case "INCOME":
                    extType = ExpenseType.INCOME;
                    break;
            }
            try {
                array_list.add(new Transaction(d.parse(date), accountNo, extType, amount));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        if(!res.isClosed()){
            res.close();
        }
        return array_list;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Transactions LIMIT "+limit,null);
        while(cursor.moveToNext()){
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = formatter.parse(cursor.getString(2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ExpenseType expenseType =ExpenseType.EXPENSE;
            if(cursor.getInt(3) == 0 ){
                expenseType = ExpenseType.INCOME;
            }
            Transaction transaction = new Transaction(date,cursor.getString(0),expenseType,cursor.getDouble(4));
            transactionList.add(transaction);
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        return transactionList;
    }
}
