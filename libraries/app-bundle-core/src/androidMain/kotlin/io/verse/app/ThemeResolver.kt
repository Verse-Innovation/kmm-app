@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import android.content.res.Configuration
import io.tagd.langx.Context

actual class ThemeResolver {

    actual fun themeLabel(context: Context): String {
        return if (isNightMode(context)) {
            DARK_THEME
        } else {
            LIGHT_THEME
        }
    }

    private fun isNightMode(context: Context): Boolean {
        val nightModeFlag = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    actual companion object {
        actual const val LIGHT_THEME = "light"
        actual const val DARK_THEME = "dark"
    }

}