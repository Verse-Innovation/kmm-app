package io.verse.app.android

import android.content.res.Configuration
import io.tagd.android.app.TagdApplication
import io.tagd.arch.access.library
import io.tagd.arch.control.ApplicationInjector
import io.verse.app.AppBundleLibrary
import io.verse.app.ThemeResolver

class SampleApplication : TagdApplication() {

    private val themeResolver = ThemeResolver()

    override fun newInjector(): ApplicationInjector<out TagdApplication> {
        return MyInjector(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updatedAppBundle()
    }

    private fun updatedAppBundle() {
        library<AppBundleLibrary>()?.apply {
            appBundle?.let {
                val currentThemeLabel = themeResolver.themeLabel(this@SampleApplication)
                if (currentThemeLabel != it.themeLabel) {
                    update(it.copy(themeLabel = currentThemeLabel))
                }
            }
        }
    }
}