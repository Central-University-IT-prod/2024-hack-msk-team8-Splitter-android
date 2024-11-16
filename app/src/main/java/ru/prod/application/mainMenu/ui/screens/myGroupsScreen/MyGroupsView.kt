package ru.prod.application.mainMenu.ui.screens.myGroupsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
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
import ru.prod.application.mainMenu.SharedViewModel
import ru.prod.application.mainMenu.ui.screens.myGroupsScreen.views.GroupView
import ru.prod.application.mainMenu.ui.screens.myGroupsScreen.views.NewGroupView
import ru.prod.application.mainMenu.ui.views.User

@Composable
fun MyDebtorsView(openGroupInfo: (Int, String, String) -> Unit) {
    val viewModel: MyGroupsViewModel = hiltViewModel()
    val sharedViewModel: SharedViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadData()
        sharedViewModel.refresh()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showModal = true },
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

                item {
                    User(username = sharedViewModel.username.value ?: "", refresh = {
                        viewModel.refresh()
                        sharedViewModel.refresh()
                    })
                }

                if (viewModel.loadingString == LoadingStatus.LOADED) {
                    viewModel.myGroups?.let { groups ->
                        items(groups) {
                            GroupView(
                                group = it,
                                onClick = { openGroupInfo(it.id, it.name, it.inviteCode) })
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

    if (viewModel.showModal) {
        Dialog(onDismissRequest = { viewModel.showModal = false }) {
            NewGroupView(
                { name ->
                    viewModel.save(name)
                },
                viewModel.error.value
            )
        }
    }
}