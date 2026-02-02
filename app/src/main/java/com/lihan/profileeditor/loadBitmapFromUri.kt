package com.lihan.profileeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@SuppressLint("NewApi")
@Composable
fun rememberLoadBitmapFromUri(uri: Uri?): ImageBitmap? {
    val context = LocalContext.current
    if (uri==null || uri.toString().isEmpty()) return null
    val source = ImageDecoder.createSource(context.contentResolver,uri)
    return remember {
        ImageDecoder.decodeBitmap(source).asImageBitmap()
    }
}