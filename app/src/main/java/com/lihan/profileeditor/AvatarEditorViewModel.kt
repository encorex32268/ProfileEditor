package com.lihan.profileeditor

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class AvatarEditorViewModel(
    private val avatarRepository: AvatarRepository
): ViewModel() {

    private val _uiEvent = Channel<AvatarEditorUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onSave(
        sourceBitmap: ImageBitmap,
        containerSize: IntSize,
        cropRect: Rect
    ){
        viewModelScope.launch{

            avatarRepository.saveToCache(
                bitmap = cropAvatar(
                    sourceBitmap = sourceBitmap ,
                    cropRect = cropRect ,
                    containerSize = containerSize
                )
            )
            _uiEvent.send(
                AvatarEditorUiEvent.OnSavedSuccess
            )
        }

    }

    private fun cropAvatar(
        sourceBitmap: ImageBitmap,
        cropRect: Rect,
        containerSize: IntSize
    ): Bitmap {
        val scaleX = containerSize.width.toFloat() / sourceBitmap.width
        val scaleY = containerSize.height.toFloat() / sourceBitmap.height
        val scale = max(scaleX, scaleY)

        val viewPortWidth = sourceBitmap.width * scale
        val viewPortHeight = sourceBitmap.height * scale

        //distance
        val offsetX = (containerSize.width - viewPortWidth) / 2
        val offsetY = (containerSize.height - viewPortHeight) / 2

        val realX = ((cropRect.left - offsetX) / scale).toInt().coerceAtLeast(0)
        val realY = ((cropRect.top - offsetY) / scale).toInt().coerceAtLeast(0)

        val realWidth = (cropRect.width / scale).toInt().coerceAtMost(sourceBitmap.width - realX)
        val realHeight = (cropRect.height / scale).toInt().coerceAtMost(sourceBitmap.height - realY)

        return Bitmap.createBitmap(
            sourceBitmap.asAndroidBitmap(),
            realX,
            realY,
            realWidth,
            realHeight
        )
    }
}