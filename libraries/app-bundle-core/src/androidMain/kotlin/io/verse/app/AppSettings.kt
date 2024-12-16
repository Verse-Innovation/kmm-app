@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import io.tagd.arch.control.IApplication

actual class NotificationSettings actual constructor(context: IApplication) {

    actual val enabledAll: Boolean
        get() = _enabledAll

    actual val channels: List<NotificationChannel>
        get() = _channels

    actual val disabledChannels: List<NotificationChannel>
        get() = _disabledChannels

    actual val groups: List<NotificationChannelGroup>
        get() = _groups

    actual val disabledGroups: List<NotificationChannelGroup>
        get() = _disabledGroups

    private var _enabledAll: Boolean = false
    private lateinit var _channels: List<NotificationChannel>
    private lateinit var _disabledChannels: List<NotificationChannel>
    private lateinit var _groups: List<NotificationChannelGroup>
    private lateinit var _disabledGroups: List<NotificationChannelGroup>

    init {
        load(context as Application)
    }

    private fun load(context: Context) {
        _enabledAll = false
        val disabledChannels: MutableList<NotificationChannel> = ArrayList()
        val disabledGroups: MutableList<NotificationChannelGroup> = ArrayList()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager
                _enabledAll = manager.areNotificationsEnabled()
                _channels = manager.notificationChannels
                for (channel in _channels) {
                    if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                        disabledChannels.add(channel)
                    }
                }
                _disabledChannels = disabledChannels

                _groups = manager.notificationChannelGroups
                for (group in _groups) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (group.isBlocked) {
                            disabledGroups.add(group)
                        }
                    }
                }
                _disabledGroups = disabledGroups
            } else {
                _channels = emptyList()
                _disabledChannels = emptyList()
                _groups = emptyList()
                _disabledGroups = emptyList()
                _enabledAll = NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun disabledChannelIds(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _disabledChannels.map { it.id }
        } else {
            emptyList()
        }
    }

    actual fun disabledChannelGroupIds(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _disabledGroups.map { it.id }
        } else {
            emptyList()
        }
    }
}

actual typealias NotificationChannel = android.app.NotificationChannel

actual typealias NotificationChannelGroup = android.app.NotificationChannelGroup