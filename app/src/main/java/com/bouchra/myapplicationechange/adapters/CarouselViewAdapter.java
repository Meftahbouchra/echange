package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.PosteModelAnnonce;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class CarouselViewAdapter  extends CarouselView {
    private  int [] mImages = new int[]{
           R.drawable.img_sac,R.drawable.user
    };

    public CarouselViewAdapter(Context context, ArrayList<PosteModelAnnonce> posts) {
        super(context);
        CarouselView carouselView;
        carouselView= findViewById(R.id.image_annonce);
        carouselView.setPageCount(mImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);
            }
        });
    }




}
