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
    val GITHUB_LICENSE_URL = "$GITHUB_URL/blob/master/LICENSE"

    const val AUTHOR_NAME = "Mr-losfer"
    const val AUTHOR_URL = "https://github.com/$GITHUB_OWNER"

    /** LOSFER 官网与 API 中心 */
    const val LOSFER_WEBSITE = "https://losfer.cn"
    const val LOSFER_API_PORTAL = "https://api.losfer.cn/pc/api-list/keys/"
}
