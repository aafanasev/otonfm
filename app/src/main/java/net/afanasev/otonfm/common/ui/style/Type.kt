package net.afanasev.otonfm.common.ui.style

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import net.afanasev.otonfm.R

/**
 * The brand font, close to the logo's geometric style. It covers Russian and the Sakha letters
 * (ҕ ҥ һ ө ү, and ӈ); check any replacement font for them.
 *
 * One variable font file serves every weight. Its default weight is Thin, so each weight sets the
 * `wght` axis explicitly; without it, text draws Thin whatever weight is asked for.
 */
private val Montserrat = FontFamily(
    listOf(FontWeight.Normal, FontWeight.Medium, FontWeight.SemiBold, FontWeight.Bold).map { weight ->
        Font(
            resId = R.font.montserrat,
            weight = weight,
            variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
        )
    },
)

private val Default = Typography()

/** Material's type scale in the brand font. Screens adjust only the weight, not sizes. */
val Typography = Typography(
    displayLarge = Default.displayLarge.copy(fontFamily = Montserrat),
    displayMedium = Default.displayMedium.copy(fontFamily = Montserrat),
    displaySmall = Default.displaySmall.copy(fontFamily = Montserrat),
    headlineLarge = Default.headlineLarge.copy(fontFamily = Montserrat),
    headlineMedium = Default.headlineMedium.copy(fontFamily = Montserrat),
    headlineSmall = Default.headlineSmall.copy(fontFamily = Montserrat),
    titleLarge = Default.titleLarge.copy(fontFamily = Montserrat),
    titleMedium = Default.titleMedium.copy(fontFamily = Montserrat),
    titleSmall = Default.titleSmall.copy(fontFamily = Montserrat),
    bodyLarge = Default.bodyLarge.copy(fontFamily = Montserrat),
    bodyMedium = Default.bodyMedium.copy(fontFamily = Montserrat),
    bodySmall = Default.bodySmall.copy(fontFamily = Montserrat),
    labelLarge = Default.labelLarge.copy(fontFamily = Montserrat),
    labelMedium = Default.labelMedium.copy(fontFamily = Montserrat),
    labelSmall = Default.labelSmall.copy(fontFamily = Montserrat),
)
