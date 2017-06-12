package com.adie.task4.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adie.task4.model.Expenses;
import com.adie.task4.model.Income;

import java.util.ArrayList;

public class SqliteHelper extends SQLiteOpenHelper {
	//required var sqlitedb
	private static final String nama_database		="dbadvance";
	private static final int versi_database			=1;
	private static final String query_buat_tabel_expenses	= "CREATE TABLE IF NOT EXISTS expenses"
													+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
													+ "description TEXT,"
													+ "amount INTEGER)";
	private static final String query_hapus_tabel_expenses	= "DROP TABLE IF EXISTS expenses";

	private static final String query_buat_tabel_income	= "CREATE TABLE IF NOT EXISTS income"
			+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "description TEXT,"
			+ "amount INTEGER)";
	private static final String query_hapus_tabel_income	= "DROP TABLE IF EXISTS income";

	private Context crudContext;
	private SQLiteDatabase crudDatabase;
	private SqliteHelper crudHelper;

	public SqliteHelper(Context context) {
		super(context, nama_database, null, versi_database);
	}




	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase){

		sqLiteDatabase.execSQL(query_buat_tabel_expenses);
		sqLiteDatabase.execSQL(query_buat_tabel_income);

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int versi_lama, int versi_baru){
		database.execSQL(query_hapus_tabel_expenses);
		database.execSQL(query_hapus_tabel_income);
		onCreate(database);
	}
	//create table method
	public void CreateTabel()  {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL(query_buat_tabel_expenses);
		database.execSQL(query_buat_tabel_income);
	}
	//connect sqlitedb
	public void ConnectDB() throws SQLException {
		crudHelper = new SqliteHelper(crudContext);
		crudDatabase = crudHelper.getWritableDatabase();
	}
	public void HapusTabel()  {
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL(query_hapus_tabel_expenses);
		database.execSQL(query_hapus_tabel_income);
	}
 

	
	public void AddExpenses(String description, int amount){
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values	= new ContentValues();
		values.put("description", description);
		values.put("amount", amount);
		database.insert("expenses", null, values);
		database.close();
	}

	public void AddIncome(String description, int amount){
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values	= new ContentValues();
		values.put("description", description);
		values.put("amount", amount);
		database.insert("income", null, values);
		database.close();
	}


	public ArrayList<Expenses> getAllExpenses() {
		ArrayList<Expenses> ExpensesList = new ArrayList<Expenses>();
		// Select All Query
		String selectQuery = "SELECT * FROM expenses";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Expenses expenses = new Expenses();
				expenses.setDescription(cursor.getString(1));
				expenses.setAmount(cursor.getInt(2));
				ExpensesList.add(expenses);
			} while (cursor.moveToNext());
		}


		return ExpensesList;
	}

	public ArrayList<Income> getAllIncome() {
		ArrayList<Income> IncomeList = new ArrayList<Income>();
		// Select All Query
		String selectQuery = "SELECT * FROM income";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Income income = new Income();
				income.setDescription(cursor.getString(1));
				income.setAmount(cursor.getInt(2));
				IncomeList.add(income);
			} while (cursor.moveToNext());
		}


		return IncomeList;
	}


}
