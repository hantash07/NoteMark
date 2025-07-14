package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.EditPreviewNote
import com.hantash.notemark.ui.component.NoteDetailSection
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.theme.OnSurfaceOpacity12
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.utils.localScreenOrientation

@Composable
fun NoteDetailScreen(onNavigateTo: (EnumScreen) -> Unit, onNavigateBack: () -> Unit) {
    val modifier = when (localScreenOrientation.current) {
        DevicePosture.MOBILE_PORTRAIT -> Modifier.fillMaxWidth()
        else -> Modifier.width(540.dp)
    }
    NoteDetailContent(modifier, onNavigateBack = {
        onNavigateBack.invoke()
    })
}

@Composable
private fun NoteDetailContent(
    modifier: Modifier = Modifier.fillMaxSize(),
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            BaseAppBar(
                enumScreen = EnumScreen.NOTE_DETAIL,
                title = "ALL NOTES",
                onClickBackButton = onNavigateBack
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(color = Surface),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Note Title",
                        style = MaterialTheme.typography.titleLarge
                    )

                    HorizontalDivider(color = OnSurfaceOpacity12)

                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        NoteDetailSection(
                            modifier = Modifier.weight(1f),
                            title = "Date created",
                            description = "27 Jun 2025, 18:54"
                        )
                        NoteDetailSection(
                            modifier = Modifier.weight(1f),
                            title = "Last edited",
                            description = "Just now"
                        )
                    }

                    HorizontalDivider(color = OnSurfaceOpacity12)

                    Text(
                        modifier = Modifier.weight(1f).padding(16.dp),
                        text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. ",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    EditPreviewNote(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onClickEdit = {},
                        onClickPreview = {}
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewPortrait() {
    NoteDetailContent()
}

@Preview(showBackground = true, widthDp = 700, heightDp = 350)
@Composable
private fun PreviewLandscape() {
    NoteDetailContent()
}