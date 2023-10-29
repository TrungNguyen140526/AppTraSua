package com.example.loginfunction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginfunction.SQLite.OrderContract;
import com.example.loginfunction.SQLite.OrderHelper;
import com.example.loginfunction.SQLite.OrderProvider;
import com.example.loginfunction.SQLite.ProductListAdapter;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    GridView gridView;
    ArrayList<Product> list;
    ProductListAdapter adapter = null;
    Context mContext;
    Button delete, create;
    OrderHelper dbHelper;
    String username;
    SearchView searchView_admin;

    TextView txtUserName;

    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        gridView = findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new ProductListAdapter(this, R.layout.list_item,list);
        gridView.setAdapter(adapter);
        delete = findViewById(R.id.btnDelete);
        create = findViewById(R.id.butCreate);
        txtUserName = findViewById(R.id.txtUserName);
        searchView_admin = findViewById(R.id.searchView_admin);

        username = getIntent().getStringExtra("username");

        txtUserName.setText("Welcome " + getIntent().getStringExtra("username") + " back!!");

        dbHelper = new OrderHelper(this);

        searchView_admin.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        populateItemList(dbHelper);
        gridView.setAdapter(adapter);


        Cursor cursor = dbHelper.getData("SELECT * FROM PRODUCT");
        list.clear();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String des = cursor.getString(2);
            String price = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new Product(id,name,des,price,image));
        }
        adapter.notifyDataSetChanged();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
                Intent intent = new Intent(AdminPage.this,AdminPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("username");
                Intent intent = new Intent(AdminPage.this,CreateProduct.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }
    public void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }
    public void deleteProduct(){
        try{
            name = (EditText) findViewById(R.id.editTextTextPersonName);
            String selection = OrderContract.OrderEntryProduct._NAME + " LIKE ?";
            String[] selectionArg = {name.getText().toString()};
            getContentResolver().delete(OrderProvider.CONTENT_URI,  selection, selectionArg);
            restartLoader();
            Toast.makeText(this, "Product Deleted !!!", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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
        adapter = new ProductListAdapter(mContext, R.layout.list_item, listFood);

        gridView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void populateItemList(OrderHelper sqLiteHelper) {
        list.clear();
        ArrayList<Product> productsFromDatabase = sqLiteHelper.getAllProducts();
        list.addAll(productsFromDatabase);
    }
}