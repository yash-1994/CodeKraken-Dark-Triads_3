package com.example.mess_mark_01.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mess_mark_01.DetailsActivity;
import com.example.mess_mark_01.Model.Product;
import com.example.mess_mark_01.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductHomeAdapter extends RecyclerView.Adapter<ProductHomeAdapter.ViewHolder> {

    private Context context;
    int currentSaveImage = 0;
    private List<Product> productList;

    public ProductHomeAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_home_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productImageView.setImageResource(R.drawable.farm);
        holder.productNameTextView.setText(product.getProTag());
        holder.productPriceView.setText(String.valueOf(product.getProStartingPrice())+"/Kgs");
        Picasso.get()
                .load(product.getProPicUrl())
                .into(holder.productImageView);
        holder.saveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSaveImage = (currentSaveImage == 0) ? 1 : 0;
                holder.productSaveImage.setImageResource(currentSaveImage == 0 ? R.drawable.save : R.drawable.green_save);
            }
        });

        holder.productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("id",product.getProID());
                intent.putExtra("uri",product.getProPicUrl());
                intent.putExtra("name",product.getProTag());
                intent.putExtra("dec",product.getProDescription());
                intent.putExtra("price",product.getProStartingPrice());
                intent.putExtra("owner",product.getOwnerID());
                intent.putExtra("loc",product.getCity());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView saveClick;
        TextView productNameTextView;
        TextView productPriceView;
        ImageView productImageView,productSaveImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            saveClick = itemView.findViewById(R.id.saveClick); // Use the ID from your CardView layout
            productNameTextView = itemView.findViewById(R.id.productName); // Use the ID from your CardView layout
            productPriceView = itemView.findViewById(R.id.productPrice); // Use the appropriate ID
            productImageView = itemView.findViewById(R.id.productPhotoContainer); // Use the appropriate ID
            productSaveImage = itemView.findViewById(R.id.productSaveImage);
        }
    }
}

