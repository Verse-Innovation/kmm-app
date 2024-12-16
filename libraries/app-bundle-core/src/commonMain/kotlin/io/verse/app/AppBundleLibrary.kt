package io.verse.app

import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.tagd.langx.Context
import io.tagd.langx.collection.concurrent.CopyOnWriteArraySet
import io.tagd.langx.ref.WeakReference
import io.verse.storage.core.FilableDao
import io.verse.storage.core.FilableDataObject

class AppBundleLibrary private constructor(name: String, outerScope: Scope) :
    AbstractLibrary(name, outerScope) {

    var appBundle: AppBundle? = null
        private set

    private lateinit var store: AppBundleDao

    private lateinit var weakContext: WeakReference<Context>

    private val listeners: CopyOnWriteArraySet<WeakReference<AppBundleUpdateListener>> =
        CopyOnWriteArraySet()

    fun setStoreAndLoadBundle(store: AppBundleDao, enricher: ((AppBundle) -> AppBundle)? = null) {
        this.store = store
        loadBundle(enricher)
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadBundle(enricher: ((AppBundle) -> AppBundle)? = null) {
        store.read(success = { storedBundle ->
            appBundle = enricher?.invoke(storedBundle) ?: storedBundle
            appBundle?.dao = store as FilableDao<FilableDataObject>
            appBundle?.commitListener = {
                notifyListeners(it)
            }
        }, failure = {
            it.printStackTrace()
        })
    }

    fun addAppBundleUpdateListener(listener: AppBundleUpdateListener) {
        listeners.add(WeakReference(listener))
        appBundle?.let { listener.onUpdate(it) }
    }

    fun removeAppBundleUpdateListener(listener: AppBundleUpdateListener) {
        listeners.firstOrNull {
            it.get() == listener
        }?.let {
            listeners.remove(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun update(bundle: AppBundle) {
        appBundle?.let {
            bundle.commitListener = it.commitListener
        }

        appBundle = bundle
        appBundle?.dao = store as FilableDao<FilableDataObject>
        store.writeAsync(bundle)

        notifyListeners(bundle)
    }

    private fun notifyListeners(bundle: AppBundle) {
        listeners.forEach {
            it.get()?.onUpdate(bundle)
        }
    }

    override fun release() {
        appBundle = null
        listeners.clear()
        super.release()
    }

    class Builder : Library.Builder<AppBundleLibrary>() {

        private lateinit var weakContext: WeakReference<Context>

        override fun name(name: String?): Builder {
            this.name = name
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        fun weakContext(weakContext: WeakReference<Context>): Builder {
            this.weakContext = weakContext
            return this
        }

        override fun buildLibrary(): AppBundleLibrary {
            return AppBundleLibrary(
                name = name ?: "${outerScope.name}/$NAME",
                outerScope = outerScope
            ).also { library ->
                library.weakContext = weakContext
                outerScope.bind<Library, AppBundleLibrary>(instance = library)
            }
        }

        companion object {
            const val NAME = "app-bundle"
        }
    }
}

fun interface AppBundleUpdateListener {

    fun onUpdate(appBundle: AppBundle)
}