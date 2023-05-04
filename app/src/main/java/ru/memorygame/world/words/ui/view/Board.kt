package ru.memorygame.world.words.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.memorygame.world.words.ui.theme.primaryBackground
import ru.memorygame.world.words.ui.theme.primaryText

@Composable
fun Board(
    modifier: Modifier = Modifier,
    text: String,
    width:Double,
    height:Double
) {
    Card(
        modifier = Modifier.padding(15.dp),
        shape = AbsoluteRoundedCornerShape(15.dp),
        backgroundColor = primaryBackground
    ) {
        Text(
            text = "$text ₽",
            fontWeight = FontWeight.W900,
            modifier = Modifier.padding(10.dp),
            color = primaryText,
            textAlign = TextAlign.Center
        )
    }
}