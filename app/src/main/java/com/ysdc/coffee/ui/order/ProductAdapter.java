package com.ysdc.coffee.ui.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> products;
    private final OnPropertyClickListener listener;
    private final Context context;

    interface OnPropertyClickListener {
        void onPropertyClicked(Product property);
    }

    public ProductAdapter(List<Product> products, OnPropertyClickListener listener, Context context) {
        this.products = products;
        this.listener = listener;
        this.context = context;
    }

    public void updateProperties(List<Product> products) {
        this.products = products;
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
//        if (!property.getImages().isEmpty()) {
//            //TODO: check this: property.getDefaultImageIndex()
//            holder.updatePicture(property.getImages().get(0));
//        } else {
//            holder.updatePicture(null);
//        }
//        holder.price.setText(property.getPrice());
//        holder.title.setText(property.getTitle());
//        holder.location.setText(property.getFullFormattedLocation());
//        holder.type.setText(property.getType());
//
//        if (property.getBedroomId() != null) {
//            holder.bedImg.setVisibility(View.VISIBLE);
//            holder.bed.setText(Bedroom.fromId(property.getBedroomId()).getKey());
//        } else {
//            holder.bedImg.setVisibility(View.GONE);
//            holder.bed.setText(EMPTY_STRING);
//        }
//        if (property.getBathroomId() != null) {
//            holder.bathImg.setVisibility(View.VISIBLE);
//            holder.bath.setText(Bathroom.fromId(property.getBathroomId()).getKey());
//        } else {
//            holder.bathImg.setVisibility(View.GONE);
//            holder.bath.setText(EMPTY_STRING);
//        }
//        if (property.isPremium()) {
//            holder.statusField.setVisibility(View.VISIBLE);
//            holder.statusField.setTextColor(context.getResources().getColor(R.color.gold));
//            holder.statusField.setText(R.string.property_premium);
//        } else if (property.isFeatured()) {
//            holder.statusField.setVisibility(View.VISIBLE);
//            holder.statusField.setTextColor(context.getResources().getColor(R.color.button_blue_color));
//            holder.statusField.setText(R.string.property_featured);
//        } else {
//            holder.statusField.setVisibility(View.GONE);
//        }
//
//        if (property.isVerified()) {
//            holder.verifiedField.setVisibility(View.VISIBLE);
//            holder.verifiedImg.setVisibility(View.VISIBLE);
//        } else {
//            holder.verifiedField.setVisibility(View.GONE);
//            holder.verifiedImg.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        private final ImageView img;
//        private final TextView price;
//        private final TextView title;
//        private final TextView location;
//        private final TextView type;
//        private final ImageView bedImg;
//        private final TextView bed;
//        private final ImageView bathImg;
//        private final TextView bath;
//        private final TextView statusField;
//        protected final Context context;
//        private final ImageView verifiedImg;
//        private final TextView verifiedField;
//        private WeakReference<OnPropertyClickListener> listenerRef;

        ViewHolder(View v, Context context, OnPropertyClickListener listener) {
            super(v);
//            this.context = context;
//
//            listenerRef = new WeakReference<>(listener);
//
//            v.findViewById(R.id.cell_layout).setOnClickListener(v1 -> listenerRef.get().onPropertyClicked(properties.get(getAdapterPosition())));
//            img = v.findViewById(R.id.property_img);
//            price = v.findViewById(R.id.property_price);
//            title = v.findViewById(R.id.property_title);
//            location = v.findViewById(R.id.property_location);
//            type = v.findViewById(R.id.property_type);
//            bedImg = v.findViewById(R.id.property_bed_img);
//            bed = v.findViewById(R.id.property_bed_value);
//            bathImg = v.findViewById(R.id.property_bath_img);
//            bath = v.findViewById(R.id.property_bath_value);
//            statusField = v.findViewById(R.id.statusField);
//            verifiedImg = v.findViewById(R.id.verified_img);
//            verifiedField = v.findViewById(R.id.verified_Field);
        }

//        protected void updatePicture(PropertyImage image) {
//            GlideUtils.loadImage(img, image.getThumbnail(), new Pair<>(150, 150));
//        }
    }
}