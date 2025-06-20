package com.hantash.notemark.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.notemark.R
import com.hantash.notemark.ui.theme.OnSurface
import com.hantash.notemark.ui.theme.OnSurfaceVariant
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.Surface

enum class EnumInputType{
    EMAIL,
    PASSWORD,
    USERNAME
}

@Preview(showBackground = true)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    type: EnumInputType = EnumInputType.EMAIL,
    name: String = "Email",
    value: String = "",//hantash@gmail.com
    placeholder: String = "john.doe@example.com",
    supportingText: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPasswordVisible: Boolean = false,
    onValueChange: (String) -> Unit = {},
    onPasswordVisibility: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurface)
        )
        AppSpacer(dp = 4.dp, enumSpacer = EnumSpacer.HEIGHT)
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(48.dp)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = if (value.isNotEmpty()) Primary else Color.Transparent
                )
                .background(color =  if (value.isNotEmpty()) Color.Transparent else Surface, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    modifier = Modifier.weight(1f)
                        .padding(start = 10.dp, end = 10.dp),
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = OnSurface),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = imeAction
                    )
                )
                if (type == EnumInputType.PASSWORD) {
                    IconButton(onClick = onPasswordVisibility) {
                        Icon(
                            painter = painterResource(if (isPasswordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye_on),
                            contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password",
                            tint = OnSurfaceVariant
                        )
                    }
                }
            }
            if (value.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp),
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(color = OnSurfaceVariant)
                )
            }
        }
        if (supportingText.isNotEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 4.dp, start = 12.dp),
                text = supportingText,
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
            )
        }
    }
}
