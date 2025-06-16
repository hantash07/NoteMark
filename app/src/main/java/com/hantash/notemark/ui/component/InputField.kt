package com.hantash.notemark.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    name: String = "Email",
    value: String = "",
    onValueChange: (String) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
        AppSpacer(dp = 8.dp, enumSpacer = EnumSpacer.HEIGHT)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            value = value,
            onValueChange = onValueChange
        )
    }
}
