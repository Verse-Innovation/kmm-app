package io.verse.app

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.tagd.arch.access.library
import io.tagd.arch.test.FakeScopable
import io.tagd.core.dependencies
import io.tagd.di.Scope
import io.tagd.langx.Context
import io.verse.storage.core.Storage
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class AppBundleLibraryInitializerTest {

    val context = mock<Context>()
    private val fakeScopable = FakeScopable()
    private val initializer = AppBundleLibraryInitializer<FakeScopable>(context, fakeScopable)

    @Test
    fun `new should initialize appLibrary with the given outerScope`() {
        val context = mock<Context>()
        val libraryName = "appBundleLibrary"
        val outerScope = Scope("outerScope")
        whenever(context.filesDir).thenReturn(File("build/test"))

        val library = initializer.new(
            dependencies = dependencies(
                AppBundleLibraryInitializer.ARG_CONTEXT to context,
                AppBundleLibraryInitializer.ARG_NAME to libraryName,
                AppBundleLibraryInitializer.ARG_OUTER_SCOPE to outerScope
            )
        )

        assertEquals(library, outerScope.library<AppBundleLibrary>())
    }

    @Test
    fun `new should initialize storage with the appScope`() {
        val context = mock<Context>()
        val libraryName = "appBundleLibrary"
        val outerScope = Scope("outerScope")
        whenever(context.filesDir).thenReturn(File("build/test"))

        initializer.new(
            dependencies = dependencies(
                AppBundleLibraryInitializer.ARG_CONTEXT to context,
                AppBundleLibraryInitializer.ARG_NAME to libraryName,
                AppBundleLibraryInitializer.ARG_OUTER_SCOPE to outerScope
            )
        )

        assert(outerScope.subScope(libraryName) != null)
        assert(outerScope.subScope(libraryName)?.library<Storage>() != null)
    }
}