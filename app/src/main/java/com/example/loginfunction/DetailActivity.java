package com.example.loginfunction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.loginfunction.SQLite.OrderContract;
import com.example.loginfunction.SQLite.OrderProvider;
import com.example.loginfunction.SQLite.ProductListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    EditText txtName,txtDes,txtPrice;
    ImageView img;
    Button choose,update;
    String userName;
    TextView textViewId;

    final int REQUEST_CODE_GALLERY = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Cursor cursor = getContentResolver().query(OrderProvider.CONTENT_URI, OrderContract.OrderEntryProduct.ALL_COLUMNS_PRODUCT,null,null,null);

        textViewId = (TextView) findViewById(R.id.textViewId);
        txtName = (EditText) findViewById(R.id.textName);
        txtDes = (EditText) findViewById(R.id.textDes);
        txtPrice = (EditText) findViewById(R.id.textPrice);
        img = (ImageView) findViewById(R.id.imageview123);

        textViewId.setText(getIntent().getStringExtra("IDD"));
        txtName.setText(getIntent().getExtras().getString("NAME"));
        txtDes.setText(getIntent().getExtras().getString("DES"));
        txtPrice.setText(getIntent().getExtras().getString("PRICE"));

        userName = getIntent().getStringExtra("username");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] byteArray = bundle.getByteArray("IMAGE23");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            img.setImageBitmap(bmp);
        }

        choose = (Button) findViewById(R.id.btnChoose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(DetailActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        update = (Button) findViewById(R.id.btnUpda);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);
                alertDialogBuilder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentValues values = new ContentValues();
                        String selection = OrderContract.OrderEntryProduct.COLUMN_ID + " LIKE ?";
                        String[] args = {textViewId.getText().toString()};

                        values.put(OrderContract.OrderEntryProduct._NAME,txtName.getText().toString());
                        values.put(OrderContract.OrderEntryProduct.COLUMN_DESCRIPTION,txtDes.getText().toString());
                        values.put(OrderContract.OrderEntryProduct.COLUMN_PRICE,txtPrice.getText().toString());
                        values.put(OrderContract.OrderEntryProduct.COLUMN_IMAGE,imageViewToByte(img));

                        int rowsAffected = getContentResolver().update(OrderProvider.CONTENT_URI,
                                values,
                                selection,
                                args
                        );
                        if(rowsAffected > 0){
                            Toast.makeText(DetailActivity.this, "Update Successful !!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(view.getContext(), AdminPage.class);
                            intent.putExtra("username", userName);
                            view.getContext().startActivity(intent);
                            cursor.requery();
                        } else {
                            Toast.makeText(DetailActivity.this, "Can't Update !!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailActivity.this, "Task cancelled !!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(view.getContext(), AdminPage.class);
                        startActivity(intent);
                    }
                });
                alertDialogBuilder.show();
            }
        });



    }
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }
    private void deleteProduct(){
        EditText name = (EditText) findViewById(R.id.edtName);
        String selection = OrderContract.OrderEntryProduct._NAME + " LIKE ?";
        String[] selectionArg = {name.getText().toString()};
        getContentResolver().delete(OrderProvider.CONTENT_URI,  selection, null);
        restartLoader();
        Toast.makeText(this, "Product Deleted", Toast.LENGTH_LONG).show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, OrderProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}