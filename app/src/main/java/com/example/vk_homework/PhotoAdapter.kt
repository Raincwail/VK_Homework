package com.example.vk_homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoAdapter (private val photoURLs : ArrayList<String>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>(){

    class PhotoViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val photoView : ImageView

        init {
            photoView = view.findViewById(R.id.photoImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_element, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoAdapter.PhotoViewHolder, position: Int) {
        Glide.with(holder.photoView.context).load(photoURLs[position]).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground).into(holder.photoView)
    }

    override fun getItemCount(): Int {
        return photoURLs.size
    }

}