package com.lihan.profileeditor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lihan.profileeditor.ui.theme.ProfileEditorTheme
import com.lihan.profileeditor.ui.theme.TextOnPrimary
import com.lihan.profileeditor.ui.theme.TextPrimary

@Composable
fun AvatarGridView(
    offset: Offset,
    cropSize: Dp = 300.dp,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    val gridLineColor = TextOnPrimary.copy(alpha = 0.4f)
    val maskColor = TextPrimary.copy(alpha = 0.7f)

    Canvas(modifier = modifier){
        val canvasWidth = size.width
        val canvasHeight = size.height
        val cropSizePx = cropSize.toPx()

        val rect = Rect(
            offset = offset,
            size = Size(cropSizePx,cropSizePx)
        )

        val maskPath = Path().apply {
            addRect(Rect(0f, 0f, canvasWidth, canvasHeight))
            addRect(rect)
            fillType = PathFillType.EvenOdd
        }
        drawPath(path = maskPath, color = maskColor)

        withTransform({
            translate(
                left = offset.x,
                top = offset.y
            )
        }){
            drawCircle(
                color = gridLineColor,
                radius = cropSizePx / 2,
                center = Offset(cropSizePx / 2, cropSizePx / 2),
                style = Stroke(width = strokeWidth)
            )
            val gridLinePath = Path().apply {
                moveTo(x = 0f, y = cropSizePx * 0.33f)
                lineTo(x = cropSizePx , y = cropSizePx * 0.33f)
                moveTo(x = 0f, y =cropSizePx * 0.66f)
                lineTo(x = cropSizePx , y = cropSizePx * 0.66f)
                moveTo(x = cropSizePx * 0.33f, y = 0f)
                lineTo(x = cropSizePx * 0.33f , y = cropSizePx)
                moveTo(x = cropSizePx * 0.66f, y = 0f)
                lineTo(x = cropSizePx * 0.66f , y = cropSizePx)
                close()
            }
            drawRect(
                color = gridLineColor,
                style = Stroke(
                    width = strokeWidth
                ),
                size = Size(
                    width = cropSizePx,
                    height = cropSizePx
                )
            )
            drawPath(
                path = gridLinePath,
                color = gridLineColor,
                style = Stroke(
                    width = strokeWidth
                )
            )

            val cropPath = Path().apply {
                val xWidth = cropSizePx * 0.33f * 0.33f
                val yHeight = cropSizePx * 0.33f * 0.33f
                //top left
                moveTo(x = 0f, y = 0f)
                lineTo(x = xWidth, y = 0f)
                moveTo(x = 0f, y = 0f)
                lineTo(x = 0f , y = yHeight)

                //top right
                moveTo(x = cropSizePx, y = 0f)
                lineTo(x = cropSizePx - xWidth , y = 0f)
                moveTo(x = cropSizePx, y = 0f)
                lineTo(x = cropSizePx , y = yHeight)

                //bottom left
                moveTo(x = 0f, y = cropSizePx)
                lineTo(x = xWidth , y = cropSizePx)
                moveTo(x = 0f, y = cropSizePx)
                lineTo(x = 0f , y = cropSizePx - yHeight)

                //bottom right
                moveTo(x = cropSizePx, y = cropSizePx)
                lineTo(x = cropSizePx - xWidth , y = cropSizePx)
                moveTo(x = cropSizePx, y = cropSizePx)
                lineTo(x = cropSizePx , y = cropSizePx - yHeight)

            }

            drawPath(
                path = cropPath,
                color = TextOnPrimary,
                style = Stroke(
                    width = strokeWidth * 2
                ),
            )
        }
    }

}


@Preview
@Composable
private fun AvatarGridViewPreview() {
    ProfileEditorTheme {
        AvatarGridView(
            modifier = Modifier.size(320.dp),
            offset = Offset(0f,0f)
        )
    }

}