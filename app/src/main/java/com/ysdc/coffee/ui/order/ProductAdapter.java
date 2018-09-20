package com.ysdc.coffee.ui.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.injection.module.GlideApp;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final Integer INCREMENT = 1;

    private List<Product> products;
    private final OnProductClickListener listener;
    private final Context context;
    private Map<String, Integer> productsQuantity;

    interface OnProductClickListener {
        void onProductClicked(Product product);
    }

    public ProductAdapter(List<Product> products, List<OrderEntry> orders, OnProductClickListener listener, Context context) {
        this.products = products;
        this.listener = listener;
        this.context = context;
        this.updateProductsQuantities(orders);
    }

    public void updateProducts(List<Product> products) {
        this.products = products;
    }

    public void updateProductsQuantities(List<OrderEntry> orders) {
        productsQuantity = new HashMap<>();
        for (OrderEntry order : orders) {
            Integer quantity = productsQuantity.get(order.getProduct().getId());
            if (quantity != null) {
                productsQuantity.put(order.getProduct().getId(), quantity + INCREMENT);
            } else {
                productsQuantity.put(order.getProduct().getId(), order.getQuantity());
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.cell_product, parent, false), parent.getContext(), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        if (!TextUtils.isEmpty(product.getImageUrl())) {
            holder.updatePicture(product.getImageUrl());
        }
        holder.productName.setText(product.getName());
        Integer quantity = productsQuantity.get(product.getId());
        if (quantity != null) {
            holder.quantityLayout.setVisibility(View.VISIBLE);
            holder.chevron.setVisibility(View.GONE);
            holder.quantityField.setText(String.valueOf(quantity));
        } else {
            holder.quantityLayout.setVisibility(View.GONE);
            holder.chevron.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView productImage;
        private final ImageView chevron;
        private final LinearLayout quantityLayout;
        private final TextView quantityField;
        private final TextView productName;
        private final Context context;

        private WeakReference<OnProductClickListener> listenerRef;

        ViewHolder(View v, Context context, OnProductClickListener listener) {
            super(v);
            this.context = context;

            listenerRef = new WeakReference<>(listener);

            v.findViewById(R.id.cell_layout).setOnClickListener(v1 -> listenerRef.get().onProductClicked(products.get(getAdapterPosition())));
            productImage = v.findViewById(R.id.product_image);
            chevron = v.findViewById(R.id.chevron);
            quantityLayout = v.findViewById(R.id.quantity_layout);
            quantityField = v.findViewById(R.id.quantity_field);
            productName = v.findViewById(R.id.product_title);
        }

        protected void updatePicture(String image) {
            GlideApp.with(context)
                    .load(image)
                    //.load(TextUtils.isEmpty(product.getImageUrl()) ? R.drawable.ic_profile_placeholder : product.getImageUrl())
                    .apply(new RequestOptions().circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(productImage);
        }
    }
}