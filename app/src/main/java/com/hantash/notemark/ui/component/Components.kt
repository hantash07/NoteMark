package com.hantash.notemark.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hantash.notemark.R
import com.hantash.notemark.ui.theme.OnPrimary
import com.hantash.notemark.ui.theme.OnSurface
import com.hantash.notemark.ui.theme.OnSurfaceOpacity12
import com.hantash.notemark.ui.theme.OnSurfaceVariant
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.SurfaceLowest
import com.hantash.notemark.ui.theme.Tertiary

enum class EnumSpacer {
    WIDTH,
    HEIGHT
}

@Composable
fun AppSpacer(dp: Dp, enumSpacer: EnumSpacer) {
    Spacer(
        modifier = if (enumSpacer == EnumSpacer.WIDTH) Modifier.width(dp)
        else Modifier.height(dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TopHeading(
    modifier: Modifier = Modifier,
    title: String = "Log In",
    message: String = "Capture your thoughts and ideas."
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    isEnable: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        enabled = isEnable,
        onClick = onClick,
    ) {
        if (!isLoading) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = SurfaceLowest
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    text: String = "Text Button",
    onClick: () -> Unit = {},
) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppOutlineButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    isEnable: Boolean = true,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color = Primary),
        enabled = isEnable,
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = Primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val gradientColorsFAB = listOf(
        Color(0xFF58A1F8), // light blue
        Color(0xFF5A4CF7)  // purple
    )

    Box(
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(corner = CornerSize(20.dp)),
                ambientColor = Color.Gray,
                spotColor = Color.DarkGray
            )
            .background(
                brush = Brush.linearGradient(gradientColorsFAB),
                shape = RoundedCornerShape(corner = CornerSize(20.dp))
            )
            .size(56.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Icon",
            tint = OnPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    text: String = "Log out",
    icon: Int = R.drawable.ic_logout,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Settings Icon",
            tint = Tertiary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(color = Tertiary)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteDetailSection(
    modifier: Modifier = Modifier,
    title: String = "Date created",
    description: String = "27 Jun 2025, 18:54"
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.titleSmall.copy(color = OnSurface)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditPreviewNote(
    modifier: Modifier = Modifier,
    onClickEdit: () -> Unit = {},
    onClickPreview: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .background(
                color = OnSurfaceOpacity12,
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            ),
    ) {
        IconButton(
            modifier = Modifier.padding(2.dp),
            onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit Icon"
            )
        }
        IconButton(
            modifier = Modifier.padding(2.dp),
            onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.ic_preview),
                contentDescription = "Preview Icon"
            )
        }
    }
}







