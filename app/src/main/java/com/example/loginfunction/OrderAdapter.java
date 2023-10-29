package com.example.loginfunction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements Filterable {

    List<Product> data;
    List<Product> itemListFiltered;
    Context context;

    public OrderAdapter(List<Product> data, Context context) {
        this.data = data;
        this.context = context;
        this.itemListFiltered = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Product temp = data.get(position);
        String nameofDrink = data.get(position).getName();
        String descriptionofdrink = data.get(position).getDescription();
        String priceOfDrink = data.get(position).getPrice();
        byte[] images = data.get(position).getImage();

        Bitmap bitmap = BitmapFactory.decodeByteArray(images, 0, images.length);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        holder.txtmName.setText(nameofDrink);
        holder.txtmDes.setText(descriptionofdrink);
        holder.txtmPrice.setText(priceOfDrink);
        holder.imgProduct.setImageBitmap(bitmap);


        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailCardItem.class);
                intent.putExtra("image",byteArray);
                intent.putExtra("name",temp.getName());
                intent.putExtra("description",temp.getDescription());
                intent.putExtra("price",temp.getPrice());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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
                data = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgProduct;
        TextView txtmName, txtmDes, txtmPrice;
        public ViewHolder( View itemView) {
            super(itemView);
            imgProduct  = (ImageView) itemView.findViewById(R.id.imgModel);
            txtmName = (TextView)  itemView.findViewById(R.id.txtmName);
            txtmDes = (TextView)  itemView.findViewById(R.id.txtmDescription);
            txtmPrice = (TextView)  itemView.findViewById(R.id.txtmPrice);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position == 0) {
                Intent intent = new Intent(context, DetailCardItem.class);
                context.startActivity(intent);
            }

            if (position == 1) {
                Intent intent2 = new Intent(context, InfoActivity.class);
                context.startActivity(intent2);
            }
        }
    }
}

