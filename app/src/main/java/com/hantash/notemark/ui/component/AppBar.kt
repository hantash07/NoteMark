package com.hantash.notemark.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.notemark.R
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_ADD_EDIT
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_LIST
import com.hantash.notemark.ui.navigation.EnumScreen.SETTINGS
import com.hantash.notemark.ui.theme.OnPrimary
import com.hantash.notemark.ui.theme.OnSurface
import com.hantash.notemark.ui.theme.OnSurfaceVariant
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.ui.theme.SurfaceLowest

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BaseAppBar(
    modifier: Modifier = Modifier,
    enumScreen: EnumScreen = NOTE_LIST,
    title: String = "NoteMark",
    onClickBackButton: () -> Unit = {},
    onClickSettings: () -> Unit = {},
    onClickProfile: () -> Unit = {},
    onClickSaveNote: () -> Unit = {},
) {
    val bgColor = when(enumScreen) {
        NOTE_LIST -> Color.White
        else -> Surface
    }

    val textStyle = when(enumScreen) {
        NOTE_LIST -> MaterialTheme.typography.titleMedium.copy(color = OnSurface)
        else -> MaterialTheme.typography.titleSmall.copy(color = OnSurfaceVariant)
    }

    val backBtn = when (enumScreen) {
        NOTE_ADD_EDIT -> Icons.Default.Close
        else -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
    }

    TopAppBar(
//        modifier = Modifier.height(56.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = bgColor
        ),
        title = {
            if (enumScreen != NOTE_ADD_EDIT) {
                Text(
                    text = title,
                    style = textStyle
                )
            }
        },
        navigationIcon = {
            if (enumScreen != NOTE_LIST) {
                IconButton(onClick = onClickBackButton) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = backBtn,
                        contentDescription = "Close Icon",
                        tint = OnSurfaceVariant
                    )
                }
            }
        },
        actions = {
            if (enumScreen == NOTE_LIST) {
                IconButton(
                    modifier = Modifier.size(40.dp),
                    onClick = onClickSettings
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = "Settings Icon"
                    )
                }

                IconButton(
                    modifier = Modifier
                        .background(
                            color = Primary,
                            shape = RoundedCornerShape(corner = CornerSize(12.dp))
                        )
                        .size(40.dp),
                    onClick = onClickProfile
                ) {
                    Text(
                        text = "HN",
                        style = MaterialTheme.typography.titleSmall.copy(color = OnPrimary)
                    )
                }
            }

            if (enumScreen == NOTE_ADD_EDIT) {
                AppTextButton(text = "SAVE NOTE", onClick = onClickSaveNote)
            }
        }
    )
}
