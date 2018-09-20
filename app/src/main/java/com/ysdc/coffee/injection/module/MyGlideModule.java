package com.ysdc.coffee.injection.module;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

//TODO: Sadly Glide is not yet ready for AndroidX, so till they do an update i cannot use it
@GlideModule
public class MyGlideModule extends AppGlideModule {
}
