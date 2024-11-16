package ru.prod.application.mainMenu.ui.screens.groupInfoSreen.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.ui.customComponents.buttons.CustomButton
import ru.prod.application.core.ui.customComponents.buttons.CustomTinyButton
import ru.prod.application.core.ui.customComponents.inputs.CustomTextField
import ru.prod.application.mainMenu.domain.models.UserModel
import ru.prod.application.mainMenu.ui.screens.groupInfoSreen.UserInfo

@Composable
fun NewDebtView(users: List<UserInfo>, saveDebt: (String, String, UserInfo) -> Unit, error: String?) {
    var name by remember { mutableStateOf("") }
    var fullDebt by remember { mutableStateOf("") }

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
            text = "Добавление долга",
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
            placeholder = "Название",
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = fullDebt,
            onValueChange = { fullDebt = it },
            placeholder = "Полная сумма",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        var expanded by remember { mutableStateOf(false) }
        var selectedId by remember { mutableStateOf(0) }
        var selectedItem by remember { mutableStateOf("Выберите должника") }
        Box {
            CustomTinyButton(text = selectedItem, onClick = { expanded = true })

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                users.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = item.username
                            selectedId = index
                            expanded = false
                        }
                    ) {
                        Text(item.username)
                    }
                }
            }
        }

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
            text = "Добавить",
            onClick = {
                saveDebt(name, fullDebt, users[selectedId])
            }
        )
    }
}