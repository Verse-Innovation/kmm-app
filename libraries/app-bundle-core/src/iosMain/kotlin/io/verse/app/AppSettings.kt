@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.app

import io.tagd.arch.control.IApplication

actual class NotificationSettings actual constructor(context: IApplication) {

    actual val enabledAll: Boolean
        get() = true

    actual val channels: List<NotificationChannel>
        get() = listOf()

    actual val disabledChannels: List<NotificationChannel>
        get() = listOf()

    actual val groups: List<NotificationChannelGroup>
        get() = listOf()

    actual val disabledGroups: List<NotificationChannelGroup>
        get() = listOf()

    actual fun disabledChannelIds(): List<String> {
        TODO("Not yet implemented")
    }

    actual fun disabledChannelGroupIds(): List<String> {
        TODO("Not yet implemented")
    }
}

actual class NotificationChannel

actual class NotificationChannelGroup