/**
 * Created by Eugeny Kabak on 20.03.2022
 */
package com.test.kabak.openweather.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Yellow200 = Color(0xffffeb46)
private val Blue200 = Color(0xff91a4fc)
private val ColorTextDark = Color(0xFF8E9099)
private val DividerDark = Color(0xFFC2CDFF)

private val Yellow500 = Color(0xFFC7B948)
private val Blue700 = Color(0xFF293986)
private val ColorTextLight = Color(0xFF1F1F1F)
private val DividerLight = Color(0xFF3E61FF)

private val DarkColors = darkColors(
    primary = Yellow200,
    secondary = Blue200,
    // ...
)
private val LightColors = lightColors(
    primary = Yellow500,
    secondary = Blue700,
    // ...
)

@get:Composable
val Colors.textColor: Color
    get() = if (isLight) ColorTextLight else ColorTextDark

@get:Composable
val Colors.dividerColor: Color
    get() = if (isLight) DividerLight else DividerDark

//val Rubik = FontFamily(
//    Font(R.font.rubik_regular),
//    Font(R.font.rubik_medium, FontWeight.W500),
//    Font(R.font.rubik_bold, FontWeight.Bold)
//)

val MyTypography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.W300,
        fontSize = 96.sp,
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
    ),
)


@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = MyTypography,
        content = content,
    )
}


