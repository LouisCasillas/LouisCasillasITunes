package com.example.louiscasillasitunes.adapters

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.louiscasillasitunes.fragments.SongFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {SongFragment.newInstance(SongFragment.CLASSIC)}
            1 -> {SongFragment.newInstance(SongFragment.ROCK)}
            2 -> {SongFragment.newInstance(SongFragment.POP)}
            else -> {throw Resources.NotFoundException("Tab not found")}
        }
    }

    override fun getItemCount() = 3
}