package com.hantash.notemark.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.notemark.R
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_ADD_EDIT
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_LIST
import com.hantash.notemark.ui.theme.OnPrimary
import com.hantash.notemark.ui.theme.OnSurface
import com.hantash.notemark.ui.theme.OnSurfaceVariant
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.ui.theme.SurfaceLowest

@Preview(showBackground = true)
@Composable
fun BaseAppBar(
    modifier: Modifier = Modifier.fillMaxWidth(),
    enumScreen: EnumScreen = NOTE_LIST,
    title: String = "NoteMark",
    username: String = "",
    isAnimate: Boolean = false,
    isConnected: Boolean = false,
    onClickBackButton: () -> Unit = {},
    onClickSettings: () -> Unit = {},
    onClickProfile: () -> Unit = {},
    onClickSaveNote: () -> Unit = {},
) {
    val bgColor = when (enumScreen) {
        NOTE_LIST -> SurfaceLowest
        else -> Surface
    }

    val textStyle = when (enumScreen) {
        NOTE_LIST -> MaterialTheme.typography.titleMedium.copy(color = OnSurface)
        else -> MaterialTheme.typography.titleSmall.copy(color = OnSurfaceVariant)
    }

    val backBtn = when (enumScreen) {
        NOTE_ADD_EDIT -> Icons.Default.Close
        else -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
    }

    Surface(
        color = bgColor,
        modifier = modifier
            .wrapContentHeight()
            .statusBarsPadding()
    ) {
        AnimatedVisibility(
            visible = !isAnimate,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Back Button
                if (enumScreen != NOTE_LIST) {
                    IconButton(
                        onClick = onClickBackButton,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = backBtn,
                            contentDescription = "Back",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Title
                if (enumScreen != NOTE_ADD_EDIT) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = title,
                            style = textStyle,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .wrapContentWidth(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!isConnected && enumScreen == NOTE_LIST) {
                            Icon(
                                painter = painterResource(R.drawable.ic_offline),
                                contentDescription = "No Network Icon"
                            )
                        }
                    }
                }

                // Actions
                if (enumScreen == NOTE_LIST) {
                    Spacer(modifier = Modifier.weight(1f)) // Push actions to end

                    IconButton(
                        modifier = Modifier.size(40.dp),
                        onClick = onClickSettings
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = "Settings Icon"
                        )
                    }

                    if (username.isNotEmpty()) {
                        IconButton(
                            modifier = Modifier
                                .background(
                                    color = Primary,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .size(40.dp),
                            onClick = onClickProfile
                        ) {
                            Text(
                                text = username,
                                style = MaterialTheme.typography.titleSmall.copy(color = OnPrimary)
                            )
                        }
                    }
                }

                if (enumScreen == NOTE_ADD_EDIT) {
                    Spacer(modifier = Modifier.weight(1f)) // Push actions to end

                    AppTextButton(text = "SAVE NOTE", onClick = onClickSaveNote)
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun BaseAppBarOLD(
//    modifier: Modifier = Modifier,
//    enumScreen: EnumScreen = NOTE_DETAIL,
//    title: String = "NoteMark",
//    username: String = "",
//    onClickBackButton: () -> Unit = {},
//    onClickSettings: () -> Unit = {},
//    onClickProfile: () -> Unit = {},
//    onClickSaveNote: () -> Unit = {},
//) {
//    val bgColor = when(enumScreen) {
//        NOTE_LIST -> SurfaceLowest
//        else -> Surface
//    }
//
//    val textStyle = when(enumScreen) {
//        NOTE_LIST -> MaterialTheme.typography.titleMedium.copy(color = OnSurface)
//        else -> MaterialTheme.typography.titleSmall.copy(color = OnSurfaceVariant)
//    }
//
//    val backBtn = when (enumScreen) {
//        NOTE_ADD_EDIT -> Icons.Default.Close
//        else -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
//    }
//
//    TopAppBar(
//        modifier = modifier,
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = bgColor
//        ),
//        title = {
//            if (enumScreen != NOTE_ADD_EDIT) {
//                Text(
//                    text = title,
//                    style = textStyle,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        },
//        navigationIcon = {
//            if (enumScreen != NOTE_LIST) {
//                IconButton(onClick = onClickBackButton) {
//                    Icon(
//                        modifier = Modifier.size(20.dp),
//                        imageVector = backBtn,
//                        contentDescription = "Close Icon",
//                        tint = OnSurfaceVariant
//                    )
//                }
//            }
//        },
//        actions = {
//            if (enumScreen == NOTE_LIST) {
//                IconButton(
//                    modifier = Modifier.size(40.dp),
//                    onClick = onClickSettings
//                ) {
//                    Image(
//                        painter = painterResource(R.drawable.ic_settings),
//                        contentDescription = "Settings Icon"
//                    )
//                }
//
//                if (username.isNotEmpty()) {
//                    IconButton(
//                        modifier = Modifier
//                            .background(
//                                color = Primary,
//                                shape = RoundedCornerShape(corner = CornerSize(12.dp))
//                            )
//                            .size(40.dp),
//                        onClick = onClickProfile
//                    ) {
//                        Text(
//                            text = username,
//                            style = MaterialTheme.typography.titleSmall.copy(color = OnPrimary)
//                        )
//                    }
//                }
//            }
//
//            if (enumScreen == NOTE_ADD_EDIT) {
//                AppTextButton(text = "SAVE NOTE", onClick = onClickSaveNote)
//            }
//        }
//    )
//}

