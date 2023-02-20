package com.antisoftware.popreminder.common.extension

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun Modifier.textButton(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)
}

fun Modifier.basicButton(): Modifier {
    return this.fillMaxWidth().height(64.dp).padding(16.dp, 8.dp)
}

fun Modifier.card(): Modifier {
    return this.padding(16.dp, 0.dp, 16.dp, 8.dp)
}

fun Modifier.contextMenu(): Modifier {
    return this.wrapContentWidth()
}

fun Modifier.dropdownSelector(): Modifier {
    return this.fillMaxWidth()
}

fun Modifier.fieldModifier(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 4.dp)
}

fun Modifier.toolbarActions(): Modifier {
    return this.wrapContentSize(Alignment.TopEnd)
}

fun Modifier.spacer(): Modifier {
    return this.fillMaxWidth().padding(12.dp)
}

fun Modifier.smallSpacer(): Modifier {
    return this.fillMaxWidth().height(8.dp)
}

fun Modifier.largeSpacer(): Modifier {
    return this.fillMaxWidth().height(32.dp)
}

fun Modifier.divider(): Modifier {
    return this.fillMaxWidth().padding(8.dp, 0.dp)
}

fun Modifier.profileImage(): Modifier {
    return this.fillMaxWidth().padding(64.dp, 32.dp).clip(CircleShape).aspectRatio(1f)
}

fun Modifier.basicImage(): Modifier {
    return this.fillMaxWidth().padding(64.dp, 32.dp)
}