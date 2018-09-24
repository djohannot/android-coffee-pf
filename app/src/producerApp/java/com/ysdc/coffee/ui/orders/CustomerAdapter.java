package com.ysdc.coffee.ui.orders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.OrderProducts;
import com.ysdc.coffee.data.model.UserOrder;
import com.ysdc.coffee.injection.module.GlideApp;

import org.ocpsoft.prettytime.PrettyTime;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import static com.ysdc.coffee.data.model.OrderStatus.READY;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private List<UserOrder> orders;
    private final OnOrdersClickListener listener;
    private final Context context;

    interface OnOrdersClickListener {
        void onOrderClicked(UserOrder order);
    }

    public CustomerAdapter(List<UserOrder> orders, OnOrdersClickListener listener, Context context) {
        this.orders = orders;
        this.listener = listener;
        this.context = context;
    }

    public void updateOrders(List<UserOrder> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.cell__customer_order, parent, false), parent.getContext(), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserOrder order = orders.get(position);

        if (!TextUtils.isEmpty(order.getUser().getImageUrl())) {
            holder.updatePicture(order.getUser().getImageUrl());
        }

        holder.customerName.setText(order.getUser().getName());
        PrettyTime timeFormatter = new PrettyTime(new Date());
        holder.date.setText(timeFormatter.format(order.getDate()));

        holder.orderStatus.setText(context.getString(order.getStatus().getLocalizableKey()));
        holder.content.removeAllViews();
        for (OrderProducts orderedProduct : order.getOrderedProductList()) {
            final View extend = LayoutInflater.from(holder.context).inflate(R.layout.cell_customer_details, holder.content, false);
            TextView quantityField = extend.findViewById(R.id.quantity_field);
            TextView productName = extend.findViewById(R.id.product_title);
            TextView orderDetails = extend.findViewById(R.id.order_details);

            productName.setText(orderedProduct.getCoffeeName());
            quantityField.setText(String.valueOf(orderedProduct.getQuantity()));
            orderDetails.setText(orderedProduct.getOrderDetails(context));

            holder.content.addView(extend);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView customerImage;
        private final TextView customerName;
        private final TextView orderStatus;
        private final TextView date;
        private final LinearLayout content;
        private final Context context;

        private WeakReference<OnOrdersClickListener> listenerRef;

        ViewHolder(View v, Context context, OnOrdersClickListener listener) {
            super(v);
            this.context = context;

            listenerRef = new WeakReference<>(listener);

            v.findViewById(R.id.cell_layout).setOnClickListener(v1 -> listenerRef.get().onOrderClicked(orders.get(getAdapterPosition())));
            customerImage = v.findViewById(R.id.customer_image);
            customerName = v.findViewById(R.id.order_owner);
            orderStatus = v.findViewById(R.id.order_status);
            date = v.findViewById(R.id.order_date);
            content = v.findViewById(R.id.content);
        }

        protected void updatePicture(String image) {
            GlideApp.with(context)
                    .load(image)
                    .fitCenter()
                    //.load(TextUtils.isEmpty(product.getImageUrl()) ? R.drawable.ic_profile_placeholder : product.getImageUrl())
                    .apply(new RequestOptions().circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(customerImage);
        }
    }
}