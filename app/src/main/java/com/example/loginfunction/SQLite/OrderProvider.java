package com.example.loginfunction.SQLite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class OrderProvider extends ContentProvider {

    // this constant is needed in order to define the path of our modification in the table
    public static final int ORDER = 100;
    private static final int PRODUCT = 1;
    private static final String AUTHORITY = "com.example.loginfunction";
    private static final String BASE_PATH = "products";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH, ORDER);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, PRODUCT);
    }

    public OrderHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new OrderHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,  String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor;
        int match  = sUriMatcher.match(uri);
        switch (match) {
            case ORDER:
                cursor = database.query(OrderContract.OrderEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT:
                cursor = database.query(OrderContract.OrderEntryProduct.TABLE_PRODUCT, OrderContract.OrderEntryProduct.ALL_COLUMNS_PRODUCT,
                        selection, selectionArgs, null, null, OrderContract.OrderEntryProduct._NAME + " ASC");
                break;
            default:
                throw new IllegalArgumentException("CANT QUERY");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType( Uri uri) {
        return null;
    }

    // we will work on this method  because in this app we are just inserting the data not updating it for now
    @Override
    public Uri insert(Uri uri,  ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ORDER:
                return insertCart(uri, values);
            case PRODUCT:
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Cant insert data");
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        SQLiteDatabase database = mHelper.getWritableDatabase();
        long id = database.insert(OrderContract.OrderEntryProduct.TABLE_PRODUCT, null, values);

        if (id == -1) {
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertCart(Uri uri, ContentValues values) {

        String name = values.getAsString(OrderContract.OrderEntry.COLUMN_NAME);
        if(name == null) {
            throw new IllegalArgumentException("Name is Required");
        }

        String quantity = values.getAsString(OrderContract.OrderEntry.COLUMN_QUANTITY);
        if(quantity == null) {
            throw new IllegalArgumentException("quantity is Required");
        }

        String price = values.getAsString(OrderContract.OrderEntry.COLUMN_PRICE);
        if(price == null) {
            throw new IllegalArgumentException("price is Required");
        }

        SQLiteDatabase database = mHelper.getWritableDatabase();
        long id = database.insert(OrderContract.OrderEntry.TABLE_NAME, null, values);

        if (id == -1) {
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ORDER:
                rowsDeleted = database.delete(OrderContract.OrderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT:
                rowsDeleted = database.delete(OrderContract.OrderEntryProduct.TABLE_PRODUCT, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Can not delete !!!");
        }
        if (rowsDeleted!=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        int updCount;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case PRODUCT:
                updCount = database.update(OrderContract.OrderEntryProduct.TABLE_PRODUCT, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        if (updCount!=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updCount;
    }


}
