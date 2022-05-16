package com.example.louiscasillasitunes.fragments

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

    fun getSongs(inflater: LayoutInflater)
    {
        musicType = requireArguments().getInt(MUSIC_KEY)

        if (musicType == CLASSIC){
            startRetrofit(inflater, ApiServiceITunes.createRetrofit().create(ApiServiceITunes::class.java).getClassicSongs())
        }else if(musicType == ROCK){
            startRetrofit(inflater, ApiServiceITunes.createRetrofit().create(ApiServiceITunes::class.java).getRockSongs())
        }else if(musicType == POP){
            startRetrofit(inflater, ApiServiceITunes.createRetrofit().create(ApiServiceITunes::class.java).getPopSongs())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_layout, container, false)

        rvSongList = view.findViewById(R.id.rv_songs)
        swipeRefresher = view.findViewById(R.id.swipe_refresher)

        getSongs(inflater)

        // TODO: tab_viewpager. set background color for frags

        swipeRefresher.setOnRefreshListener {

            swipeRefresher.isRefreshing = true

            getSongs(inflater)

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