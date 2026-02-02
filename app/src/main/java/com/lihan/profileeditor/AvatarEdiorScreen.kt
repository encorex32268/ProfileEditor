@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.profileeditor

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lihan.profileeditor.ui.theme.ProfileEditorTheme
import com.lihan.profileeditor.ui.theme.TextOnPrimary
import com.lihan.profileeditor.ui.theme.TextPrimary
import androidx.core.net.toUri

@Composable
fun AvatarEditorScreenRoot(
    uriString: String,
    onBack: () -> Unit,
    viewModel: AvatarEditorViewModel
){
    ObserveAsEvent(viewModel.uiEvent){ uiEvent ->
        when(uiEvent){
            AvatarEditorUiEvent.OnSavedSuccess -> onBack()
        }
    }
    AvatarEditorScreen(
        onBackClick = onBack,
        uriString = uriString,
        onSave = { imageBitmap , containerSize, rect ->
            viewModel.onSave(imageBitmap,containerSize,rect)
        }
    )

}


@Composable
fun AvatarEditorScreen(
    onBackClick: () -> Unit,
    onSave: (ImageBitmap, IntSize, Rect) -> Unit,
    modifier: Modifier = Modifier,
    uriString: String?=null
) {

    val density = LocalDensity.current

    val bitmap = if (uriString == null){
        null
    }else {
        rememberLoadBitmapFromUri(
            uriString.toUri()
        )
    }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    var cropSizePx by remember { mutableFloatStateOf(with(density) { 300.dp.toPx() }) }
    val currentCropSizeDp = with(density) { cropSizePx.toDp() }

    val cropSizeMax  = with(density) { 300.dp.toPx() }

    var cropOffset by remember { mutableStateOf(Offset(100f, 100f)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TextPrimary)
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarColors(
                containerColor = TextPrimary,
                navigationIconContentColor = TextOnPrimary,
                titleContentColor = TextOnPrimary,
                subtitleContentColor = TextOnPrimary,
                scrolledContainerColor = Color.Transparent,
                actionIconContentColor = TextOnPrimary
            ),
            title = {
                Text(
                    text = "Edit Avatar",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    color = TextOnPrimary
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "ArrowBack"
                    )
                }
            }
        )
        Spacer(Modifier.height(80.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(320.dp)
                .onGloballyPositioned {
                    containerSize = it.size
                }
        ) {
            if(bitmap != null){
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = bitmap,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                AvatarGridView(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit){
                            detectTransformGestures { _, pan, zoom, _ ->

                                val newSize = (cropSizePx * zoom).coerceIn(100f, cropSizeMax)
                                cropSizePx = newSize

                                val draggableLimitX = (containerSize.width.toFloat() - cropSizePx).coerceAtLeast(0f)
                                val draggableLimitY = (containerSize.height.toFloat() - cropSizePx).coerceAtLeast(0f)

                                val newX = (cropOffset.x + pan.x).coerceIn(0f, draggableLimitX)
                                val newY = (cropOffset.y + pan.y).coerceIn(0f, draggableLimitY)

                                cropOffset = Offset(newX, newY)
                            }
                        },
                    cropSize = currentCropSizeDp,
                    offset = cropOffset
                )

            }


        }

        Spacer(Modifier.height(80.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                if (bitmap == null) return@Button
                onSave(
                    bitmap,
                    containerSize,
                    Rect(cropOffset, Size(cropSizePx,cropSizePx))
                )
            }
        ) {
            Text(
                text = "Save",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = TextOnPrimary
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun AvatarEditorScreenPreview() {
    ProfileEditorTheme {
        AvatarEditorScreen(
            uriString = "",
            onBackClick = {},
            onSave = { _ , _ , _  ->

            }
        )
    }
}