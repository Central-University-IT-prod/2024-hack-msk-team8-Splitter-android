package ru.prod.application.mainMenu.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.prod.application.R
import ru.prod.application.mainMenu.ui.screens.groupInfoSreen.GroupInfoScreenView
import ru.prod.application.mainMenu.ui.screens.myDebtsScreen.MyDebtsView
import ru.prod.application.mainMenu.ui.screens.myGroupsScreen.MyDebtorsView
// Модель для хранения данных о странице в tab navigation
data class TabModel(val route: String, val title: String, val icon: Painter)

// Экраны
object MyDebtsScreen {
    const val ROUTE = "my_debts_screen"
}

object MyGroupsScreen {
    const val ROUTE = "my_groups_screen"
}

object GroupInfoScreen {
    const val ROUTE = "group_info_screen"
    const val ARG_GROUP_ID = "groupId"
    const val ARG_GROUP_NAME = "groupName"
    const val ARG_INVITE_CODE = "inviteCode"

    fun createRoute(groupId: Int, groupName: String, inviteCode: String): String {
        // Encode the strings to ensure safe URL usage
        val encodedGroupName = java.net.URLEncoder.encode(groupName, "UTF-8")
        val encodedInviteCode = java.net.URLEncoder.encode(inviteCode, "UTF-8")
        return "$ROUTE/$groupId/$encodedGroupName/$encodedInviteCode"
    }
}

@Composable
fun TabNavigator() {
    val navController = rememberNavController()

    val tabs = listOf(
        TabModel(
            MyDebtsScreen.ROUTE,
            stringResource(id = R.string.myDebts),
            painterResource(id = R.drawable.emoji_icon)
        ),
        TabModel(
            MyGroupsScreen.ROUTE,
            stringResource(id = R.string.myDebtors),
            painterResource(id = R.drawable.group_icon)
        )
    )

    Scaffold(bottomBar = { BottomNavigationView(navController, tabs) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MyDebtsScreen.ROUTE,
            modifier = Modifier
                .background(colorResource(id = R.color.secondary_background))
                .padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
            composable(MyDebtsScreen.ROUTE) {
                MyDebtsView()
            }
            composable(MyGroupsScreen.ROUTE) {
                MyDebtorsView { groupId, groupName, inviteCode ->
                    navController.navigate(GroupInfoScreen.createRoute(groupId, groupName, inviteCode))
                }
            }
            composable(
                route = "${GroupInfoScreen.ROUTE}/{${GroupInfoScreen.ARG_GROUP_ID}}/{${GroupInfoScreen.ARG_GROUP_NAME}}/{${GroupInfoScreen.ARG_INVITE_CODE}}",
                arguments = listOf(
                    navArgument(GroupInfoScreen.ARG_GROUP_ID) { type = NavType.IntType },
                    navArgument(GroupInfoScreen.ARG_GROUP_NAME) { type = NavType.StringType },
                    navArgument(GroupInfoScreen.ARG_INVITE_CODE) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getInt(GroupInfoScreen.ARG_GROUP_ID)
                    ?: throw IllegalArgumentException("Group ID is required")
                val groupName = backStackEntry.arguments?.getString(GroupInfoScreen.ARG_GROUP_NAME)
                    ?: throw IllegalArgumentException("Group Name is required")
                val inviteCode = backStackEntry.arguments?.getString(GroupInfoScreen.ARG_INVITE_CODE)
                    ?: throw IllegalArgumentException("Invite Code is required")

                GroupInfoScreenView(groupId, groupName, inviteCode) {
                    navController.popBackStack()
                }
            }
        }
    }
}
