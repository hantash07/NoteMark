package com.hantash.notemark.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.theme.OnSurface
import com.hantash.notemark.ui.theme.OnSurfaceVariant
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.SurfaceLowest
import com.hantash.notemark.utils.toReadableDate

@Preview(showBackground = true)
@Composable
fun NoteItem(
    note: Note = Note(),
    maxLength: Int = 150,
    onPreview: (Note) -> Unit = {},
    onDelete: (Note) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onDelete.invoke(note) },
                    onTap = { onPreview.invoke(note) }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = SurfaceLowest
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.createdAt.toReadableDate(),
                style = MaterialTheme.typography.bodyMedium.copy(color = Primary)
            )
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium.copy(color = OnSurface)
            )
            Text(
                text = truncateText(note.content, maxLength),
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
            )
        }
    }
}

private fun truncateText(text: String, maxLength: Int): String {
    return if (text.length > maxLength) {
        text.take(maxLength) + "..."
    } else {
        text
    }
}
