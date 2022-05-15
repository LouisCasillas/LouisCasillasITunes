package com.example.louiscasillasitunes

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager2.widget.ViewPager2

import com.example.louiscasillasitunes.adapters.ViewPagerAdapter

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import louiscasillasitunes.R

class MainActivity : AppCompatActivity() {

    private lateinit var tabViewPager : ViewPager2
    private lateinit var tabTabLayout : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        tabViewPager = findViewById(R.id.tab_viewpager)
        tabTabLayout = findViewById(R.id.tab_layout)

        tabViewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tabTabLayout,tabViewPager){ tab, index ->
            tab.text = when(index){
                0 -> {"Classic"}
                1 -> {"Rock"}
                2 -> {"Pop"}
                else -> {throw Resources.NotFoundException("Tab not found")}
            }

            tab.icon = when(index){
                0 -> {
                    AppCompatResources.getDrawable(this, R.drawable.ic_music_classic)}
                1 -> {
                    AppCompatResources.getDrawable(this, R.drawable.ic_music_rock)}
                2 -> {
                    AppCompatResources.getDrawable(this, R.drawable.ic_music_pop)}
                else -> {throw Resources.NotFoundException("Tab not found")}
            }
        }.attach()

    }



}