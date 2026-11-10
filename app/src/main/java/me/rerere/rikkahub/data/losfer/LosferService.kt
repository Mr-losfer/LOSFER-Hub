package me.rerere.rikkahub.data.losfer

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.rerere.ai.provider.Model
import me.rerere.ai.provider.ModelType
import me.rerere.ai.provider.ProviderSetting
import me.rerere.common.http.await
import me.rerere.rikkahub.data.datastore.Settings
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.uuid.Uuid

/**
 * LOSFER Hub：LOSFER 平台接入。
 *
 * - 验证 API 密钥：调用 `GET {baseUrl}/models`，200 表示有效，401 表示密钥无效。
 * - 拉取模型列表：同一接口返回的 `data[].id`。
 * - 添加提供商：以 OpenAI 协议（Chat Completions）添加 `https://api.losfer.cn/v1`。
 */
class LosferService(
    private val client: OkHttpClient,
) {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 密钥校验 + 模型拉取。
     *
     * @return [Result.success] 携带模型列表；失败时异常为 [InvalidApiKeyException] 或网络异常
     */
    suspend fun verifyAndFetchModels(apiKey: String): Result<List<Model>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = client.newCall(
                Request.Builder()
                    .url("$BASE_URL/models")
                    .header("Authorization", "Bearer ${apiKey.trim()}")
                    .header("Accept", "application/json")
                    .get()
                    .build()
            ).await()

            val body = response.body?.string().orEmpty()
            if (response.code == 401 || response.code == 403) {
                throw InvalidApiKeyException(body.take(200))
            }
            if (!response.isSuccessful) {
                throw IllegalStateException("HTTP ${response.code}")
            }

            val data = runCatching {
                json.parseToJsonElement(body).jsonObject["data"]?.jsonArray
            }.getOrNull() ?: throw IllegalStateException("Malformed models response")

            data.mapNotNull { element ->
                val id = runCatching { element.jsonObject["id"]?.jsonPrimitive?.content }.getOrNull()
                    ?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                Model(
                    modelId = id,
                    displayName = id,
                    type = guessModelType(id),
                )
            }.distinctBy { it.modelId }
                .sortedBy { it.modelId }
                .ifEmpty { throw IllegalStateException("Empty model list") }
        }.onFailure {
            if (it !is InvalidApiKeyException) Log.w(TAG, "verify losfer api key failed", it)
        }
    }

    /**
     * 校验成功后把提供商写进设置：已存在则更新密钥和模型，不存在则插到列表最前面。
     */
    fun applyProvider(settings: Settings, apiKey: String, models: List<Model>): Settings {
        val existing = settings.providers.firstOrNull {
            it is ProviderSetting.OpenAI && it.id == LOSFER_PROVIDER_ID
        } as? ProviderSetting.OpenAI

        val provider = (existing ?: LOSFER_PROVIDER_SETTING).copy(
            apiKey = apiKey.trim(),
            models = models,
            baseUrl = BASE_URL,
            enabled = true,
            // LOSFER 提供的是 OpenAI 兼容的 Chat Completions，不走 Responses API
            useResponseApi = false,
        )

        val providers = if (existing != null) {
            settings.providers.map { if (it.id == LOSFER_PROVIDER_ID) provider else it }
        } else {
            listOf(provider) + settings.providers
        }
        return settings.copy(providers = providers)
    }

    private fun guessModelType(modelId: String): ModelType {
        val id = modelId.lowercase()
        return when {
            id.contains("embed") -> ModelType.EMBEDDING
            IMAGE_MODEL_HINTS.any { id.contains(it) } -> ModelType.IMAGE
            else -> ModelType.CHAT
        }
    }

    class InvalidApiKeyException(message: String?) : Exception(message)

    companion object {
        private const val TAG = "LosferService"

        const val BASE_URL = "https://api.losfer.cn/v1"
        const val API_KEY_PORTAL = "https://api.losfer.cn/pc/api-list/keys/"

        val LOSFER_PROVIDER_ID: Uuid = Uuid.parse("105f3e0a-1b7c-4c1a-9b39-6c1b1a2f0001")

        /** LOSFER 提供商模板（推荐列表与默认添加都用它） */
        val LOSFER_PROVIDER_SETTING: ProviderSetting.OpenAI = ProviderSetting.OpenAI(
            id = LOSFER_PROVIDER_ID,
            name = "LOSFER",
            baseUrl = BASE_URL,
            apiKey = "",
            enabled = true,
        )

        private val IMAGE_MODEL_HINTS = listOf(
            "image", "imagine", "banana", "flux", "dall-e", "dalle", "seedream", "gpt-image",
        )
    }
}
