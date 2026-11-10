package me.rerere.rikkahub.utils

/**
 * LOSFER Hub 的应用元信息与官方地址。
 *
 * 更新检查、下载地址都指向本项目自己的 GitHub 仓库（Mr-losfer/LOSFER-Hub），
 * 每次发版在仓库 Releases 里上传 APK 即可被客户端检测到。
 */
object LosferInfo {
    /** GitHub 仓库（owner/repo） */
    const val GITHUB_OWNER = "Mr-losfer"
    const val GITHUB_REPO = "LOSFER-Hub"

    val GITHUB_URL = "https://github.com/$GITHUB_OWNER/$GITHUB_REPO"
    val GITHUB_RELEASES_URL = "$GITHUB_URL/releases"
    val GITHUB_API_LATEST_RELEASE = "https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/releases/latest"
    val GITHUB_LICENSE_URL = "$GITHUB_URL/blob/main/LICENSE"

    /**
     * 技能库清单。放在仓库根目录的 skill-sources.json（也可以是同位置的 txt），
     * 客户端点「下载 LOSFER 技能库」时先读它，再按清单批量导入技能。
     * 改清单即可增减技能源，不需要重新发版。
     */
    val SKILL_LIBRARY_CONFIG =
        "https://raw.githubusercontent.com/$GITHUB_OWNER/$GITHUB_REPO/main/skill-sources.json"

    /** 清单读取失败时的内置兜底技能库 */
    const val DEFAULT_SKILL_LIBRARY_REPO = "https://github.com/NoraStory/reverse-SKILL"

    /**
     * 国内可用的 GitHub 加速代理。更新检测与 APK 下载都优先走它，
     * 失败时再直连 github.com。
     */
    const val GH_PROXY = "https://gh-proxy.com/"

    /** 给任意 github 链接套上加速代理 */
    fun proxied(url: String): String =
        if (url.startsWith("http") && !url.startsWith(GH_PROXY)) GH_PROXY + url else url

    const val AUTHOR_NAME = "Mr-losfer"
    const val AUTHOR_URL = "https://github.com/$GITHUB_OWNER"

    /** LOSFER 官网与 API 中心 */
    const val LOSFER_WEBSITE = "https://losfer.cn"
    const val LOSFER_API_PORTAL = "https://api.losfer.cn/pc/api-list/keys/"
}
