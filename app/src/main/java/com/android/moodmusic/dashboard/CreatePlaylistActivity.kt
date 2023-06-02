package com.android.moodmusic.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.moodmusic.R
import com.android.moodmusic.dashboard.adapters.MusicAdapter
import com.android.moodmusic.models.MusicModel
import java.io.FileNotFoundException
import java.io.IOException

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var rvAllMusic: RecyclerView
    private val PERMISSION_REQUEST_CODE = 101
    private lateinit var playlistType : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)

        playlistType = intent.getStringExtra("mood").toString()
        supportActionBar?.title = "Make $playlistType songs playlist"


        rvAllMusic = findViewById<RecyclerView>(R.id.rv_all_music)

        // this creates a vertical layout Manager
        rvAllMusic.layoutManager = LinearLayoutManager(this)

        checkAndRequestPermission()
    }

    fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(baseContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission is already granted, so you can proceed with reading the file
            readAudioFiles()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted, so you can read the file
                    readAudioFiles()
                } else {
                    // Permission denied, so you can't read the file
                    deniedPrompt()
                }
                return
            }

            // Handle other request codes
        }
    }

    private fun readAudioFiles() {
        val projection = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID,
        )

        val selection = "${MediaStore.Audio.Media.MIME_TYPE}=?"
        val selectionArgs = arrayOf("audio/mpeg")

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        val audioFiles = ArrayList<MusicModel>()

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val displayName = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val path = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val title = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val albumID = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

                    lateinit var image : Bitmap
                    try {
                        val albumIdColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                        val albumId = cursor.getLong(albumIdColumnIndex)

                        val albumArtUri: Uri = Uri.parse("content://media/external/audio/albumart")
                        image = MediaStore.Images.Media.getBitmap(contentResolver, ContentUris.withAppendedId(albumArtUri, albumId))
                    }
                    catch (e: FileNotFoundException) {
                        image = BitmapFactory.decodeResource(resources, R.drawable.music_album)
                    }

                    audioFiles.add(MusicModel(path, title, id, displayName, albumID, image))
                } while (it.moveToNext())
            }
        }

        // This will pass the ArrayList to our Adapter
        val adapter = MusicAdapter(audioFiles, playlistType)

        // Setting the Adapter with the recyclerview
        rvAllMusic.adapter = adapter


        // below code to play music
        val mediaPlayer = MediaPlayer()

        try {
//            mediaPlayer.setDataSource(audioFiles[50].path)
//            mediaPlayer.prepare()
//            mediaPlayer.start()
        } catch (e: IOException) {
            // Handle the error
        }

        cursor?.close()
    }

    private fun deniedPrompt() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission Denied")
        builder.setMessage("This app needs the permission to read external storage to function properly. Please grant the permission in the app settings.")
        builder.setPositiveButton("Go to Settings") { _, _ ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        val dialog = builder.create()
        dialog.show()
    }
}