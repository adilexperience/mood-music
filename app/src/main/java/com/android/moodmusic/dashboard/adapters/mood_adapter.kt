import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.moodmusic.R
import com.android.moodmusic.dashboard.CreatePlaylistActivity
import com.android.moodmusic.models.MoodModel

class MoodAdapter(private val mList: List<MoodModel>) : RecyclerView.Adapter<MoodAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mood_list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsMoodModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(itemsMoodModel.image)

        // sets the text to the textview from our itemHolder class
        holder.titleTV.text = itemsMoodModel.title
        holder.descriptionTV.text = itemsMoodModel.description

        holder.cvMoodItem.setOnClickListener {
            v ->
            var intent = Intent(v.context, CreatePlaylistActivity::class.java)
            intent.putExtra("mood", itemsMoodModel.uniqueKey)
            v.context.startActivity(intent)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cvMoodItem: CardView = itemView.findViewById(R.id.cv_mood_item)
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val titleTV: TextView = itemView.findViewById(R.id.title)
        val descriptionTV: TextView = itemView.findViewById(R.id.description)
    }
}
