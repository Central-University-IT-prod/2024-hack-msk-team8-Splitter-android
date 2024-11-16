package ru.prod.application.core.ui.customComponents.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY

// View счетчика
@Composable
fun Stepper(
    step: Int,
    onStepChange: (Int) -> Unit,
    minStep: Int = 0,
    maxStep: Int = 10
) {
    Row(
        Modifier
            .border(
                1.dp,
                colorResource(id = R.color.ternary_background),
                RoundedCornerShape(50)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onStepChange(step - 1) },
            enabled = step > minStep
        ) {
            Icon(
                painter = painterResource(id = R.drawable.minus_icon),
                contentDescription = null,
                tint = if (step > minStep) colorResource(id = R.color.darkest) else colorResource(id = R.color.ternary_background),
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = step.toString(),
            color = colorResource(id = R.color.text),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontFamily = APP_DEFAULT_FONT_FAMILY,
            modifier = Modifier.widthIn(min = 44.dp),
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = { onStepChange(step + 1) },
            enabled = step < maxStep
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus_icon),
                contentDescription = null,
                tint = if (step < maxStep) colorResource(id = R.color.darkest) else colorResource(id = R.color.ternary_background),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}