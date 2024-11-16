package ru.prod.application.mainMenu.ui.screens.myGroupsScreen.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.mainMenu.domain.models.GroupModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun GroupView(group: GroupModel, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.secondary_background))
            .padding(12.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = group.name,
                color = colorResource(id = R.color.text),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Код подключения",
                    color = colorResource(id = R.color.text),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = APP_DEFAULT_FONT_FAMILY
                )

                Spacer(modifier = Modifier.width(4.dp))

                InviteCodeView(inviteCode = group.inviteCode)
            }

            /**Text(
                text = "Количество участников: ${group.participantsCount}",
                color = colorResource(id = R.color.text),
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )*/

            Spacer(modifier = Modifier.height(4.dp))
        }

        /**Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val progressPercentage: BigDecimal =
                if (group.fullDebt.compareTo(BigDecimal.ZERO) != 0) {
                    (group.fullDebt - group.remains)
                        .divide(group.fullDebt, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal(100))
                        .setScale(0, RoundingMode.HALF_UP)
                } else {
                    BigDecimal.ZERO
                }

            Box(modifier = Modifier.size(80.dp)) {
                CircularProgressIndicator(
                    progress = { (progressPercentage.divide(BigDecimal(100)).toFloat()) },
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.darkest),
                    trackColor = colorResource(id = R.color.ternary_background),
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round
                )

                Text(
                    text = "$progressPercentage%",
                    color = colorResource(id = R.color.text),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    fontFamily = APP_DEFAULT_FONT_FAMILY,
                    modifier = Modifier.align(Alignment.Center),
                )
            }


            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${group.fullDebt - group.remains} из ${group.fullDebt}",
                color = colorResource(id = R.color.secondary_text),
                fontWeight = FontWeight.Thin,
                fontSize = 12.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )
        }*/
    }
}