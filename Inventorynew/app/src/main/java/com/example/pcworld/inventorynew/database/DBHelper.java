package com.example.pcworld.inventorynew.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.DB";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryContract.InventoryData.TABLE_NAME + " ("
                + InventoryContract.InventoryData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.InventoryData.PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryContract.InventoryData.PRICE + " INTEGER NOT NULL, "
                + InventoryContract.InventoryData.QUANTITY + " INTEGER NOT NULL , "
                + InventoryContract.InventoryData.SUPPLIER_NAME + " STRING ,"
                + InventoryContract.InventoryData.SUPPLIER_PHONE + " STRING);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
