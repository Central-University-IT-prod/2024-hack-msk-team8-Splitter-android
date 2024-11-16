package ru.prod.application.mainMenu.ui.views.debtView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.ui.customComponents.buttons.CustomTinyButton
import ru.prod.application.mainMenu.domain.models.DebtModel
import ru.prod.application.util.BASE_URL
import ru.prod.application.util.Result
import ru.prod.application.util.safeCall
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun DebtView(
    debt: DebtModel,
    saveDebt: (BigDecimal) -> Unit,
    isAdmin: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.secondary_background))
            .padding(12.dp)
            .clickable {
                if (!isAdmin) {
                    onClick()
                }
            }
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = debt.name,
                color = colorResource(id = R.color.text),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = debt.partnerPhoneNumber,
                color = colorResource(id = R.color.text),
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )

            if (isAdmin && !debt.isCompleted) {
                Spacer(modifier = Modifier.height(4.dp))

                CustomTinyButton(text = "Закрыть долг") {
                    saveDebt(debt.fullDebt)
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val progressPercentage: BigDecimal =
                if (debt.isCompleted) {
                    BigDecimal(100)
                } else if (debt.fullDebt.compareTo(BigDecimal.ZERO) != 0) {
                    (debt.fullDebt - debt.remains)
                        .divide(debt.fullDebt, 2, RoundingMode.HALF_UP)
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
                text = "${if (debt.isCompleted) debt.fullDebt else debt.fullDebt - debt.remains} из ${debt.fullDebt}",
                color = colorResource(id = R.color.secondary_text),
                fontWeight = FontWeight.Thin,
                fontSize = 12.sp,
                fontFamily = APP_DEFAULT_FONT_FAMILY
            )
        }
    }
}