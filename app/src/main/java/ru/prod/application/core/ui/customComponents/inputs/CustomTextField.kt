package ru.prod.application.core.ui.customComponents.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY

// View с кастомной реализацией TextField
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    isError: Boolean = false,
    isSecured: Boolean = false,
    readOnly: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(false) }
    var isSecuredContentVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = !readOnly,
        placeholder = {
            Text(
                text = placeholder,
                color = colorResource(id = R.color.secondary_text),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )
        },
        isError = isError,
        visualTransformation = if (isSecured && !isSecuredContentVisible) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = colorResource(id = R.color.secondary_text),
            focusedTextColor = colorResource(id = R.color.text),
            errorTextColor = colorResource(id = R.color.text),
            disabledTextColor = colorResource(id = R.color.secondary_text),
            cursorColor = colorResource(id = R.color.darkest),
            focusedContainerColor = colorResource(id = R.color.secondary_background),
            unfocusedContainerColor = colorResource(id = R.color.secondary_background),
            errorContainerColor = colorResource(id = R.color.secondary_background),
            disabledContainerColor = colorResource(id = R.color.secondary_background),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedPlaceholderColor = colorResource(id = R.color.secondary_text),
            focusedPlaceholderColor = colorResource(id = R.color.secondary_text),
            errorPlaceholderColor = colorResource(id = R.color.secondary_text),
            disabledPlaceholderColor = colorResource(id = R.color.secondary_text),
        ),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                2.dp,
                when {
                    isError -> colorResource(id = R.color.error)
                    isFocused -> colorResource(id = R.color.darkest)
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(8.dp)
            ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = APP_DEFAULT_FONT_FAMILY,
            fontWeight = FontWeight.Medium
        ),
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            if (isSecured) {
                val image = if (isSecuredContentVisible) {
                    painterResource(id = R.drawable.visible)
                } else {
                    painterResource(id = R.drawable.invisible)
                }
                IconButton(onClick = { isSecuredContentVisible = !isSecuredContentVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = null,
                        tint = if (isError) colorResource(id = R.color.error) else colorResource(id = R.color.secondary_text)
                    )
                }
            }
        },
        keyboardOptions = keyboardOptions,
        readOnly = readOnly
    )
}