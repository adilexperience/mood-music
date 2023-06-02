package com.android.moodmusic.models

import android.graphics.Bitmap

//data class MusicDBModel(var songFileId: Long, var albumID : String, val songMood : String, var title: String, var displayName: String, var image: Bitmap, var path: String)
data class MusicDBModel(val songMood : String, var title: String, var path: String)