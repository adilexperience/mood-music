package com.android.moodmusic.services.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.android.moodmusic.R
import com.android.moodmusic.models.MusicDBModel
import java.io.ByteArrayOutputStream

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DatabaseHandler.DB_NAME, null, DatabaseHandler.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                ID + " INTEGER PRIMARY KEY," +
//                SONG_FILE_ID + " INTEGER," +
//                ALBUM_ID + " TEXT," +
                SONG_MOOD + " TEXT," +
                TITLE + " TEXT," +
//                DISPLAY_NAME + " TEXT," +
//                IMAGE + " BLOB," +
                PATH + " TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    companion object {

        private val DB_VERSION = 1
        private val DB_NAME = "MusicMoods"
        private val TABLE_NAME = "Music"

        private val ID = "ID"
        private val SONG_FILE_ID = "SongFileID"
        private val ALBUM_ID = "AlbumID"
        private val SONG_MOOD = "SongMood"
        private val TITLE = "Title"
        private val DISPLAY_NAME = "DisplayName"
        private val IMAGE = "Image"
        private val PATH = "Path"
    }

    fun addMusic(music: MusicDBModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

//        val stream = ByteArrayOutputStream()
//        music.image.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        val imageByteArray = stream.toByteArray()

//        values.put(SONG_FILE_ID, music.songFileId)
//        values.put(ALBUM_ID, music.albumID)
        values.put(SONG_MOOD, music.songMood)
        values.put(TITLE, music.title)
//        values.put(DISPLAY_NAME, music.displayName)
//        values.put(IMAGE, imageByteArray)
        values.put(PATH, music.path)
        val _success = db.insert(TABLE_NAME, null, values)
        Log.e("DATA_MOOD", _success.toString())
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    @SuppressLint("Range")
    fun getTask(mood: String): ArrayList<MusicDBModel> {
        var musics = ArrayList<MusicDBModel>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                musics.add(
                    MusicDBModel(
//                    cursor.getLong(cursor.getColumnIndex(SONG_FILE_ID)),
//                    cursor.getString(cursor.getColumnIndex(ALBUM_ID)),
                    mood,
                    cursor.getString(cursor.getColumnIndex(TITLE)),
//                    cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)),
//                    BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex(IMAGE)), 0, cursor.getBlob(cursor.getColumnIndex(IMAGE)).size),
                    cursor.getString(cursor.getColumnIndex(PATH)),
                    )
                )
            }
        }
        cursor.close()
        return musics
    }
}