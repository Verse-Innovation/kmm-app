@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies
import io.tagd.langx.Context

actual open class AppBundleLibraryInitializer<S : Scopable> actual constructor(
    context: Context,
    within: S
) : AbstractWithinScopableInitializer<S, AppBundleLibrary>(within) {

    override fun new(dependencies: Dependencies): AppBundleLibrary {
        TODO("Not yet implemented")
    }
}