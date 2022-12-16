package com.example.vk_homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlbumAdapter (private val albumTitles : ArrayList<String>, private val albumDescs : ArrayList<String>, private val photoURLs : ArrayList<ArrayList<String>>): RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(view: View) :RecyclerView.ViewHolder(view) {

        val titleView : TextView = view.findViewById(R.id.card_title)
        val descView : TextView = view.findViewById(R.id.card_subtitle)
        val photoView : ImageView = view.findViewById(R.id.albumImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_element, parent, false)

        return AlbumViewHolder(view).listen { pos, type ->

            if (photoURLs[pos].isEmpty()) {
                Toast.makeText(view.context, "Sadly, this album does not contain any photos yet!", Toast.LENGTH_LONG).show()
            } else {
                (view.context as MainActivity).myBundle.putStringArrayList(
                    "album_photo_urls",
                    photoURLs[pos]
                )
                (view.context as MainActivity).myBundle.putString(
                    "album_name",
                    albumTitles[pos]
                )

                findNavController(view).navigate(R.id.action_SecondFragment_to_ThirdFragment)
            }

        }
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {

        var title : CharSequence = albumTitles[position]

        var description : CharSequence = albumDescs[position]
        if (description.isEmpty()){
            description = "No description"
        }

        holder.titleView.text = title
        holder.descView.text = description
        if (photoURLs[position].isEmpty()){
            Glide.with(holder.photoView.context).load(R.drawable.ic_launcher_foreground).into(holder.photoView)
        } else {
            Glide.with(holder.photoView.context).load(photoURLs[position][0])
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground).into(holder.photoView)
        }
    }

    override fun getItemCount(): Int {
        return albumTitles.size
    }

}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}

