package io.verse.app

import io.tagd.core.annotation.VisibleForTesting
import io.tagd.langx.Locale
import io.tagd.langx.time.UnixEpochInMillis
import io.verse.storage.core.SavablePropertiesObject
import io.verse.system.Device
import kotlin.jvm.Transient

data class AppBundle(
    val namespace: String,
    val versionName: String,
    val currentVersionCode: Int,
    val previousVersionCode: Int,
    val flavour: String?,
    val flavorDimension: String?,
    val buildType: String,
    val profilable: Boolean,
    val installTime: UnixEpochInMillis,
    // todo remove [localityLocale] from appLibrary,
    //  it is not a generic property across apps,
    //  we can use extension properties for any specific app.
    var localityLocale: Locale?,
    var appLocale: Locale?,
    var systemLocale: Locale,
    var themeLabel: String?,
    val publishingIdentifier: String,
    val installIdentifier: InstallIdentifier,
) : SavablePropertiesObject() {

    @Transient
    var commitListener: ((AppBundle) -> Unit)? = null

    @VisibleForTesting
    internal var whitelisted: Boolean? = null

    val buildVariant
        get() = "$flavour$flavorDimension$buildType"

    fun runningOnWhitelisted(manufacturers: List<String>, device: Device): Boolean {
        return whitelisted ?: kotlin.run {
            device.firmware.inManufactures(manufacturers).also {
                whitelisted = it
            }
        }
    }

    fun resolvedLocale(): Locale {
        return appLocale ?: systemLocale
    }

    override fun commit() {
        super.commit()
        commitListener?.invoke(this)
    }
}