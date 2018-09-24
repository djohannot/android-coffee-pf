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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.injection.module.GlideApp;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import static com.ysdc.coffee.data.model.OrderStatus.CANCELED;
import static com.ysdc.coffee.data.model.OrderStatus.IN_PROGRESS;
import static com.ysdc.coffee.data.model.OrderStatus.PENDING;
import static com.ysdc.coffee.data.model.OrderStatus.READY;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private static final String EXTRA_SPACE = "  ";
    private List<Order> orders;
    private final OnOrdersClickListener listener;
    private final Context context;

    interface OnOrdersClickListener {
        void onOrderClicked(Order orderedProduct);
    }

    public OrderAdapter(List<Order> orders, OnOrdersClickListener listener, Context context) {
        this.orders = orders;
        this.listener = listener;
        this.context = context;
    }

    public void updateOrders(List<Order> orders) {
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
        Order order = orders.get(position);

        PrettyTime timeFormatter = new PrettyTime(new Date());
        holder.date.setText(timeFormatter.format(order.getDate()));
        switch (order.getStatus()) {
            case DONE:
                holder.orderStatus.setVisibility(View.GONE);
                break;
            case READY:
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.orderStatus.setText(EXTRA_SPACE + context.getString(READY.getLocalizableKey()) + EXTRA_SPACE);
                holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_round_corner_status_ready));
                break;
            case CANCELED:
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.orderStatus.setText(EXTRA_SPACE + context.getString(CANCELED.getLocalizableKey()) + EXTRA_SPACE);
                holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_round_corner_status_canceled));
                break;
            case PENDING:
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.orderStatus.setText(EXTRA_SPACE + context.getString(PENDING.getLocalizableKey()) + EXTRA_SPACE);
                holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_round_corner_status_pending));
                break;
            case IN_PROGRESS:
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.orderStatus.setText(EXTRA_SPACE + context.getString(IN_PROGRESS.getLocalizableKey()) + EXTRA_SPACE);
                holder.orderStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_round_corner_status_pending));
            default:
                break;
        }
        holder.content.removeAllViews();
        for(OrderEntry entry : order.getEntries()){
            final View extend = LayoutInflater.from(holder.context).inflate(R.layout.cell_order_entry, holder.content, false);

            if (!TextUtils.isEmpty(entry.getCoffeeImageUrl())) {
                ImageView productImage = extend.findViewById(R.id.product_image);
                holder.updatePicture(entry.getCoffeeImageUrl(), productImage);
            }
            TextView productTitle = extend.findViewById(R.id.product_title);
            productTitle.setText(entry.getCoffeeName());
            TextView productDetails = extend.findViewById(R.id.product_details);
            productDetails.setText(entry.getOrderDetails(holder.context));
            TextView quantityField = extend.findViewById(R.id.quantity_field);
            quantityField.setText(String.valueOf(entry.getQuantity()));

            holder.content.addView(extend);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
            orderStatus = v.findViewById(R.id.order_status);
            content = v.findViewById(R.id.content);
            date = v.findViewById(R.id.order_date);
        }

        protected void updatePicture(String image, ImageView view) {
            GlideApp.with(context)
                    .load(image)
                    //TODO: add placeholder to all image loaded with Glide
                    //.load(TextUtils.isEmpty(product.getImageUrl()) ? R.drawable.ic_profile_placeholder : product.getImageUrl())
                    .apply(new RequestOptions().circleCropTransform())
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
    }
}