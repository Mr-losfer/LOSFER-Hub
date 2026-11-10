package me.rerere.rikkahub.data.datastore

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import me.rerere.ai.provider.ProviderSetting
import me.rerere.rikkahub.data.losfer.LosferService

/**
 * 推荐的提供商列表，在提供商设置页右上角的推荐 Sheet 中展示。
 *
 * 推荐 Sheet 和提供商列表展示 [ProviderSetting.shortDescription]，
 * 添加后的提供商详情页展示带链接的完整 [ProviderSetting.description]。
 */
val RECOMMENDED_PROVIDERS: List<ProviderSetting> = listOf(
    ProviderSetting.OpenAI(
        id = LosferService.LOSFER_PROVIDER_ID,
        name = "LOSFER",
        baseUrl = LosferService.BASE_URL,
        apiKey = "",
        enabled = true,
        description = {
            FullDescription(
                text = "LOSFER Hub نىڭ رەسمىي تەمىنلىگۈچىسى — OpenAI ئۇچۇر ئالماشتۇرۇش كېلىشىمىنى قوللايدۇ، بىر كۇنۇپكىدا API ئاچقۇچى ئارقىلىق ئۇلىنىدۇ",
                DescriptionLink(
                    prefix = "API مەركىزى: ",
                    text = LosferService.API_KEY_PORTAL,
                    url = LosferService.API_KEY_PORTAL,
                ),
            )
        },
        shortDescription = {
            ShortDescription("OpenAI ماسلاشتۇرۇلغان رەسمىي مۇلازىمەت، بىر كۇنۇپكىدا ئۇلىنىدۇ")
        },
    ),
)

private data class DescriptionLink(
    val prefix: String = "",
    val text: String,
    val url: String,
)

@Composable
private fun FullDescription(text: String, vararg links: DescriptionLink) {
    val linkColor = MaterialTheme.colorScheme.primary
    Text(
        text = buildAnnotatedString {
            append(text)
            links.forEach { link ->
                appendLine()
                append(link.prefix)
                withLink(LinkAnnotation.Url(link.url)) {
                    withStyle(SpanStyle(linkColor)) {
                        append(link.text)
                    }
                }
            }
        }
    )
}

@Composable
private fun ShortDescription(text: String) {
    Text(
        text = text,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}
