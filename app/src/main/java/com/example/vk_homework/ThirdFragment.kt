package com.example.vk_homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ThirdFragment : Fragment() {

    private lateinit var albumName : String
    private lateinit var photoURLs : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        albumName = (activity as MainActivity).myBundle.get("album_name") as String
        photoURLs = (activity as MainActivity).myBundle.get("album_photo_urls") as ArrayList<String>

        (activity as AppCompatActivity).supportActionBar?.title = albumName

        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView : RecyclerView = requireView().findViewById(R.id.photoRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PhotoAdapter(photoURLs)
    }

}