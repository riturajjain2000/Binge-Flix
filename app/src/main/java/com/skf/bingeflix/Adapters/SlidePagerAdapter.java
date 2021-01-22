package com.skf.bingeflix.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skf.bingeflix.Activity.MovieplayerActivity;
import com.skf.bingeflix.R;
import com.skf.bingeflix.Models.Slide;

import java.util.List;

public class SlidePagerAdapter extends PagerAdapter {

    private Context context;
    private List<Slide> mlist;



    public SlidePagerAdapter(Context context, List<Slide> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slidelayout = inflater.inflate(R.layout.slide_item, null);

        ImageView slideImg = slidelayout.findViewById(R.id.slider_img);
        TextView slideText = slidelayout.findViewById(R.id.slider_title);
        FloatingActionButton fab = slidelayout.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,MovieplayerActivity.class));
            }
        });


        slideImg.setImageResource(mlist.get(position).getImage());
        slideText.setText(mlist.get(position).getTitle());
        container.addView(slidelayout);
        return slidelayout;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}
