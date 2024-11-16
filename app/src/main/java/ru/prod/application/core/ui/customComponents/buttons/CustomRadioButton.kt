package ru.prod.application.core.ui.customComponents.buttons

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import ru.prod.application.R

// View с кастомной реализацией RadioButton
@Composable
fun CustomRadioButton(selected: Boolean, onClick: () -> Unit) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        colors = RadioButtonDefaults.colors(
            selectedColor = colorResource(id = R.color.darkest),
            unselectedColor = colorResource(id = R.color.ternary_background),
        )
    )
}