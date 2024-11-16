package ru.prod.application.mainMenu.ui.screens.myDebtsScreen.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.ui.customComponents.buttons.CustomButton
import ru.prod.application.core.ui.customComponents.inputs.CustomTextField

@Composable
fun NewDebtView(saveDebt: (String) -> Unit, error: String?) {
    var name by remember { mutableStateOf("") }

    val focus = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.background))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focus.clearFocus() }
            .statusBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Подключение к группе",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            fontFamily = APP_DEFAULT_FONT_FAMILY,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "Код подключения",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        error?.let {
            Text(
                text = it,
                color = colorResource(R.color.error),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Подключиться",
            onClick = {
                saveDebt(name)
            }
        )
    }
}