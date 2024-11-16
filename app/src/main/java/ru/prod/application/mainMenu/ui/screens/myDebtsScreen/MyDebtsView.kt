package ru.prod.application.mainMenu.ui.screens.myDebtsScreen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import ru.prod.application.mainMenu.SharedViewModel
import ru.prod.application.mainMenu.ui.screens.myDebtsScreen.views.NewDebtView
import ru.prod.application.mainMenu.ui.views.debtView.DebtView
import ru.prod.application.mainMenu.ui.views.User
import ru.prod.application.mainMenu.ui.views.bannerView.BannerView
import ru.prod.application.mainMenu.ui.views.statisticsView.StatisticsView

@Composable
fun MyDebtsView() {
    val viewModel: MyDebtsViewModel = hiltViewModel()

    val sharedViewModel: SharedViewModel = hiltViewModel<SharedViewModel>()

    var modalPhoneNumber by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

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

    Scaffold(
        floatingActionButton = {
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
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background))
                    .padding(it),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.statusBarsPadding())
                }

                if (viewModel.loadingString == LoadingStatus.LOADED) {
                    sharedViewModel.username.value?.let {
                        item {
                            User(username = it, refresh = viewModel::loadData)

                            Spacer(modifier = Modifier.height(12.dp))

                            StatisticsView()
                        }
                    } ?: run {
                        item {
                            User(username = "", refresh = {
                                viewModel.loadData()
                                sharedViewModel.refresh()
                            })
                        }
                    }

                    viewModel.myDebts?.let { items ->
                        itemsIndexed(items) { index, item ->
                            when (item) {
                                DebtItem.Banner -> {
                                    //Spacer(modifier = Modifier.height(12.dp))
                                    BannerView()
                                }

                                is DebtItem.Debt -> {
                                    DebtView(item.debtModel, saveDebt = {
                                        viewModel.saveDebtById(item.debtModel.id, it)
                                    }, isAdmin = false, onClick = {
                                        modalPhoneNumber = item.debtModel.partnerPhoneNumber
                                    })
                                }
                            }
                        }
                    }
                } else if (viewModel.loadingString == LoadingStatus.LOADING) {
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

    if (viewModel.showAddDebtModal) {
        Dialog(onDismissRequest = { viewModel.showAddDebtModal = false }) {
            NewDebtView(
                viewModel::save,
                viewModel.error.value
            )
        }
    }
}