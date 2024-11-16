package ru.prod.application.auth.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.domain.enums.LoadingStatus
import ru.prod.application.core.ui.customComponents.buttons.CustomButton
import ru.prod.application.core.ui.customComponents.inputs.CustomTextField

@Composable
fun SignUpScreen(
    onNewToken: (String?) -> Unit,
) {

    var isLogin by remember { mutableStateOf(false) }

    val phoneNumber = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val viewModel = hiltViewModel<SignUpViewModel>()

    val focus = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .navigationBarsPadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focus.clearFocus()
            }
    ) {
        Column(Modifier.align(Alignment.Center)) {
            CustomTextField(
                value = phoneNumber.value,
                onValueChange = {
                    viewModel.warning = ""
                    phoneNumber.value = it
                },
                placeholder = "Телефон",
                modifier = Modifier.fillMaxWidth(),
                readOnly = viewModel.loadingStatus == LoadingStatus.LOADING
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = password.value,
                onValueChange = {
                    viewModel.warning = ""
                    password.value = it
                },
                placeholder = "Пароль",
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = viewModel.loadingStatus == LoadingStatus.LOADING
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = viewModel.warning,
                color = colorResource(id = R.color.error),
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )
        }

        Column(
            Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomButton(
                text = if (isLogin) "Войти" else "Зарегистрироваться",
                onClick = {
                    var hasError = false
                    if (phoneNumber.value.isBlank() && hasError.not()) {
                        hasError = true
                        viewModel.warning = "Проверьте свой телефон"
                    }

                    if (password.value.isBlank() && hasError.not()) {
                        hasError = true
                        viewModel.warning = "Проверьте свой пароль"
                    }

                    if (isLogin && hasError.not()) {
                        viewModel.login(phoneNumber = phoneNumber.value, password = password.value) { token ->
                            onNewToken(token.value)
                        }
                    } else if (hasError.not()) {
                        viewModel.signUp(phoneNumber = phoneNumber.value, password = password.value) { token ->
                            onNewToken(token.value)
                        }
                    }
                },
                isLoading = viewModel.loadingStatus == LoadingStatus.LOADING,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                if (isLogin) {
                    Text(
                        text = "Нет аккаунта? ",
                        color = colorResource(id = R.color.text),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY
                    )
                    Text(
                        text = "Создать",
                        color = colorResource(id = R.color.darkest),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                isLogin = false
                            }
                    )
                } else {
                    Text(
                        text = "Уже есть аккаунт? ",
                        color = colorResource(id = R.color.text),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY,
                    )
                    Text(
                        text = "Войти",
                        color = colorResource(id = R.color.darkest),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                isLogin = true
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen({})
}
