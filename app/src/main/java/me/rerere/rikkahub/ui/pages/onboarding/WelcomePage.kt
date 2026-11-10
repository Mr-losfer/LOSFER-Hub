package me.rerere.rikkahub.ui.pages.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Tick02
import me.rerere.rikkahub.R
import me.rerere.rikkahub.data.datastore.AppLanguage
import me.rerere.rikkahub.data.datastore.UyghurFont
import me.rerere.rikkahub.ui.theme.LocalDarkMode

/**
 * LOSFER Hub 首次启动欢迎页 —— 类似 iOS 首次开机欢迎页：大标题、图标与语言选择，
 * 选定语言后立即按该语言的方向（维吾尔语为 RTL）预览整页文案。
 */
@Composable
fun WelcomePage(
    onDone: (AppLanguage) -> Unit,
) {
    var selected by remember { mutableStateOf<AppLanguage?>(null) }
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(60)
        contentVisible = true
    }

    val dark = LocalDarkMode.current
    val background = if (dark) Color(0xFF0E0B0D) else Color(0xFFF7F2F4)
    val gradient = if (dark) {
        listOf(Color(0xFF2A1B23), Color(0xFF0E0B0D))
    } else {
        listOf(Color(0xFFFFE9EE), Color(0xFFF7F2F4))
    }

    // 选择维吾尔语时整页切成 RTL，让用户马上看到效果
    val direction = if (selected == AppLanguage.UYGHUR) LayoutDirection.Rtl else LayoutDirection.Ltr
    val language = selected
    val strings = remember(language) { onboardingStrings(language) }

    androidx.compose.runtime.CompositionLocalProvider(LocalLayoutDirection provides direction) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .background(Brush.verticalGradient(gradient))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(48.dp))

                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn() + slideInVertically(spring(stiffness = Spring.StiffnessLow)) { it / 3 },
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AppLogoIcon(size = 104)
                        Spacer(Modifier.height(28.dp))
                        Text(
                            text = strings.title,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = strings.subtitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = strings.desc,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn() + slideInVertically(spring(stiffness = Spring.StiffnessLow)) { it / 4 },
                    exit = fadeOut() + slideOutVertically { it / 4 },
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        LanguageRow(
                            language = AppLanguage.UYGHUR,
                            label = strings.uyghur,
                            fontFamily = UyghurFont.DEFAULT.fontFamily,
                            selected = selected == AppLanguage.UYGHUR,
                            onClick = { selected = AppLanguage.UYGHUR },
                        )
                        LanguageRow(
                            language = AppLanguage.CHINESE,
                            label = strings.chinese,
                            fontFamily = FontFamily.Default,
                            selected = selected == AppLanguage.CHINESE,
                            onClick = { selected = AppLanguage.CHINESE },
                        )
                        LanguageRow(
                            language = AppLanguage.ENGLISH,
                            label = strings.english,
                            fontFamily = FontFamily.Default,
                            selected = selected == AppLanguage.ENGLISH,
                            onClick = { selected = AppLanguage.ENGLISH },
                        )
                        LanguageRow(
                            language = AppLanguage.SYSTEM,
                            label = strings.system,
                            fontFamily = FontFamily.Default,
                            selected = selected == AppLanguage.SYSTEM,
                            onClick = { selected = AppLanguage.SYSTEM },
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                val primary = MaterialTheme.colorScheme.primary
                Button(
                    onClick = { selected?.let(onDone) },
                    enabled = selected != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Text(
                        text = strings.continueLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "LOSFER Hub · Mr-losfer",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
private fun AppLogoIcon(size: Int) {
    val scale by animateFloatAsState(1f, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "logo")
    Surface(
        modifier = Modifier
            .size(size.dp)
            .scale(scale)
            .clip(RoundedCornerShape(28.dp)),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 12.dp,
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun LanguageRow(
    language: AppLanguage,
    label: String,
    fontFamily: FontFamily,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier
            .fillMaxWidth()
            .border(if (selected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(18.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = HugeIcons.Tick02,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

private data class OnboardingStrings(
    val title: String,
    val subtitle: String,
    val desc: String,
    val continueLabel: String,
    val uyghur: String,
    val chinese: String,
    val english: String,
    val system: String,
)

private fun onboardingStrings(language: AppLanguage?): OnboardingStrings = when (language) {
    AppLanguage.UYGHUR -> OnboardingStrings(
        title = "خۇش كەلدىڭىز",
        subtitle = "LOSFER Hub",
        desc = "ئۆزىڭىز ياخشى كۆرىدىغان تىلنى تاللاڭ. كېيىن تەڭشەكلەردە ئۆزگەرتەلەيسىز.",
        continueLabel = "داۋاملاشتۇرۇش",
        uyghur = "ئۇيغۇرچە",
        chinese = "中文（简体）",
        english = "English",
        system = "سىستېما تىلى",
    )
    AppLanguage.CHINESE -> OnboardingStrings(
        title = "欢迎",
        subtitle = "LOSFER Hub",
        desc = "请选择你偏好的语言，之后可以在设置中随时更改。",
        continueLabel = "继续",
        uyghur = "ئۇيغۇرچە",
        chinese = "中文（简体）",
        english = "English",
        system = "跟随系统",
    )
    else -> OnboardingStrings(
        title = "Welcome",
        subtitle = "LOSFER Hub",
        desc = "Choose your preferred language. You can change it later in Settings.",
        continueLabel = "Continue",
        uyghur = "ئۇيغۇرچە",
        chinese = "中文（简体）",
        english = "English",
        system = "System language",
    )
}
