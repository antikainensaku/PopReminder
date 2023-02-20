package com.antisoftware.popreminder.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DividerText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.primary
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Divider(modifier = Modifier.weight(1f), thickness = 2.dp, color = color)
        Text(
            text = text,
            modifier = Modifier.padding(8.dp, 0.dp),
            color = color,
            fontSize = 14.sp
        )
        Divider(modifier = Modifier.weight(1f), thickness = 2.dp, color = color)
    }
}