package com.example.loginfunction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.loginfunction.SQLite.OrderContract;
import com.example.loginfunction.SQLite.OrderHelper;
import com.example.loginfunction.SQLite.OrderProvider;
import com.example.loginfunction.SQLite.ProductListAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    TextView txtUsername;
    ImageView cartIcon;
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    ArrayList<Product> list;
    SearchView searchView;
    OrderHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        txtUsername = findViewById(R.id.txtUserName);
        cartIcon = findViewById(R.id.cartIcon);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        searchView = findViewById(R.id.searchView);
        list = new ArrayList<>();

        dbHelper = new OrderHelper(this);


        txtUsername.setText("Welcome " + getIntent().getStringExtra("username") + " \n");

        String username = getIntent().getStringExtra("username");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                orderAdapter.getFilter().filter(newText);
                return true;
            }
        });
        populateItemList(dbHelper);
        recyclerView.setAdapter(orderAdapter);

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        Cursor cursor = dbHelper.getData("SELECT * FROM PRODUCT");
        ArrayList<Product> listFood = new ArrayList<>();
        while(cursor.moveToNext()){
            int index0 = cursor.getColumnIndex(OrderContract.OrderEntryProduct.COLUMN_ID);
            int index1 =cursor.getColumnIndex(OrderContract.OrderEntryProduct._NAME);
            int index2 =cursor.getColumnIndex(OrderContract.OrderEntryProduct.COLUMN_DESCRIPTION);
            int index3 =cursor.getColumnIndex(OrderContract.OrderEntryProduct.COLUMN_PRICE);
            int index4 = cursor.getColumnIndex(OrderContract.OrderEntryProduct.COLUMN_IMAGE);
            String id = cursor.getString(index0);
            String name = cursor.getString(index1);
            String description = cursor.getString(index2);
            String price = cursor.getString(index3);
            byte[] image = cursor.getBlob(index4);

            Product product = new Product(id,name,description,price,image);
            listFood.add(product);
        }
        orderAdapter = new OrderAdapter(listFood, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // adapter
        recyclerView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
    }

    private void populateItemList(OrderHelper sqLiteHelper) {
        list.clear();
        ArrayList<Product> productsFromDatabase = sqLiteHelper.getAllProducts();
        list.addAll(productsFromDatabase);
    }
}