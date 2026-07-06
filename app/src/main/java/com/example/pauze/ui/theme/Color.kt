package com.example.pauze.ui.theme

import androidx.compose.ui.graphics.Color

// PAUZE 색상 팔레트
data class ColorSet(
    val id: String,
    val colors: List<Color>
){
    fun getColor(index: Int): Color{
        return colors[index]
    }
}

data class AppPaletteSystem(
    val base: ColorSet,
    val gray: ColorSet,
    val primary: ColorSet,
    val secondary: ColorSet,
    val tertiary: ColorSet,
    val redError: ColorSet,
    val yellowWarning: ColorSet,
)

val MyGlobalPalette = AppPaletteSystem(
    base = ColorSet(
        id = "baseColour",
        colors = listOf(
            Color(0xFF1B1C18), Color(0xFFFBFBF9)
        )
    ),
    gray = ColorSet(
        id = "gray",
        colors = listOf(
            Color(0xFFFBFBF9), Color(0xFFF6F6F3),
            Color(0xFFEBEBE5), Color(0xFFDBDBD1),
            Color(0xFFBEBFB0), Color(0xFF81826B),
            Color(0xFF5D5E50), Color(0xFF49493F),
            Color(0xFF2D2328), Color(0xFF1D1E1A),
            )
    ),
    primary = ColorSet(
        id = "primary",
        colors = listOf(
            Color(0xFFFAFBF9), Color(0xFFF2F4F0),
            Color(0xFFD9DFD2), Color(0xFFBFCAB4),
            Color(0xFFA6B596), Color(0xFF879F78),
            Color(0xFF73B75F), Color(0xFF69694A),
            Color(0xFF404B35), Color(0xFF262D20),
        )
    ),
    secondary = ColorSet(
        id = "secondary",
        colors = listOf(
            Color(0xFFFDF7F7), Color(0xFFF9EBEB),
            Color(0xFFEEC3C3), Color(0xFFE39B9B),
            Color(0xFFD97474), Color(0xFFCD4C4C),
            Color(0xFFB33232), Color(0xFF8B2727),
            Color(0xFF641C1C), Color(0xFF3C1111),
        )
    ),
    tertiary = ColorSet(
        id = "tertiary",
        colors = listOf(
            Color(0xFFFEFBF6), Color(0xFFFDF527),
            Color(0xFFF9E0B8), Color(0xFFF6CC89),
            Color(0xFFF2B85A), Color(0xFFEEA32A),
            Color(0xFFD58A11), Color(0xFFA56B0D),
            Color(0xFF764C09), Color(0xFF472E06),
        )
    ),
    redError = ColorSet(
        id = "redError",
        colors = listOf(
            Color(0xFFFEF2F2), Color(0xFFFEE2E2),
            Color(0xFFFCA5A5), Color(0xFFDC2626),
            Color(0xFF991B1B),
        )
    ),
    yellowWarning = ColorSet(
        id = "yellowWarning",
        colors = listOf(
            Color(0xFFF2FCE8), Color(0xFFFEF9C3),
            Color(0xFFFDE047), Color(0xFFEAB308),
            Color(0xFF713F12),
        )
    ),
)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)