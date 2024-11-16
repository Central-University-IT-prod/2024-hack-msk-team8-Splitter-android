package ru.prod.application.mainMenu.ui.views.bannerView

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.mainMenu.domain.models.BannerModel
import kotlin.random.Random

@Composable
fun BannerView() {

    val viewModel: BannerViewModel = hiltViewModel()

    val selectedBanner = viewModel.selectedBanner
    var showBanner by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        showBanner = true
    }

    AnimatedVisibility(
        visible = showBanner,
        enter = fadeIn(tween(500)),
        exit = fadeOut(tween(500)),
        modifier = Modifier.animateContentSize()
    ) {
        if (selectedBanner != null) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(selectedBanner!!.link))
                        context.startActivity(intent)
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(id = R.color.secondary_background))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = selectedBanner!!.imgId),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = selectedBanner!!.title,
                        color = colorResource(id = R.color.text),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY
                    )
                    Text(
                        text = selectedBanner!!.text,
                        color = colorResource(id = R.color.text),
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        fontFamily = APP_DEFAULT_FONT_FAMILY
                    )
                }

                Box(modifier = Modifier.fillMaxHeight()) {
                    Icon(
                        painter = painterResource(id = R.drawable.x_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(21.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                showBanner = false
                            },
                        tint = colorResource(id = R.color.text)
                    )
                }
            }
        }
    }
}