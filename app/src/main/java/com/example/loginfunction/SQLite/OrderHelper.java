package com.example.loginfunction.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.loginfunction.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABSE_NAME = "ord.db";


    public OrderHelper(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_TABLE = "CREATE TABLE " + OrderContract.OrderEntry.TABLE_NAME + " ("
                + OrderContract.OrderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +  OrderContract.OrderEntry.COLUMN_NAME + " TEXT NOT NULL, "
                +  OrderContract.OrderEntry.COLUMN_QUANTITY + " TEXT NOT NULL, "
                +  OrderContract.OrderEntry.COLUMN_PRICE + " TEXT NOT NULL, "
                +  OrderContract.OrderEntry.COLUMN_HASTOPPING + " TEXT NOT NULL, "
                +  OrderContract.OrderEntry.COLUMN_CREAM + " TEXT NOT NULL);";

                db.execSQL(SQL_TABLE);

        String SQL_TABLE_PRODUCT = "CREATE TABLE " + OrderContract.OrderEntryProduct.TABLE_PRODUCT + " ("
                + OrderContract.OrderEntryProduct.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +  OrderContract.OrderEntryProduct._NAME + " TEXT NOT NULL, "
                +  OrderContract.OrderEntryProduct.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                +  OrderContract.OrderEntryProduct.COLUMN_PRICE + " TEXT NOT NULL, "
                +  OrderContract.OrderEntryProduct.COLUMN_IMAGE + " TEXT NOT NULL)";

                db.execSQL(SQL_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addProduct(String name,String description,String price, byte[] image){
        ContentValues values = new ContentValues();
        values.put(OrderContract.OrderEntryProduct._NAME, name);
        values.put(OrderContract.OrderEntryProduct.COLUMN_DESCRIPTION, description);
        values.put(OrderContract.OrderEntryProduct.COLUMN_PRICE,price);
        values.put(OrderContract.OrderEntryProduct.COLUMN_IMAGE,image);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(OrderContract.OrderEntryProduct.TABLE_PRODUCT, null, values);
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {OrderContract.OrderEntryProduct.COLUMN_ID, OrderContract.OrderEntryProduct._NAME, OrderContract.OrderEntryProduct.COLUMN_DESCRIPTION, OrderContract.OrderEntryProduct.COLUMN_PRICE, OrderContract.OrderEntryProduct.COLUMN_IMAGE};
        Cursor cursor = db.query(OrderContract.OrderEntryProduct.TABLE_PRODUCT, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntryProduct.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntryProduct._NAME));
                String description = cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntryProduct.COLUMN_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntryProduct.COLUMN_PRICE));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(OrderContract.OrderEntryProduct.COLUMN_IMAGE));

                productList.add(new Product(id, name, description, price, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return productList;
    }

    public Cursor searchByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {OrderContract.OrderEntryProduct._NAME};
        String selection = OrderContract.OrderEntryProduct._NAME + " LIKE ?";
        String[] selectionArgs = {"%" + name + "%"};
        String sortOrder = OrderContract.OrderEntryProduct._NAME + " ASC";

        return db.query(
                OrderContract.OrderEntryProduct.TABLE_PRODUCT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
}

