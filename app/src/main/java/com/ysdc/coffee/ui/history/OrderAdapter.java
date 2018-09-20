package com.ysdc.coffee.ui.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.injection.module.GlideApp;

import org.ocpsoft.prettytime.PrettyTime;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import static com.ysdc.coffee.data.model.OrderStatus.CANCELED;
import static com.ysdc.coffee.data.model.OrderStatus.IN_PROGRESS;
import static com.ysdc.coffee.data.model.OrderStatus.PENDING;
import static com.ysdc.coffee.data.model.OrderStatus.READY;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private static final Integer INCREMENT = 1;

    private List<OrderedProduct> orders;
    private final OnOrdersClickListener listener;
    private final Context context;

    interface OnOrdersClickListener {
        void onOrderClicked(OrderedProduct orderedProduct);
    }

    public OrderAdapter(List<OrderedProduct> orders, OnOrdersClickListener listener, Context context) {
        this.orders = orders;
        this.listener = listener;
        this.context = context;
    }

    public void updateOrders(List<OrderedProduct> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.cell_order, parent, false), parent.getContext(), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderedProduct orderedProduct = orders.get(position);

        if (!TextUtils.isEmpty(orderedProduct.getProduct().getImageUrl())) {
            holder.updatePicture(orderedProduct.getProduct().getImageUrl());
        }

        holder.productName.setText(orderedProduct.getProduct().getName());
        holder.quantityField.setText(String.valueOf(orderedProduct.getQuantity()));
        holder.orderDetails.setText(orderedProduct.getOrderDetails(context));
        switch (orderedProduct.getOrder().getStatus()) {
            case DONE:
                holder.orderStatus.setVisibility(View.GONE);
                break;
            case READY:
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.orderStatus.setText(context.getString(R.string.order_status, context.getString(READY.getLocalizableKey())));
                holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_round_corner_status_ready));
                break;
            case CANCELED:
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.orderStatus.setText(context.getString(R.string.order_status, context.getString(CANCELED.getLocalizableKey())));
                holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_round_corner_status_canceled));
                break;
            case PENDING:
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.orderStatus.setText(context.getString(R.string.order_status, context.getString(PENDING.getLocalizableKey())));
                holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_round_corner_status_pending));
                break;
            case IN_PROGRESS:
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.orderStatus.setText(context.getString(R.string.order_status, context.getString(IN_PROGRESS.getLocalizableKey())));
                holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_round_corner_status_pending));
            default:
                break;
        }
        PrettyTime timeFormatter = new PrettyTime(new Date());
        holder.date.setText(timeFormatter.format(orderedProduct.getOrder().getDate()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView productImage;
        private final TextView productName;
        private final TextView orderDetails;
        private final TextView orderStatus;
        private final TextView quantityField;
        private final TextView date;
        private final Context context;

        private WeakReference<OnOrdersClickListener> listenerRef;

        ViewHolder(View v, Context context, OnOrdersClickListener listener) {
            super(v);
            this.context = context;

            listenerRef = new WeakReference<>(listener);

            v.findViewById(R.id.cell_layout).setOnClickListener(v1 -> listenerRef.get().onOrderClicked(orders.get(getAdapterPosition())));
            productImage = v.findViewById(R.id.product_image);
            productName = v.findViewById(R.id.product_title);
            orderDetails = v.findViewById(R.id.order_details);
            orderStatus = v.findViewById(R.id.order_status);
            quantityField = v.findViewById(R.id.quantity_field);
            date = v.findViewById(R.id.order_date);
        }

        protected void updatePicture(String image) {
            GlideApp.with(context)
                    .load(image)
                    //.load(TextUtils.isEmpty(product.getImageUrl()) ? R.drawable.ic_profile_placeholder : product.getImageUrl())
                    .apply(new RequestOptions().circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(productImage);
        }
    }
}