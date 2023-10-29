package com.example.loginfunction.SQLite;

import android.net.Uri;
import android.provider.BaseColumns;

public class OrderContract {

    public OrderContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.loginfunction";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH = "orderig";
    public static final Uri BASE_URI_PRODUCT = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCT = "products";



    public static abstract class OrderEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);

        public static final String TABLE_NAME = "orderig";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_HASTOPPING = "hastoppings";
        public static final String COLUMN_CREAM = "hascream";


    }

    public static abstract class OrderEntryProduct implements BaseColumns{

        public static final Uri CONTENT_URI_PRODUCT = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT);
        public static final String TABLE_PRODUCT = "PRODUCT";
        public static final String COLUMN_ID = "Id";
        public static final String _NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE = "image";

        public static final String[] ALL_COLUMNS_PRODUCT =
                {COLUMN_ID, _NAME, COLUMN_DESCRIPTION, COLUMN_PRICE,COLUMN_IMAGE};


    }
}
