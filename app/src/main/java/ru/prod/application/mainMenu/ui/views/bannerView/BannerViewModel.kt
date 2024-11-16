package ru.prod.application.mainMenu.ui.views.bannerView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.prod.application.R
import ru.prod.application.mainMenu.domain.models.BannerModel
import javax.inject.Inject

@HiltViewModel
class BannerViewModel @Inject constructor() : ViewModel() {

    var selectedBanner by mutableStateOf<BannerModel?>(null)

    val banners = listOf(
        BannerModel(
            "Кешбек",
            "Используйте привелегии в поездках",
            R.drawable.money,
            "https://kartinki.pics/pics/uploads/posts/2022-09/1663483072_1-kartinkin-net-p-otkritka-prosto-khoroshemu-cheloveku-1.jpg"
        ),
        BannerModel(
            "Кредитная карта",
            "Не откладывайте свои покупки на потом.",
            R.drawable.credit,
            "https://3d-galleru.ru/cards/21/50/1boidx8w9wkqnwuf/zdravstvuj-xoroshij-chepovek-udachi-tebe-naves-den.gif"
        ),
        BannerModel(
            "Вклад",
            "Храните свои накопления у нас.",
            R.drawable.bank,
            "https://cool.klev.club/uploads/posts/2024-05/cool-klev-club-sjzl-p-prikolnie-kartinki-ulibnis-muzhchine-27.jpg"
        ),
        BannerModel(
            "Подписка PRO",
            "Повышенный кешбек, юридические консультации и другое.",
            R.drawable.gift,
            "https://cdn1.ozone.ru/s3/multimedia-1-1/7129287973.jpg"
        )
    )

    init {
        viewModelScope.launch {
            while (true) {
                selectedBanner = banners.random()
                delay(30000L)
            }
        }
    }

}
