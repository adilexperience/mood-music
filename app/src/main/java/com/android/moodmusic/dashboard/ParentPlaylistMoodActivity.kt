package com.android.moodmusic.dashboard

import MoodAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.moodmusic.R
import com.android.moodmusic.models.MoodModel
import com.android.moodmusic.models.MusicDBModel
import com.android.moodmusic.services.database.DatabaseHandler

class ParentPlaylistMoodActivity : AppCompatActivity() {

    override fun onStart() {
//        checkIfListHaveData()
        super.onStart()
    }

    fun checkIfListHaveData() {
        var angrySize = DatabaseHandler(baseContext).getTask("angry").size
        var happySize = DatabaseHandler(baseContext).getTask("happy").size
        var sadSize = DatabaseHandler(baseContext).getTask("sad").size

        if((angrySize > 0) && (happySize > 0) && (sadSize > 0)) {
            Intent(baseContext, RandomMusicPlayingActivity::class.java)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_playlist_mood)
        supportActionBar?.title = "Create playlist"

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.rv_moods)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<MoodModel>()

        data.add(MoodModel(R.drawable.happy, "Feeling happy !", "Create playlist with your most loved songs to play when you're happy", "happy"))
        data.add(MoodModel(R.drawable.sad, "Feeling Sad ?", "Create playlist with your relaxing music of your choice to help you", "sad"))
        data.add(MoodModel(R.drawable.angry, "Feeling Angry !", "Create playlist with songs which you like to hear when in anger to boost you up", "angry"))

        // This will pass the ArrayList to our Adapter
        val adapter = MoodAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

    }
}