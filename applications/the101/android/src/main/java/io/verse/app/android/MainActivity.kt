package io.verse.app.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.tagd.android.app.AppCompatActivity
import io.tagd.arch.control.IApplication
import io.verse.app.AppSettings
import io.verse.app.NotificationSettings

class MainActivity : AppCompatActivity() {

    override fun onCreateView(savedInstanceState: Bundle?) {
        val settings = AppSettings(NotificationSettings(application as IApplication))
        val builder = StringBuilder("notification settings \n")
        builder.append("enabled? " + settings.notificationSettings.enabledAll)
        settings.notificationSettings.channels.forEach {
            builder.append("channel --> " + it)
            builder.append(",")
        }
        settings.notificationSettings.groups.forEach {
            builder.append("group --> " + it)
            builder.append(",")
        }
        val message = builder.toString()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GreetingView(message)
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
