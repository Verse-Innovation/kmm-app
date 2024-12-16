package io.verse.app.android

import io.tagd.android.app.TagdApplication
import io.tagd.android.app.TagdApplicationInjector
import io.tagd.arch.scopable.Scopable
import io.tagd.arch.scopable.WithinScopableInitializer
import io.tagd.core.Dependencies
import io.tagd.langx.Context
import io.tagd.langx.Locale
import io.tagd.langx.time.UnixEpochInMillis
import io.verse.app.AppBundle
import io.verse.app.AppBundleLibrary
import io.verse.app.AppBundleLibraryInitializer
import io.verse.app.InstallIdentifierLoader
import io.verse.app.ThemeResolver

class MyInjector(app: TagdApplication) : TagdApplicationInjector<TagdApplication>(app) {

    override fun load(initializers: ArrayList<WithinScopableInitializer<TagdApplication, *>>) {
        super.load(initializers)
        initializers.add(SampleAppBundleLibraryInitializer(within, within))
    }
}

class SampleAppBundleLibraryInitializer<S : Scopable>(context: Context, within: S) :
    AppBundleLibraryInitializer<S>(context, within) {

    override fun new(dependencies: Dependencies): AppBundleLibrary {

        return super.new(dependencies).also { appBundleLibrary ->
            appBundleLibrary.appBundle
                ?: appBundleLibrary.update(newFreshBundle(within as SampleApplication))
        }
    }

    private fun newFreshBundle(app : SampleApplication) = AppBundle(
        versionName = BuildConfig.VERSION_NAME,
        currentVersionCode = BuildConfig.VERSION_CODE,
        previousVersionCode = BuildConfig.VERSION_CODE,
        flavour = BuildConfig.FLAVOR,
        buildType = BuildConfig.BUILD_TYPE,
        namespace = app.packageName,
        profilable = isProfilable(),
        installTime = UnixEpochInMillis(),
        flavorDimension = BuildConfig.FLAVOUR_DIMENSION,
        appLocale = Locale.default(),
        themeLabel = ThemeResolver().themeLabel(app),
        localityLocale = Locale.default(),
        systemLocale = Locale.default(),
        publishingIdentifier = BuildConfig.APPLICATION_ID,
        installIdentifier =
        InstallIdentifierLoader.newInstallIdentifier(app)
    )

    private fun isProfilable(): Boolean {
        return BuildConfig.BUILD_TYPE == "debug"
    }
}