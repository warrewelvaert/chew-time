package com.example.warre_welvaert_project_android.ui

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorScreen() {
    val ctx = LocalContext.current
    val bgColor = MaterialTheme.colors.secondary
    Toast.makeText(ctx, "Failed to fetch foods, try again.", Toast.LENGTH_LONG).show()
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 3
        val radius = size.width / 6

        drawCircle(
            color = bgColor,
            center = Offset(centerX, centerY),
            radius = radius
        )
        val exclamationMarkSize = radius / 2.5
        val offset = 30
        val exclamationMarkTop = centerY - exclamationMarkSize - offset
        val exclamationMarkBottom = centerY + exclamationMarkSize - offset
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(centerX - 7f, exclamationMarkTop.toFloat()),
            size = Size(14f, (exclamationMarkBottom - exclamationMarkTop).toFloat()),
            style = Stroke(width = 24f),
            cornerRadius = CornerRadius(x = 5.0f, y = 5.0f)
        )
        drawCircle(
            color = Color.White,
            center = Offset(centerX, (exclamationMarkBottom + exclamationMarkSize - offset).toFloat()),
            radius = radius / 10
        )
    }
}

@Preview
@Composable
fun PreviewCircleWithExclamationMark() {
    ErrorScreen()
}