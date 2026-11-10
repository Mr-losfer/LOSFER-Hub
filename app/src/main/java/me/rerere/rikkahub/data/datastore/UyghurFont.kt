package me.rerere.rikkahub.data.datastore

import androidx.annotation.FontRes
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import me.rerere.rikkahub.R

/**
 * LOSFER Hub 内置的维吾尔语字体。
 *
 * 字库来自 LOSFER 维吾尔语字体库（http://ccyun.losfer.top/fonts/），字体版权归原作者所有。
 * 默认字体为技能给出的默认字体 ALKATIP Asliya（经典正文体）。
 */
enum class UyghurFont(
    val id: String,
    @param:FontRes val fontRes: Int,
    // 字体本名，用于展示
    val displayName: String,
    val description: String,
) {
    ALKATIP_ASLIYA(
        id = "alkatip_asliya",
        fontRes = R.font.alkatip_asliya,
        displayName = "ALKATIP Asliya",
        description = "ئەسلىيە — ئەنئەنىۋى تېكىست شەكلى",
    ),
    ALKATIP_BASMA(
        id = "alkatip_basma",
        fontRes = R.font.alkatip_basma,
        displayName = "ALKATIP Basma",
        description = "باسما — گېزىت بېسىم شەكلى",
    ),
    ALKATIP_KITAB(
        id = "alkatip_kitab",
        fontRes = R.font.alkatip_kitab,
        displayName = "ALKATIP Kitab",
        description = "كىتاب — كىتاب تەرتىپ شەكلى",
    ),
    ALPSOFT_BASMA_B(
        id = "alpsoftbasma_b",
        fontRes = R.font.alpsoftbasma_b,
        displayName = "AlpsoftBasma-b",
        description = "ئالپسوفت — زامانىۋى ۋە ئېنىق",
    ),
    UKIJ_TUZ(
        id = "ukijtuz",
        fontRes = R.font.ukijtuz,
        displayName = "UKIJ Tuz",
        description = "UKIJ — ئۆلچەملىك تۈز شەكلى",
    ),
    UUS_UN_ASLIYA(
        id = "uus_un_asliya",
        fontRes = R.font.uus_un_asliya,
        displayName = "UUS UN Asliya",
        description = "UUSUN — كىچىك ھەجىم، تۈز قۇرۇلما",
    ),
    ;

    val fontFamily: FontFamily
        get() = FontFamily(Font(fontRes))

    companion object {
        val DEFAULT = ALKATIP_ASLIYA

        fun fromId(id: String?): UyghurFont = entries.find { it.id == id } ?: DEFAULT
    }
}
