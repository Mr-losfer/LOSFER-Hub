package me.rerere.rikkahub.ui.pages.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Tick02
import me.rerere.rikkahub.R
import me.rerere.rikkahub.data.datastore.AppLanguage
import me.rerere.rikkahub.data.datastore.UyghurFont
import me.rerere.rikkahub.ui.components.nav.BackButton
import me.rerere.rikkahub.ui.components.ui.CardGroup
import me.rerere.rikkahub.ui.components.ui.switchItem
import me.rerere.rikkahub.ui.theme.CustomColors
import me.rerere.rikkahub.utils.plus
import org.koin.androidx.compose.koinViewModel

/**
 * LOSFER Hub：语言与维吾尔语字体设置页。
 */
@Composable
fun SettingLanguageFontPage(vm: SettingVM = koinViewModel()) {
    val settings by vm.settings.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val currentLanguage = AppLanguage.fromTag(settings.appLanguage)
    val currentFont = UyghurFont.fromId(settings.uyghurFontId)

    fun updateLanguage(language: AppLanguage) {
        vm.updateSettings { it.copy(appLanguage = language.tag) }
    }

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(R.string.losfer_language_font_page_title)) },
                navigationIcon = { BackButton() },
                scrollBehavior = scrollBehavior,
                colors = CustomColors.topBarColors,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = CustomColors.topBarColors.containerColor,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item("language") {
                CardGroup(title = { Text(stringResource(R.string.losfer_language_section)) }) {
                    AppLanguage.entries.forEach { language ->
                        item(
                            onClick = { updateLanguage(language) },
                            headlineContent = { Text(languageLabel(language)) },
                            supportingContent = {
                                if (language == AppLanguage.UYGHUR) {
                                    Text(
                                        text = stringResource(R.string.losfer_rtl_desc),
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            },
                            trailingContent = {
                                if (language == currentLanguage) {
                                    Icon(
                                        imageVector = HugeIcons.Tick02,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            },
                        )
                    }
                }
            }

            item("fontEnable") {
                CardGroup(title = { Text(stringResource(R.string.losfer_font_section)) }) {
                    switchItem(
                        checked = settings.useUyghurFont,
                        onCheckedChange = { checked ->
                            vm.updateSettings { it.copy(useUyghurFont = checked) }
                        },
                        supportingContent = { Text(stringResource(R.string.losfer_font_enable_desc)) },
                        headlineContent = { Text(stringResource(R.string.losfer_font_enable)) },
                    )
                }
            }

            if (settings.useUyghurFont) {
                item("fontPreview") {
                    CardGroup {
                        formItem(
                            headlineContent = { Text(currentFont.displayName) },
                            supportingContent = { Text(stringResource(R.string.losfer_font_section)) },
                        ) {
                            Text(
                                text = stringResource(R.string.losfer_font_preview),
                                fontSize = 22.sp,
                                lineHeight = 36.sp,
                                fontFamily = currentFont.fontFamily,
                            )
                        }
                    }
                }

                item("fontList") {
                    CardGroup {
                        UyghurFont.entries.forEach { font ->
                            item(
                                onClick = {
                                    vm.updateSettings { it.copy(uyghurFontId = font.id, useUyghurFont = true) }
                                },
                                leadingContent = { FontAvatar(font = font) },
                                headlineContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Text(
                                            text = font.displayName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                        if (font == UyghurFont.DEFAULT) DefaultBadge()
                                    }
                                },
                                supportingContent = {
                                    Text(
                                        text = font.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = font.fontFamily,
                                    )
                                },
                                trailingContent = {
                                    if (font == currentFont) {
                                        Icon(
                                            imageVector = HugeIcons.Tick02,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                        )
                                    }
                                },
                            )
                        }
                    }
                }

                item("fontFooter") {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        TextButton(
                            onClick = {
                                vm.updateSettings { it.copy(uyghurFontId = UyghurFont.DEFAULT.id) }
                            },
                            enabled = currentFont != UyghurFont.DEFAULT,
                        ) {
                            Text(stringResource(R.string.losfer_font_reset))
                        }
                        Text(
                            text = stringResource(R.string.losfer_font_copyright),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FontAvatar(font: UyghurFont) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        // 用该字体本身渲染一个维文字母作为缩略
        Text(
            text = "ئ",
            fontFamily = font.fontFamily,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
private fun DefaultBadge() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
    ) {
        Text(
            text = stringResource(R.string.losfer_font_default_badge),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun languageLabel(language: AppLanguage): String = when (language) {
    AppLanguage.UYGHUR -> "ئۇيغۇرچە"
    AppLanguage.CHINESE -> "中文（简体）"
    AppLanguage.ENGLISH -> "English"
    AppLanguage.SYSTEM -> stringResource(R.string.losfer_language_system)
}
