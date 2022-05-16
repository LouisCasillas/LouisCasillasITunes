package com.example.louiscasillasitunes.fragments

import android.graphics.LinearGradient
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.louiscasillasitunes.apis.ApiServiceITunes
import com.example.louiscasillasitunes.model.ITunesResponse
import com.example.louiscasillasitunes.views.SongAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import louiscasillasitunes.R

class SongFragment : Fragment() {

    lateinit var rvSongList: RecyclerView
    lateinit var songAdapter: SongAdapter
    lateinit var swipeRefresher: SwipeRefreshLayout

    private var musicType : Int = CLASSIC

    companion object {

        const val MUSIC_KEY = "MUSIC_TYPE"

        const val CLASSIC = 0
        const val ROCK = 1
        const val POP = 2

        fun newInstance(musicType: Int) : SongFragment{
            val fragment = SongFragment()
            val bundle = Bundle()

            bundle.putInt(MUSIC_KEY, musicType)
            fragment.arguments = bundle

            return fragment
        }
    }

    fun setRandomBackground(view : View)
    {
        val randomBackground : GradientDrawable = GradientDrawable()
        val background_colors : IntArray = IntArray(2)
        background_colors[0] = (0x70000000 or (0..16777215).random()).toInt()
        background_colors[1] = (0x70000000 or (0..16777215).random()).toInt()

        randomBackground.colors = background_colors
        randomBackground.gradientType = android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT

        view.setBackground(randomBackground)
    }

    fun getSongs(inflater: LayoutInflater, view: View)
    {
        musicType = requireArguments().getInt(MUSIC_KEY)

        if (musicType == CLASSIC){
            startRetrofit(inflater, ApiServiceITunes.createRetrofit().create(ApiServiceITunes::class.java).getClassicSongs())
            setRandomBackground(view)
        }else if(musicType == ROCK){
            startRetrofit(inflater, ApiServiceITunes.createRetrofit().create(ApiServiceITunes::class.java).getRockSongs())
            setRandomBackground(view)
        }else if(musicType == POP){
            startRetrofit(inflater, ApiServiceITunes.createRetrofit().create(ApiServiceITunes::class.java).getPopSongs())
            setRandomBackground(view)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_layout, container, false)

        rvSongList = view.findViewById(R.id.rv_songs)
        swipeRefresher = view.findViewById(R.id.swipe_refresher)

        getSongs(inflater, view)

        // TODO: tab_viewpager. set background color for frags

        swipeRefresher.setOnRefreshListener {

            swipeRefresher.isRefreshing = true

            getSongs(inflater, view)

            // TODO: shuffle song cards
        }

        return view
    }

    private fun startRetrofit(inflater: LayoutInflater, call: Call<ITunesResponse>){
        call.enqueue(object: Callback<ITunesResponse>{
            override fun onResponse(
                call: Call<ITunesResponse>,
                response: Response<ITunesResponse>
            ) {
                if (response.isSuccessful){
                    songAdapter = SongAdapter(response.body()!!.results)
                    rvSongList.adapter = songAdapter
                    swipeRefresher.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                Toast.makeText(inflater.context, t.message, Toast.LENGTH_LONG).show()
                swipeRefresher.isRefreshing = false
            }
        })
    }
}