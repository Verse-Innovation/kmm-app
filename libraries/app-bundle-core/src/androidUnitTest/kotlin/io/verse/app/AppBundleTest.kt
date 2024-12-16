package io.verse.app

import com.nhaarman.mockito_kotlin.mock
import io.tagd.langx.Context
import io.tagd.langx.Locale
import io.tagd.langx.time.UnixEpochInMillis
import io.verse.system.Device
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppBundleTest {

    private val bundle = newAppBundle()

    private fun newAppBundle(): AppBundle {
        return AppBundle(
            versionName = "1.0",
            currentVersionCode = 1,
            previousVersionCode = 1,
            flavour = "free",
            buildType = "prod",
            namespace = "com.example",
            profilable = true,
            installTime = UnixEpochInMillis(),
            flavorDimension = null,
            appLocale = null,
            themeLabel = null,
            localityLocale = null,
            systemLocale = Locale.default(),
            publishingIdentifier = "com.example",
            installIdentifier = InstallIdentifier()
        )
    }

    @Test
    fun `given an AppBundle then verify it is not null`() {
        assertTrue(bundle != null)
    }

    @Test
    fun `bundle should cache isDeviceWhitelisted`() {
        val context = mock<Context>()
        val isDeviceWhitelisted = bundle.runningOnWhitelisted(listOf(), Device(context))

        assertEquals(isDeviceWhitelisted, bundle.whitelisted)
    }
}