package ru.prod.application.core.ui.customComponents.inputs

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import ru.prod.application.R

// View с кастомной реализацией Checkbox
@Composable
fun CustomCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = CheckboxDefaults.colors(
            checkedColor = colorResource(id = R.color.darkest),
            checkmarkColor = colorResource(id = R.color.white),
            uncheckedColor = colorResource(id = R.color.ternary_background),
        )
    )
}