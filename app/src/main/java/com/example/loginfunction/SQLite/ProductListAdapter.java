package com.example.loginfunction.SQLite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginfunction.AdminPage;
import com.example.loginfunction.DetailActivity;
import com.example.loginfunction.Product;
import com.example.loginfunction.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private  int layout;
    private ArrayList<Product> productList;

    private ArrayList<Product> itemListFiltered;

    public ProductListAdapter(Context context, int layout, ArrayList<Product> productsList) {
        this.context = context;
        this.layout = layout;
        this.productList = productsList;
        this.itemListFiltered = productsList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.count = itemListFiltered.size();
                    filterResults.values = itemListFiltered;
                } else {
                    String searchString = charSequence.toString().toLowerCase();
                    ArrayList<Product> resultData = new ArrayList<>();
                    for (Product model : itemListFiltered) {
                        if (model.getName().toLowerCase().contains(searchString) ||
                                model.getDescription().toLowerCase().contains(searchString)) {
                            resultData.add(model);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productList = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtDes, txtPrice, txtIds;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();
        Product item = productList.get(position);

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtIds = (TextView) row.findViewById(R.id.txtIds);
            holder.txtName = (TextView) row.findViewById(R.id.txtmName);
            holder.txtDes = (TextView) row.findViewById(R.id.txtmDescription);
            holder.txtPrice = (TextView) row.findViewById(R.id.txtmPrice);
            holder.imageView = (ImageView) row.findViewById(R.id.imgModel);
            ImageView img = (ImageView) row.findViewById(R.id.imgModel);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("IDD",item.getId());
                    intent.putExtra("NAME",item.getName());
                    intent.putExtra("DES",item.getDescription());
                    intent.putExtra("PRICE",item.getPrice());
                    intent.putExtra("IMAGE23",byteArray);
                    view.getContext().startActivity(intent);
                }
            });
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        Product food = productList.get(position);

        holder.txtIds.setText(food.getId());
        holder.txtName.setText(food.getName());
        holder.txtDes.setText(food.getDescription());
        holder.txtPrice.setText(food.getPrice());
        byte[] foodImage = food.getImage();

        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }

}
