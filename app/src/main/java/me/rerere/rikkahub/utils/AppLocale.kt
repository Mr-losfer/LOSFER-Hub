package me.rerere.rikkahub.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * LOSFER Hub：应用内界面语言切换。
 *
 * 用 [android.app.LocaleManager]（API 33+，AppCompat 的 setApplicationLocales 在 33 以下走
 * 同样的 service）把语言固定到应用级别，重启后依然生效；语言资源（values-ug 等）由系统按
 * 应用 locale 选取，维吾尔语的语言标签为 ug，Android 会自动把 LocalLayoutDirection 设为 RTL。
 *
 * 更低版本（minSdk 26）没有系统级接口，退化为进程内修改默认 Locale，
 * 保证当前会话的字符串与布局方向正确，重启后由欢迎页或设置再次选择。
 */
fun applyAppLanguage(context: Context, languageTag: String) {
    val appContext = context.applicationContext
    if (languageTag.isBlank()) {
        Locale.setDefault(Locale.getDefault())
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            runCatching {
                val localeManager = appContext.getSystemService(android.app.LocaleManager::class.java)
                localeManager?.applicationLocales = android.os.LocaleList.getEmptyLocaleList()
            }
        }
        return
    }

    val locale = Locale.forLanguageTag(languageTag)
    Locale.setDefault(locale)

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        runCatching {
            val localeManager = appContext.getSystemService(android.app.LocaleManager::class.java)
            localeManager?.applicationLocales = android.os.LocaleList(locale)
        }.onFailure {
            // 部分定制 ROM 没有实现 LocaleManager，退化为下面更新 Configuration
        }
    }

    runCatching {
        val config = Configuration(appContext.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        @Suppress("DEPRECATION")
        appContext.resources.updateConfiguration(config, appContext.resources.displayMetrics)
    }
}
