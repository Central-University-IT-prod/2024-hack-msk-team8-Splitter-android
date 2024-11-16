package ru.prod.application.mainMenu.ui.screens.groupInfoSreen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.domain.enums.LoadingStatus
import ru.prod.application.core.ui.customComponents.modals.ConfirmationAlert
import ru.prod.application.mainMenu.ui.screens.groupInfoSreen.views.InviteCodeView
import ru.prod.application.mainMenu.ui.screens.groupInfoSreen.views.NewDebtView
import ru.prod.application.mainMenu.ui.views.bannerView.BannerView
import ru.prod.application.mainMenu.ui.views.debtView.DebtView

@Composable
fun GroupInfoScreenView(
    groupId: Int,
    groupName: String,
    inviteCode: String,
    popBackNavStack: () -> Unit
) {
    val viewModel: GroupInfoScreenViewModel = hiltViewModel()

    var modalPhoneNumber by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(groupId) {
        viewModel.loadGroupInfo(groupId, groupName, inviteCode)
    }

    Scaffold(
        floatingActionButton = {
            if (viewModel.loadingStatus == LoadingStatus.LOADED) {
                FloatingActionButton(
                    onClick = { viewModel.showAddDebtModal = true },
                    containerColor = colorResource(id = R.color.darkest)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus_icon),
                        contentDescription = null,
                        tint = colorResource(id = R.color.white),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background))
                    .padding(it)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(
                        modifier = Modifier
                            .statusBarsPadding()
                            .height(12.dp)
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_left_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(27.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { popBackNavStack() },
                            tint = colorResource(id = R.color.text),
                        )
                    }
                }

                if (viewModel.loadingStatus == LoadingStatus.LOADED) {
                    item {
                        Text(
                            text = viewModel.groupInfo!!.name,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = APP_DEFAULT_FONT_FAMILY,
                            color = colorResource(id = R.color.text)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            androidx.compose.material3.Text(
                                text = "Код подключения",
                                color = colorResource(id = R.color.text),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                fontFamily = APP_DEFAULT_FONT_FAMILY
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            InviteCodeView(inviteCode = viewModel.groupInfo!!.inviteCode)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        BannerView()
                    }

                    viewModel.groupInfo?.debts?.distinct()?.let { items ->
                        items(items) {
                            DebtView(
                                it,
                                { newDebt -> viewModel.saveDebtById(it.id, newDebt) },
                                isAdmin = true,
                            ) {
                                modalPhoneNumber = it.partnerPhoneNumber
                            }
                        }
                    }

                } else if (viewModel.loadingStatus == LoadingStatus.LOADING) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 33.dp)
                                .size(24.dp),
                            color = colorResource(id = R.color.darkest),
                            strokeWidth = 3.dp,
                            strokeCap = StrokeCap.Round
                        )
                    }
                } else {
                    item {
                        Text(
                            text = "Ошибка загрузки",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = APP_DEFAULT_FONT_FAMILY,
                            color = colorResource(id = R.color.secondary_text),
                            modifier = Modifier.padding(top = 33.dp)
                        )
                    }
                }
            }
        }
    )


    modalPhoneNumber?.let {
        ConfirmationAlert(
            title = "Погасить долг",
            text = "Перевести деньги по номеру $modalPhoneNumber",
            confirmButtonText = "Перевести через Т-Банк",
            denyButtonText = "Нет, спасибо",
            onConfirm = {
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "tel:$modalPhoneNumber"
                    )
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }.let { intent ->
                    intent.`package` = "com.idamob.tinkoff.android"
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context, "Кажется, на вашем телефоне не установлен Т-Банк",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                modalPhoneNumber = null
            },
            onDismissRequest = {
                modalPhoneNumber = null
            }
        )
    }

    if (viewModel.showAddDebtModal) {
        Dialog(onDismissRequest = { viewModel.showAddDebtModal = false }) {
            viewModel.users?.let {
                NewDebtView(
                    it,
                    viewModel::save,
                    viewModel.error
                )
            }
        }
    }
}