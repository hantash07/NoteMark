package com.hantash.notemark.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hantash.notemark.R
import com.hantash.notemark.ui.component.EnumNoteField.TITLE
import com.hantash.notemark.ui.theme.OnSurface
import com.hantash.notemark.ui.theme.OnSurfaceOpacity12
import com.hantash.notemark.ui.theme.OnSurfaceVariant
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.ui.theme.Tertiary

enum class EnumInputType {
    EMAIL,
    PASSWORD,
    USERNAME
}

enum class EnumNoteField {
    TITLE,
    DESCRIPTION
}

@Preview(showBackground = true)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    type: EnumInputType = EnumInputType.EMAIL,
    name: String = "Email",
    value: String = "",
    placeholder: String = "john.doe@example.com",
    supportingText: String = "",
    errorText: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Next,
    isPasswordVisible: Boolean = false,
    isValid: Boolean = true,
    onValueChange: (String) -> Unit = {},
    onPasswordVisibility: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurface)
        )
        AppSpacer(dp = 4.dp, enumSpacer = EnumSpacer.HEIGHT)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = if (isFocused.value) Primary else if (!isFocused.value && !isValid && value.isNotEmpty()) Tertiary else Color.Transparent
                )
                .background(
                    color = if (isFocused.value || (!isValid && value.isNotEmpty())) Color.Transparent else Surface,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 10.dp),
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = OnSurface),
                    cursorBrush = SolidColor(if (isFocused.value) Primary else if (!isFocused.value && !isValid && value.isNotEmpty()) Tertiary else Color.Transparent),
                    interactionSource = interactionSource,
                    singleLine = true,
                    visualTransformation = if (type == EnumInputType.PASSWORD && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = imeAction
                    ),
                    keyboardActions = keyboardActions
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(color = OnSurfaceVariant)
                )
            }
        }
        if (supportingText.isNotEmpty() && isFocused.value && value.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 12.dp),
                text = supportingText,
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
            )
        }
        if (errorText.isNotEmpty() && !isFocused.value && value.isNotEmpty() && !isValid) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 12.dp),
                text = errorText,
                style = MaterialTheme.typography.bodySmall.copy(color = Tertiary)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesField(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(76.dp),
    enumNoteField: EnumNoteField = TITLE,
    value: String = "",
    placeholder: String = "Note Title",
    onValueChange: (String) -> Unit = {},
) {
    TextField(
        modifier = modifier,
        value = value,
        placeholder = {
            Text(
                text = placeholder,
                style = if (enumNoteField == TITLE)
                    MaterialTheme.typography.titleLarge.copy(color = OnSurfaceVariant)
                else
                    MaterialTheme.typography.bodyLarge.copy(color = OnSurfaceVariant)
            )
        },
        textStyle = if (enumNoteField == TITLE)
            MaterialTheme.typography.titleLarge.copy(color = OnSurface)
        else
            MaterialTheme.typography.bodyLarge.copy(color = OnSurfaceVariant),
        singleLine = enumNoteField == TITLE,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = if (enumNoteField == TITLE) OnSurfaceOpacity12 else Color.Transparent,
            unfocusedIndicatorColor = if (enumNoteField == TITLE) OnSurfaceOpacity12 else Color.Transparent,
        )
    )
}


