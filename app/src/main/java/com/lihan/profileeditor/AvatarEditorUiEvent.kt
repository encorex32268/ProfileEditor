package com.lihan.profileeditor

sealed interface AvatarEditorUiEvent {
    data object OnSavedSuccess: AvatarEditorUiEvent
}