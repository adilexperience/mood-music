package com.android.moodmusic.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.moodmusic.R
import com.android.moodmusic.models.MusicDBModel
import com.android.moodmusic.models.MusicModel
import com.android.moodmusic.services.database.DatabaseHandler

class MusicAdapter(private val mList: List<MusicModel>, private val mood : String) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsMusicModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageBitmap(itemsMusicModel.image)

        // sets the text to the textview from our itemHolder class
        holder.titleTV.text = itemsMusicModel.title
        holder.descriptionTV.text = itemsMusicModel.displayName

        holder.cvAddToList.setOnClickListener{
//            v -> var response = DatabaseHandler(v.context).addMusic(MusicDBModel(itemsMusicModel.id, itemsMusicModel.albumID, mood, itemsMusicModel.title, itemsMusicModel.displayName, itemsMusicModel.image, itemsMusicModel.path))
            v -> DatabaseHandler(v.context).addMusic(MusicDBModel(mood, itemsMusicModel.title, itemsMusicModel.path))
            Toast.makeText(v.context, "Music Added", Toast.LENGTH_SHORT).show()

        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cvAddToList: CardView = itemView.findViewById(R.id.cv_add_to_list)
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val titleTV: TextView = itemView.findViewById(R.id.title)
        val descriptionTV: TextView = itemView.findViewById(R.id.description)
    }
}
