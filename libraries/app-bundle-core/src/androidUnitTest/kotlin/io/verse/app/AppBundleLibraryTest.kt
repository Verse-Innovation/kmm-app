package io.verse.app

import io.tagd.arch.access.library
import io.tagd.arch.test.FakeInjector
import io.tagd.arch.test.FakeScopable
import io.tagd.core.dependencies
import io.tagd.di.Global
import io.tagd.di.Scope
import io.tagd.langx.Locale
import io.tagd.langx.time.UnixEpochInMillis
import io.verse.storage.core.Storage
import io.verse.storage.core.StorageInitializer
import org.junit.After
import org.junit.Before
import org.mockito.kotlin.mock
import java.lang.ref.WeakReference
import kotlin.test.Test

class AppBundleLibraryTest {

    private val fakeScopable = FakeScopable()

    private val appScope = Scope("app_scope")

    private lateinit var dao: AppBundleDao
    private lateinit var library: AppBundleLibrary

    @Before
    fun setup() {
        FakeInjector.inject()
        StorageInitializer(fakeScopable).new(
            dependencies = dependencies(
                StorageInitializer.ARG_OUTER_SCOPE to appScope
            )
        )
        dao = newDao()
    }

    @After
    fun tearDown() {
        dao.delete(success = null, failure = null)
        Global.release()
        FakeInjector.release()
    }

    private fun newDao(): AppBundleDao {
        return AppBundleDao(
            name = "library.txt",
            path = "build",
            accessor = appScope.library<Storage>()?.dataObjectFileAccessor
        )
    }

    @Test
    fun `appBundle should be null if no persisted value exists`() {
        library = buildLibrary()

        assert(library.appBundle == null)
    }

    @Test
    fun `build should read and update app bundle from the dao`() {
        val appBundle = newAppBundle()
        dao.write(appBundle)

        library = buildLibrary()

        assert(library.appBundle != null)
    }

    @Test
    fun `enricher should enrich the appBundle`() {
        val appBundle = newAppBundle()
        val enrichedAppBundle = appBundle.copy(namespace = "enrichedAppBundle")
        dao.write(appBundle)

        library = buildLibrary(enricher = {
            enrichedAppBundle
        })

        assert(library.appBundle?.namespace == "enrichedAppBundle")
    }

    @Test
    fun `update should persist the appBundle`() {
        val appBundle = newAppBundle()
        library = buildLibrary()

        library.update(appBundle)

        var storedBundle: AppBundle? = null
        assert(library.appBundle != null)
        dao.read(success = {
            storedBundle = it
        })
        assert(storedBundle != null)
    }

    private fun buildLibrary(enricher: ((AppBundle) -> AppBundle)? = null): AppBundleLibrary {
        return AppBundleLibrary.Builder()
            .scope(appScope)
            .weakContext(WeakReference(mock()))
            .build().also {
                it.setStoreAndLoadBundle(dao, enricher)
            }
    }

    private fun newAppBundle(): AppBundle {
        return AppBundle(
            namespace = "com.example",
            versionName = "1.0",
            currentVersionCode = 1,
            previousVersionCode = 1,
            buildType = "prod",
            flavour = "default",
            flavorDimension = "free",
            profilable = true,
            installTime = UnixEpochInMillis(),
            appLocale = Locale.get(java.util.Locale.ENGLISH.toLanguageTag()),
            systemLocale = Locale.default(),
            localityLocale = Locale.get(java.util.Locale.ENGLISH.toLanguageTag()),
            themeLabel = "light",
            publishingIdentifier = "com.example",
            installIdentifier = InstallIdentifier(),
        )
    }

}