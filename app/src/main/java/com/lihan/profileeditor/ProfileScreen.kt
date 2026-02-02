package com.lihan.profileeditor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.profileeditor.ui.theme.BG
import com.lihan.profileeditor.ui.theme.Outline
import com.lihan.profileeditor.ui.theme.ProfileEditorTheme
import com.lihan.profileeditor.ui.theme.TextOnPrimary
import com.lihan.profileeditor.ui.theme.TextPrimary
import com.lihan.profileeditor.ui.theme.TextSecondary

@Composable
fun ProfileScreenRoot(
    onNavigateToAvatarEditorScreen: (String) -> Unit,
    viewModel: ProfileViewModel
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (state.imageUri == Uri.EMPTY){
            viewModel.loadImage()
        }
    }

    ProfileScreen(
        state = state,
        onNavigateToAvatarEditor = {
            onNavigateToAvatarEditorScreen(it.toString())
        }
    )

}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onNavigateToAvatarEditor: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) {  uri: Uri? ->
        if (uri != null){
            onNavigateToAvatarEditor(uri)
        }
    }

    val bitmap = if (state.imageUri == Uri.EMPTY){
        null
    }else {
        rememberLoadBitmapFromUri(state.imageUri)
    }

    Column(
        modifier = modifier
            .background(color = BG)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            color = TextPrimary
        )
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Outline,
                    shape = CircleShape
                )
                .padding(8.dp)
        ){
            if (bitmap == null){
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(color = Color.DarkGray)
                        .clickable(onClick = {
                            pickMedia.launch("image/*")
                        }),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = TextOnPrimary
                    )
                }

            }else{
                Image(
                    bitmap = bitmap,
                    contentDescription = "Image",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        OutlinedButton(
            onClick = {
                pickMedia.launch("image/*")
            }
        ) {
            Text(
                text = "Change Avatar",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = TextSecondary
            )
        }
    }

}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileEditorTheme {
        ProfileScreen(
            state = ProfileState(),
            onNavigateToAvatarEditor = {}
        )
    }
}