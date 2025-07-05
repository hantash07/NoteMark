package com.hantash.notemark.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.notemark.ui.theme.OnSurface
import com.hantash.notemark.ui.theme.OnSurfaceVariant
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.SurfaceLowest

@Preview(showBackground = true)
@Composable
fun NoteItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "7  Mar",
                style = MaterialTheme.typography.bodyMedium.copy(color = Primary)
            )
            Text(
                text = "Title",
                style = MaterialTheme.typography.titleMedium.copy(color = OnSurface)
            )
            Text(
                text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
            )
        }
    }
}