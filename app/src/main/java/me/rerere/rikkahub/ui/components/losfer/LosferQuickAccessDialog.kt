package me.rerere.rikkahub.ui.components.losfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Key01
import me.rerere.hugeicons.stroke.Link01
import me.rerere.rikkahub.R
import me.rerere.rikkahub.data.datastore.SettingsStore
import me.rerere.rikkahub.data.losfer.LosferService
import me.rerere.rikkahub.ui.context.LocalToaster
import me.rerere.rikkahub.utils.openUrl
import me.rerere.ui.toast.ToastType
import org.koin.compose.koinInject

/**
 * LOSFER 一键接入弹窗。
 *
 * 输入 API 密钥 → 调 `/v1/models` 校验并拉取模型 → 自动添加 OpenAI 协议提供商。
 */
@Composable
fun LosferQuickAccessDialog(
    onDismissRequest: () -> Unit,
    onConnected: () -> Unit = {},
) {
    val context = LocalContext.current
    val toaster = LocalToaster.current
    val losferService: LosferService = koinInject()
    val settingsStore: SettingsStore = koinInject()
    val scope = rememberCoroutineScope()

    var apiKey by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val emptyKeyError = stringResource(R.string.losfer_quick_access_empty_key)
    val invalidKeyError = stringResource(R.string.losfer_quick_access_invalid_key)
    val networkError = stringResource(R.string.losfer_quick_access_network_error)
    val successTemplate = stringResource(R.string.losfer_quick_access_success)
    val updatedTemplate = stringResource(R.string.losfer_quick_access_updated)
    val portalUrl = stringResource(R.string.losfer_quick_access_get_link)

    fun connect() {
        val key = apiKey.trim()
        if (key.isBlank()) {
            error = emptyKeyError
            return
        }
        error = null
        loading = true
        scope.launch {
            val result = losferService.verifyAndFetchModels(key)
            loading = false
            result.fold(
                onSuccess = { models ->
                    var updated = false
                    settingsStore.update { settings ->
                        val existed = settings.providers.any { it.id == LosferService.LOSFER_PROVIDER_ID }
                        updated = existed
                        losferService.applyProvider(settings, key, models)
                    }
                    val message = if (updated) {
                        updatedTemplate.format(models.size)
                    } else {
                        successTemplate.format(models.size)
                    }
                    toaster.show(message, type = ToastType.Success)
                    onConnected()
                    onDismissRequest()
                },
                onFailure = { throwable ->
                    error = if (throwable is LosferService.InvalidApiKeyException) {
                        invalidKeyError
                    } else {
                        networkError
                    }
                },
            )
        }
    }

    AlertDialog(
        onDismissRequest = { if (!loading) onDismissRequest() },
        icon = { Icon(HugeIcons.Link01, contentDescription = null) },
        title = { Text(stringResource(R.string.losfer_quick_access_dialog_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.losfer_quick_access_dialog_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = {
                        apiKey = it
                        error = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    label = { Text(stringResource(R.string.losfer_quick_access_api_key_hint)) },
                    placeholder = { Text("ls-xxxxxxxxxxxxxxxx") },
                    singleLine = true,
                    isError = error != null,
                    supportingText = error?.let { { Text(it) } },
                    leadingIcon = { Icon(HugeIcons.Key01, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                )

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.losfer_quick_access_how_to_get),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                imageVector = HugeIcons.Link01,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = portalUrl,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 2.dp),
                            )
                        }
                        TextButton(
                            onClick = {
                                context.openUrl(portalUrl)
                            },
                            contentPadding = PaddingValues(horizontal = 0.dp),
                        ) {
                            Text(stringResource(R.string.losfer_quick_access_open_portal))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { connect() }, enabled = !loading) {
                if (loading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Text(stringResource(R.string.losfer_quick_access_checking))
                    }
                } else {
                    Text(stringResource(R.string.losfer_quick_access_connect))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest, enabled = !loading) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}
