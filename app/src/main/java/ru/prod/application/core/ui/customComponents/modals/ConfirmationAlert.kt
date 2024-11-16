package ru.prod.application.core.ui.customComponents.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.ui.customComponents.buttons.CustomButton

// Alert с двумя кнопкой
@Composable
fun ConfirmationAlert(
    title: String,
    text: String,
    confirmButtonText: String,
    denyButtonText: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.background))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = colorResource(id = R.color.text),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                color = colorResource(id = R.color.secondary_text),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton(
                text = confirmButtonText,
                modifier = Modifier.fillMaxWidth()
            ) { onConfirm() }

            Spacer(modifier = Modifier.height(8.dp))
            
            CustomButton(
                text = denyButtonText,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = colorResource(id = R.color.darkest)
            ) { onDismissRequest() }
        }
    }
}

@Preview
@Composable
private fun ConfirmationAlertPreview() {
    ConfirmationAlert("hello world", "I love yandex", "Yes", "No", {}) { }
}