package com.baidu.BaiduMap.Utils.Theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * 深色模式工具
 *
 * @author D10NG
 * @date on 2020/4/27 2:03 PM
 */
class NightModeUtils constructor(context: Context) {

    private val mSpf = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    companion object {

        @Volatile
        private var INSTANCE: NightModeUtils? = null

        @JvmStatic
        fun getInstance(context: Context) : NightModeUtils =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: NightModeUtils(context).also {
                        INSTANCE = it
                    }
                }

        private const val SPF_THEME_MODE = "theme_mode"
    }

    /** 获取设置 */
    private fun getSpfThemeMode() : Int = mSpf.getInt(SPF_THEME_MODE, ThemeMode.MODE_FOLLOW_SYSTEM.intValue)

    /** 设置 */
    private fun setSpfThemeMode(mode: Int) {
        mSpf.edit().putInt(SPF_THEME_MODE, mode).apply()
    }

    /** 获取设置好的ThemeMode */
    fun getThemeMode(): ThemeMode = ThemeMode.parseOfInt(getSpfThemeMode())

    /** 设置模式 */
    fun setThemeMode(mode: ThemeMode) {
        setSpfThemeMode(mode.intValue)

        // Inflate the layout for this fragment
    }

    /**
     * 应用存储的设置
     */
    fun applySetting() {
        // 检查主题
        val themeModeInt = getSpfThemeMode()
        val themeMode = ThemeMode.parseOfInt(themeModeInt)
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(when(themeMode) {
            ThemeMode.MODE_ALWAYS_ON -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeMode.MODE_ALWAYS_OFF -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.MODE_FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        })
    }
}