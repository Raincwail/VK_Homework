package com.example.vk_homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class SecondFragment : Fragment() {

    private lateinit var albumTitles : ArrayList<String>
    private lateinit var albumDescs : ArrayList<String>
    private lateinit var photoURLs : ArrayList<ArrayList<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        albumTitles = (activity as MainActivity).myBundle.get("album_titles") as ArrayList<String>
        albumDescs = (activity as MainActivity).myBundle.get("album_descs") as ArrayList<String>
        photoURLs = (activity as MainActivity).myBundle.get("photo_urls") as ArrayList<ArrayList<String>>
        return inflater.inflate(R.layout.fragment_second, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView : RecyclerView = requireView().findViewById(R.id.albumRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = AlbumAdapter(albumTitles, albumDescs, photoURLs)

    }

}