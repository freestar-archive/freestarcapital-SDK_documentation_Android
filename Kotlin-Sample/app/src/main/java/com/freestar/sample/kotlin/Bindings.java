package com.freestar.sample.kotlin;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Bindings {
    
   @androidx.databinding.BindingAdapter({"bind:imageUrl"})
   public static void setImageUrl(ImageView imageView, String url) {
      Glide.with(imageView).load(url).into(imageView);
      Log.d("Bindings","setImageUrl: imageView: " + imageView + " url: " + url);
   }

}
