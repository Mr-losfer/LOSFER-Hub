package me.rerere.rikkahub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import me.rerere.rikkahub.data.datastore.Settings
import me.rerere.rikkahub.data.datastore.UyghurFont

/**
 * LOSFER Hub：把选中的维吾尔语字体应用到全局 Material Typography。
 *
 * 这些字体只包含维吾尔语（阿拉伯字母）与拉丁字形，不包含中日韩字形，
 * 因此真正的中文回退仍由系统字体完成（Compose 会按字形缺失逐字回退）。
 */
@Composable
fun UyghurFontProvider(
    settings: Settings,
    content: @Composable () -> Unit,
) {
    val fontFamily: FontFamily? = remember(settings.uyghurFontId, settings.useUyghurFont) {
        if (!settings.useUyghurFont) null else UyghurFont.fromId(settings.uyghurFontId).fontFamily
    }

    if (fontFamily == null) {
        content()
        return
    }

    val base = MaterialTheme.typography
    val typography = remember(base, fontFamily) { base.withFontFamily(fontFamily) }
    MaterialTheme(typography = typography) {
        content()
    }
}

private fun Typography.withFontFamily(fontFamily: FontFamily): Typography = copy(
    displayLarge = displayLarge.withFontFamily(fontFamily),
    displayMedium = displayMedium.withFontFamily(fontFamily),
    displaySmall = displaySmall.withFontFamily(fontFamily),
    headlineLarge = headlineLarge.withFontFamily(fontFamily),
    headlineMedium = headlineMedium.withFontFamily(fontFamily),
    headlineSmall = headlineSmall.withFontFamily(fontFamily),
    titleLarge = titleLarge.withFontFamily(fontFamily),
    titleMedium = titleMedium.withFontFamily(fontFamily),
    titleSmall = titleSmall.withFontFamily(fontFamily),
    bodyLarge = bodyLarge.withFontFamily(fontFamily),
    bodyMedium = bodyMedium.withFontFamily(fontFamily),
    bodySmall = bodySmall.withFontFamily(fontFamily),
    labelLarge = labelLarge.withFontFamily(fontFamily),
    labelMedium = labelMedium.withFontFamily(fontFamily),
    labelSmall = labelSmall.withFontFamily(fontFamily),
)

private fun androidx.compose.ui.text.TextStyle.withFontFamily(
    fontFamily: FontFamily,
): androidx.compose.ui.text.TextStyle = copy(fontFamily = fontFamily)
