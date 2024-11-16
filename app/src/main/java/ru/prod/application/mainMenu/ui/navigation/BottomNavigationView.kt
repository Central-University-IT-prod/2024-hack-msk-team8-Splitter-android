package ru.prod.application.mainMenu.ui.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY

// View со всеми табами
@Composable
fun BottomNavigationView(navController: NavController, tabs: List<TabModel>) {
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding(),
        backgroundColor = colorResource(id = R.color.secondary_background),
        elevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        tabs.forEach { tab ->
            val tabContentColor = if (currentDestination?.route == tab.route) {
                colorResource(id = R.color.darkest)
            } else {
                colorResource(id = R.color.text)
            }

            BottomNavigationItem(
                icon = {
                    Icon(
                        tab.icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = tabContentColor
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        color = tabContentColor,
                        fontWeight = FontWeight.Thin,
                        fontSize = 12.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}