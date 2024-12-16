@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies
import io.tagd.core.dependencies
import io.tagd.di.Scope
import io.tagd.langx.Callback
import io.tagd.langx.Context
import io.tagd.langx.ref.weak
import io.verse.storage.core.Storage
import io.verse.storage.core.StorageInitializer
import java.lang.ref.WeakReference

actual open class AppBundleLibraryInitializer<S : Scopable> actual constructor(
    context: Context,
    within: S
) : AbstractWithinScopableInitializer<S, AppBundleLibrary>(within) {

    private var weakContext: WeakReference<Context>? = context.weak()

    private val context
        get() = weakContext?.get()!!

    private val themeResolver = ThemeResolver()

    override fun initialize(callback: Callback<Unit>) {
        new(newDependencies() + dependencies(ARG_CONTEXT to context))
        super.initialize(callback)
    }

    override fun new(dependencies: Dependencies): AppBundleLibrary {
        val context = dependencies.get<Context>(ARG_CONTEXT)!!
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!
        val libraryName = dependencies.get<String>(ARG_NAME)
            ?: "${outerScope.name}/${AppBundleLibrary.Builder.NAME}"

        return AppBundleLibrary.Builder()
            .name(libraryName)
            .scope(outerScope)
            .weakContext(WeakReference(context))
            .build().also { library ->
                library.setStoreAndLoadBundle(context)
            }
    }

    private fun AppBundleLibrary.setStoreAndLoadBundle(context: Context) {
        val store = newPreferencesStore(context, newStorage())
        setStoreAndLoadBundle(store) { actual ->
            actual.installIdentifier.invalidate(context)
            actual.copy(themeLabel = themeResolver.themeLabel(context)).also { copied ->
                actual.copySuper(copied)
            }
        }
    }

    protected open fun AppBundleLibrary.newStorage(): Storage {
        val storage = StorageInitializer(this).new(
            dependencies = dependencies(
                StorageInitializer.ARG_OUTER_SCOPE to thisScope
            )
        )
        return storage
    }

    protected open fun newPreferencesStore(context: Context, storage: Storage): AppBundleDao {
        return AppBundleDao(
            name = DEFAULT_STORE_NAME,
            path = context.filesDir.path,
            accessor = storage.dataObjectFileAccessor
        )
    }

    companion object {
        const val ARG_OUTER_SCOPE = AbstractWithinScopableInitializer.ARG_OUTER_SCOPE
        const val ARG_CONTEXT = "context"
        const val ARG_NAME = "name"
        const val DEFAULT_STORE_NAME = "app_bundle_store.json"
    }
}