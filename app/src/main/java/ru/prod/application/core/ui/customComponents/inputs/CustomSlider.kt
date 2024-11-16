package ru.prod.application.core.ui.customComponents.inputs

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import ru.prod.application.R

// View с кастомной реализацией Slider
@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        colors = SliderDefaults.colors(
            thumbColor = colorResource(id = R.color.darkest),
            activeTrackColor = colorResource(id = R.color.darkest),
            inactiveTrackColor = colorResource(id = R.color.ternary_background),
        ),
        modifier = modifier
    )
}