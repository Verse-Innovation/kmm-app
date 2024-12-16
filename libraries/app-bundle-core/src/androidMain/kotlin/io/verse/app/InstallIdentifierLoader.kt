package io.verse.app

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.arch.domain.crosscutting.async.cancelAsync
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.langx.Callback
import io.tagd.langx.datatype.UUID

object InstallIdentifierLoader: AsyncContext {

    fun newInstallIdentifier(
        context: Context
    ): InstallIdentifier {

        val appGeneratedId = newApplicationId()
        val platformLevelId = loadPlatformLevelId(context)

        return InstallIdentifier(
            appGeneratedId = appGeneratedId,
            platformLevelId = platformLevelId
        ).also {
            loadGoogleAdvertisementId(context) { id ->
                it.googleAdvertisementId = id
            }
        }
    }

    private fun newApplicationId(): String {
        return UUID().value;
    }

    @SuppressLint("HardwareIds")
    private fun loadPlatformLevelId(context: Context): String? {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    internal fun loadGoogleAdvertisementId(
        context: Context,
        callback: Callback<String?>
    ) {

        compute {
            val advertisementId = try {
                AdvertisingIdClient.getAdvertisingIdInfo(context).id
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            callback.invoke(advertisementId)
        }
    }

    override fun release() {
        cancelAsync()
    }
}

fun InstallIdentifier.invalidate(context: Context) {
    InstallIdentifierLoader.loadGoogleAdvertisementId(context) {
        googleAdvertisementId = it
    }
}