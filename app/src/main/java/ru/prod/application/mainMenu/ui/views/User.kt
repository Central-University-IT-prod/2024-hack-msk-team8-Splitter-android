package ru.prod.application.mainMenu.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.prod.application.R
import ru.prod.application.auth.AuthManager
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY

@Composable
fun User(
    username: String,
    contentColor: Color = Color.White,
    refresh: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.darkest),
                        colorResource(R.color.light)
                    )
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Spacer(modifier = Modifier.width(9.dp))

        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Person Icon",
            modifier = Modifier.size(48.dp),
            tint = contentColor
        )

        Spacer(modifier = Modifier.width(9.dp))

        Column {
            Text(
                text = username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                color = contentColor
            )
            
            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Добро пожаловать!",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = APP_DEFAULT_FONT_FAMILY,
                color = contentColor
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {
                refresh()
            },
        ) {
            Icon(
                contentDescription = null,
                imageVector = Icons.Filled.Refresh,
                tint = contentColor
            )
        }

        IconButton(
            onClick = {
                AuthManager.INSTANCE.setAuthToken(null)
            },
        ) {
            Icon(
                contentDescription = null,
                imageVector = Icons.Filled.Logout,
                tint = contentColor
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun UserPreview() {
    User(username = "{{sensitive_data}}") {}
}