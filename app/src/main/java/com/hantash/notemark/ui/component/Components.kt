package com.hantash.notemark.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier.fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        enabled = isEnable,
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
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










