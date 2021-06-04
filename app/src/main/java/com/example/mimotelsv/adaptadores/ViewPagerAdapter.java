package com.example.mimotelsv.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.mimotelsv.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {
    // Context object
    Context context;

    // Array of images
   List<String> images = new ArrayList<>();

    // Layout Inflater
    LayoutInflater mLayoutInflater;


    // Viewpager Constructor
    public ViewPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(images.get(position),Base64.DEFAULT | Base64.NO_WRAP);

        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        // setting the image in the imageView
        imageView.setImageBitmap(bmp);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}
