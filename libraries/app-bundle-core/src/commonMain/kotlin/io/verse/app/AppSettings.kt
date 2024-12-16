@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import io.tagd.arch.control.IApplication

data class AppSettings(val notificationSettings: NotificationSettings)

expect class NotificationSettings(context: IApplication) {
    val enabledAll: Boolean
    val channels: List<NotificationChannel>
    val disabledChannels: List<NotificationChannel>
    val groups: List<NotificationChannelGroup>
    val disabledGroups: List<NotificationChannelGroup>

    fun disabledChannelIds(): List<String>

    fun disabledChannelGroupIds(): List<String>
}

expect class NotificationChannel

expect class NotificationChannelGroup