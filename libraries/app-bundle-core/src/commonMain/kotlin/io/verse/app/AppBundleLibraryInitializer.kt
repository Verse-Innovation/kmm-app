@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.langx.Context

expect open class AppBundleLibraryInitializer<S : Scopable>(context: Context, within: S) :
    AbstractWithinScopableInitializer<S, AppBundleLibrary>