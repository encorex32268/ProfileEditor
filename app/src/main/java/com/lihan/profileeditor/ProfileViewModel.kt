package com.lihan.profileeditor

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.graphics.BitmapCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val avatarRepository: AvatarRepository
): ViewModel(){

    private val _state = MutableStateFlow(ProfileState())
    val state  = _state.asStateFlow()

    fun loadImage(){
        viewModelScope.launch {
            _state.update { it.copy(
                imageUri = avatarRepository.loadFromCache()
            ) }
        }
    }
}