package ru.prod.application.mainMenu.ui.views.statisticsView

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.domain.enums.LoadingStatus

@Composable
fun StatisticsView() {
    val viewModel: StatisticsViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Row(
        Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.secondary_background))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (viewModel.loadingStatus == LoadingStatus.LOADED) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(80.dp)) {
                    CircularProgressIndicator(
                        progress = { viewModel.incomeProgress / 100f },
                        modifier = Modifier.fillMaxSize(),
                        color = colorResource(id = R.color.darkest),
                        trackColor = colorResource(id = R.color.ternary_background),
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )

                    Text(
                        text = "${viewModel.incomeProgress}%",
                        color = colorResource(id = R.color.text),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Вернули вам",
                    color = colorResource(id = R.color.secondary_text),
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    fontFamily = APP_DEFAULT_FONT_FAMILY
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(80.dp)) {
                    CircularProgressIndicator(
                        progress = { viewModel.outcomeProgress / 100f },
                        modifier = Modifier.fillMaxSize(),
                        color = colorResource(id = R.color.darkest),
                        trackColor = colorResource(id = R.color.ternary_background),
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )

                    Text(
                        text = "${viewModel.outcomeProgress}%",
                        color = colorResource(id = R.color.text),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Вернули вы",
                    color = colorResource(id = R.color.secondary_text),
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    fontFamily = APP_DEFAULT_FONT_FAMILY
                )
            }
        } else if (viewModel.loadingStatus == LoadingStatus.LOADING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 33.dp)
                    .size(24.dp),
                color = colorResource(id = R.color.darkest),
                strokeWidth = 3.dp,
                strokeCap = StrokeCap.Round
            )
        } else {
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