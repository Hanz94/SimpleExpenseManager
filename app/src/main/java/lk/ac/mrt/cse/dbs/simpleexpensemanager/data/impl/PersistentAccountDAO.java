package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.DbOperator;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Hanz on 05/12/2015.
 */
public class PersistentAccountDAO implements AccountDAO {

    SQLiteDatabase db=null;
    DbOperator dbHandler=null;

    public PersistentAccountDAO(Context context){
        if(dbHandler==null){
            dbHandler=new DbOperator(context);
        }
        db=dbHandler.getWritableDatabase();
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> array_list = new ArrayList<String>();

        Cursor res =  db.rawQuery( "select * from Account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DbOperator.accountNo)));
            res.moveToNext();
        }
        if(!res.isClosed()){
            res.close();
        }
        return array_list;

    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> array_list = new ArrayList<Account>();

        Cursor res =  db.rawQuery( "select * from Account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            String accountNo=res.getString(res.getColumnIndex(DbOperator.accountNo));
            String ranchName=res.getString(res.getColumnIndex(DbOperator.branchName));
            String accHolderName=res.getString(res.getColumnIndex(DbOperator.accountHolderName));
            double balance=res.getDouble(res.getColumnIndex(DbOperator.balance));
            array_list.add(new Account(accountNo,ranchName,accHolderName,balance));
            res.moveToNext();
        }
        if(!res.isClosed()){
            res.close();
        }
        return array_list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor res = db.rawQuery("select * from Account where accountNo = '"+ accountNo+"'",null);
        res.moveToFirst();
        String accountNumber = res.getString(res.getColumnIndex(DbOperator.accountNo));
        String branchName=res.getString(res.getColumnIndex(DbOperator.branchName));
        String accHolderName=res.getString(res.getColumnIndex(DbOperator.accountHolderName));
        double balance=res.getDouble(res.getColumnIndex(DbOperator.balance));
        if(!res.isClosed()){
            res.close();
        }
        return new Account(accountNumber,branchName,accHolderName,balance);
    }

    @Override
    public void addAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put(DbOperator.accountNo, account.getAccountNo());
        values.put(DbOperator.branchName, account.getBankName());
        values.put(DbOperator.accountHolderName, account.getAccountHolderName());
        values.put(DbOperator.balance, account.getBalance());
        db.insert("account", null, values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db.delete("Account", "id = ? ", new String[] { accountNo});

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        ContentValues values = new ContentValues();
        values.put(DbOperator.accountNo, accountNo);
        Account account = getAccount(accountNo);

        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                values.put(DbOperator.balance, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(DbOperator.balance, account.getBalance() + amount);
                break;
        }
        db.update(dbHandler.accountTable, values, "accountNo = ? ", new String[] { accountNo });
    }
}
