package ru.prod.application.core.ui.customComponents.inputs

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import ru.prod.application.R

// View с кастомной реализацией Switch
@Composable
fun CustomSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedTrackColor = colorResource(id = R.color.darkest),
            checkedThumbColor = colorResource(id = R.color.white),
            checkedBorderColor = Color.Transparent,
            uncheckedTrackColor = colorResource(id = R.color.ternary_background),
            uncheckedThumbColor = colorResource(id = R.color.white),
            uncheckedBorderColor = Color.Transparent
        )
    )
}