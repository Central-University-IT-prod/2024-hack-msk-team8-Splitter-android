package ru.prod.application.mainMenu.ui.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import ru.prod.application.core.ui.ads

@Composable
fun Ad(
    text: String,
    modifier: Modifier = Modifier,
) {
    val contentColor = Color.Black
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.secondary_background),
                        colorResource(R.color.secondary_background),
                        //colorResource(R.color.light)
                    )
                )
            ).clickable {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(ads.random()))
                context.startActivity(browserIntent)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Person Icon",
            modifier = Modifier.size(32.dp + 32.dp).padding(start = 24.dp),
            tint = contentColor
        )

        Column(Modifier.padding(16.dp)) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                color = contentColor
            )

            Text(
                modifier = Modifier,
                text = "Подробнее...",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                color = contentColor
            )
        }
    }
}

@Preview
@Composable
private fun AdPreview() {
    Ad("Хотите работу в лучшем банке страны?")
}