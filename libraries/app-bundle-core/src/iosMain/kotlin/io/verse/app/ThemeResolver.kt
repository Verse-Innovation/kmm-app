@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import io.tagd.langx.Context

actual class ThemeResolver {
    actual fun themeLabel(context: Context): String {
        TODO("Not yet implemented")
    }


    actual companion object {
        actual const val LIGHT_THEME = "light"
        actual const val DARK_THEME = "dark"
    }
}