package me.rerere.rikkahub.data.datastore

import me.rerere.ai.provider.ProviderSetting
import me.rerere.rikkahub.data.losfer.LosferService
import kotlin.uuid.Uuid

val DEFAULT_AUTO_MODEL_ID = Uuid.parse("b7055fb4-39f9-4042-a88a-0d80ed76cf08")

/** 内置 DeepSeek 提供商，快速入门页的迁移流程会直接跳到它 */
val DEEPSEEK_PROVIDER_ID = Uuid.parse("f099ad5b-ef03-446d-8e78-7e36787f780b")

val DEFAULT_PROVIDERS = listOf(
    // LOSFER Hub：内置提供商只有 LOSFER，首次使用即为唯一可选项
    ProviderSetting.OpenAI(
        id = LosferService.LOSFER_PROVIDER_ID,
        name = "LOSFER",
        baseUrl = LosferService.BASE_URL,
        apiKey = "",
        enabled = true,
        builtIn = true,
        description = LOSFER_FULL_DESCRIPTION,
        shortDescription = LOSFER_SHORT_DESCRIPTION,
    ),
)
