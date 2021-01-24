package com.skf.bingeflix.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skf.bingeflix.Activity.MovieplayerActivity
import com.skf.bingeflix.Models.Slide
import com.skf.bingeflix.R

class SlidePagerAdapter(private val context: Context, private val mlist: List<Slide>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slidelayout = inflater.inflate(R.layout.slide_item, null)
        val slideImg = slidelayout.findViewById<ImageView>(R.id.slider_img)
        val slideText = slidelayout.findViewById<TextView>(R.id.slider_title)
        val fab: FloatingActionButton = slidelayout.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener { context.startActivity(Intent(context, MovieplayerActivity::class.java)) }
        slideImg.setImageResource(mlist[position].image)
        slideText.text = mlist[position].title
        container.addView(slidelayout)
        return slidelayout
    }

    override fun getCount(): Int {
        return mlist.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}