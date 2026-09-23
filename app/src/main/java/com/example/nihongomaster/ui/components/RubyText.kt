package com.example.nihongomaster.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nihongomaster.model.FuriganaMode

@Composable
fun RubyText(
    japanese: String,
    reading: String = "",
    furiganaMode: FuriganaMode = FuriganaMode.ALWAYS,
    fontSize: TextUnit = 16.sp,
    modifier: Modifier = Modifier
) {
    var isRevealed by remember { mutableStateOf(false) }

    val shouldShowFurigana = when (furiganaMode) {
        FuriganaMode.ALWAYS -> true
        FuriganaMode.OFF -> false
        FuriganaMode.TAP_TO_REVEAL -> isRevealed
    }

    Column(
        modifier = modifier
            .clickable(enabled = furiganaMode == FuriganaMode.TAP_TO_REVEAL) {
                isRevealed = !isRevealed
            }
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (reading.isNotBlank() && shouldShowFurigana) {
            Text(
                text = reading,
                fontSize = (fontSize.value * 0.65f).sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                lineHeight = (fontSize.value * 0.75f).sp
            )
        }
        Text(
            text = japanese,
            fontSize = fontSize,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            lineHeight = (fontSize.value * 1.3f).sp
        )
    }
}
