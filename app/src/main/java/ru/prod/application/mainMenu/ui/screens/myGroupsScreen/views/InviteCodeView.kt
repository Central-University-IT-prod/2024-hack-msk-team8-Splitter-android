package ru.prod.application.mainMenu.ui.screens.myGroupsScreen.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY

@Composable
fun InviteCodeView(inviteCode: String) {
    Text(
        text = inviteCode,
        color = colorResource(id = R.color.white),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        fontFamily = APP_DEFAULT_FONT_FAMILY,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colorResource(id = R.color.darkest))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    )
}