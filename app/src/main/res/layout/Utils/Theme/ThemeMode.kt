package com.baidu.BaiduMap.Utils.Theme

import android.content.Context
import com.baidu.BaiduMap.R

/**
 * 主题开启模式
 * @author D10NG
 * @date on 2020/2/26 10:02 AM
 */
enum class ThemeMode(val intValue: Int, val stringValueId: Int) {
    MODE_ALWAYS_ON(0, R.string.settings_theme_dark),
    MODE_ALWAYS_OFF(1, R.string.settings_theme_light),
    MODE_FOLLOW_SYSTEM(2, R.string.settings_theme_sys);

    companion object {
        @JvmStatic
        fun parseOfString(context: Context, String: String): ThemeMode {
            return when(String) {
                context.resources.getString(MODE_ALWAYS_ON.stringValueId) -> MODE_ALWAYS_ON
                context.resources.getString(MODE_ALWAYS_OFF.stringValueId) -> MODE_ALWAYS_OFF
                else -> MODE_FOLLOW_SYSTEM
            }
        }

        @JvmStatic
        fun parseOfInt(intValue: Int) : ThemeMode {
            return when(intValue) {
                MODE_ALWAYS_ON.intValue -> MODE_ALWAYS_ON
                MODE_ALWAYS_OFF.intValue -> MODE_ALWAYS_OFF
                else -> MODE_FOLLOW_SYSTEM
            }
        }
    }
}