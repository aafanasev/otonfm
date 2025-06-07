package net.afanasev.otonfm.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

@Immutable
data class CustomColorsPalette(
    val previewBackground: Color = Color.Unspecified,
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }

val LightCustomColorsPalette = CustomColorsPalette(
    previewBackground = Color.White,
)

val DarkCustomColorsPalette = CustomColorsPalette(
    previewBackground = Color.DarkGray,
)

// Credits: https://github.com/androsovm/otonfm/blob/main/Oton.FM/YakutiaGradients.swift
val BACKGROUND_GRADIENTS: List<List<Color>> = listOf(
    // Северное сияние над Якутией
    listOf(Color(0xFF264CB2), Color(0xFF663080)),
    // Закат над Якутском
    listOf(Color(0xFFF27A38), Color(0xFF4D1E4D)),
    // Сумерки в якутской тайге
    listOf(Color(0xFF392759), Color(0xFF664080)),
    // Рассвет над рекой Лена
    listOf(Color(0xFF80ADE6), Color(0xFF265999)),
    // Зимняя Лена в сумерках
    listOf(Color(0xFF1F263F), Color(0xFF394267)),
    // Самоцветы Якутии
    listOf(Color(0xFF592380), Color(0xFFA42666)),
    // Летний вечер в Якутии
    listOf(Color(0xFFD9994D), Color(0xFF803324)),
    // Полярная звезда в ясную ночь
    listOf(Color(0xFF331966), Color(0xFF663399)),
    // Зимнее утро в Якутии
    listOf(Color(0xFFD9E6F2), Color(0xFF99BFE6)),
    // Лиловый вечер на Лене
    listOf(Color(0xFF483359), Color(0xFF732680)),
    // Древние легенды якутов
    listOf(Color(0xFF40334C), Color(0xFF664C73)),
    // Янтарный закат над тундрой
    listOf(Color(0xFFF28333), Color(0xFF99330D)),
    // Зимняя тайга в снегу
    listOf(Color(0xFF264059), Color(0xFF4D6673)),
    // Кристаллы якутского льда
    listOf(Color(0xFF4D3399), Color(0xFF8052A6)),
    // Звездное небо Якутии
    listOf(Color(0xFF1F2673), Color(0xFF40188C)),
    // Цветение весны в долине Туймаада
    listOf(Color(0xFFD9A6D9), Color(0xFF8040A6)),
    // Ночь Эпоса Олонхо
    listOf(Color(0xFF592066), Color(0xFF80268C)),
    // Алмазное небо Якутии
    listOf(Color(0xFF335080), Color(0xFF5C7399)),
    // Летняя тундра в цвету
    listOf(Color(0xFF33665A), Color(0xFF599173)),
    // Осенние краски Якутии
    listOf(Color(0xFFB2331A), Color(0xFFE6994D)),

    // Якутский луг в разгар лета
    listOf(Color(0xFF19BF73), Color(0xFF99F233)),
    // Полуночное солнце в Якутии
    listOf(Color(0xFFF2C733), Color(0xFFF27F4D)),
    // Голубые озёра Якутии
    listOf(Color(0xFF33CCE6), Color(0xFF1A73CC)),
    // Яркие цветы якутской тундры
    listOf(Color(0xFFF24273), Color(0xFFF29926)),
    // Праздник Ысыах в разгар лета
    listOf(Color(0xFFB3F28C), Color(0xFF26994D))
)