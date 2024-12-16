@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import io.tagd.langx.Context

expect class ThemeResolver {

    fun themeLabel(context: Context): String

    companion object {
        val DARK_THEME: String
        val LIGHT_THEME: String
    }

}