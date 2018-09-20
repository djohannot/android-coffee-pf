package com.ysdc.coffee.utils;

import android.support.v4.util.Pair;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ysdc.coffee.injection.module.GlideApp;

/**
 * Wrapper class to add authorization header on Glide call
 */
public class GlideUtils {

    public static void loadImage(ImageView imageView, String url, Pair<Integer, Integer> widthHeightPair) {
        GlideUrl glideUrl = new GlideUrl(url);

        if (url != null) {
            GlideApp.with(imageView.getContext())
                    .load(glideUrl)
                    .centerCrop()
                    //TODO: Placeholder
                    //.placeholder(R.drawable.ic_property_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            //TODO: Placeholder
//            GlideApp.with(imageView.getContext())
//                    .load(R.drawable.ic_no_image_placeholder)
//                    .override(widthHeightPair.first, widthHeightPair.second)
//                    .fitCenter()
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imageView);
        }
    }
}
