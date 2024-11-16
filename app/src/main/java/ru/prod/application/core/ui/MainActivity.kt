package ru.prod.application.core.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.prod.application.auth.AuthManager
import ru.prod.application.auth.presentation.SignUpScreen
import ru.prod.application.core.ui.navigation.StackNavigator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    init {
        INSTANCE = this
    }

    @Inject
    lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //authManager.setAuthToken(null)

        // Подключаем AppMetrica
//        val config = AppMetricaConfig.newConfigBuilder(APP_METRICA_KEY).build()
//        AppMetrica.activate(this, config)

        enableEdgeToEdge(SystemBarStyle.light(0, 0))
        setContent {
            when (authManager.authToken.value) {
                null -> {
                    SignUpScreen(
                        onNewToken = { authManager.setAuthToken(it) }
                    )
                }

                else -> {
                    StackNavigator()
                }
            }
        }
    }

    companion object {
        lateinit var INSTANCE: MainActivity

        fun showToast(message: String?) {
            try {
                INSTANCE.lifecycleScope.launch(Dispatchers.Main.immediate) {
                    Toast.makeText(INSTANCE, message ?: "Неизвестная ошибка", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) { }
        }
    }

}

val ads = listOf(
    "https://contract.gosuslugi.ru",
    "https://rabota.sber.ru",
    "https://www.tbank.ru/career/blog/kak-uvolitsya-po-sobstvennomu-zhelaniyu",
    "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
)
