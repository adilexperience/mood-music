package com.android.moodmusic.models

import android.graphics.Bitmap

data class MusicModel(val path: String, val title: String, val id: Long, val displayName: String, val albumID : String, val image: Bitmap)