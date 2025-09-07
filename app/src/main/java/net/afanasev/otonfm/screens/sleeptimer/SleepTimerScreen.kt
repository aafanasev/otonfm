package net.afanasev.otonfm.screens.sleeptimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import net.afanasev.otonfm.R
import net.afanasev.otonfm.log.Logger
import net.afanasev.otonfm.services.PlaybackService
import net.afanasev.otonfm.ui.components.Dialog
import net.afanasev.otonfm.ui.components.DialogItem
import java.util.concurrent.TimeUnit

@Composable
fun SleepTimerScreen() {
    val context = LocalContext.current

    Dialog {

        if (true) {
            Text(text = "Sleep timer is on")
        }

        DialogItem(
            R.string.sleep_timer_off,
            onClick = {

            },
        )
        DialogItem(
            R.string.sleep_timer_10min,
            onClick = {
                val am = context.getSystemService(AlarmManager::class.java)
                val intent =
                    Intent(PlaybackService.ACTION_SLEEP_TIMER).setPackage(context.packageName)
                val pi = PendingIntent.getBroadcast(
                    context,
                    1001,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val triggerAt = SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(1)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (am.canScheduleExactAlarms()) {
                        println("asdasd schedule")
                        am.setExactAndAllowWhileIdle(
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            triggerAt,
                            pi
                        )
                    } else {
                        // TODO: request
                        println("asdasd need to request")

                        val requestPermission =
                            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                data = "package:${context.packageName}".toUri()
                            }
                        context.startActivity(requestPermission)
                    }
                } else {
                    println("asdasd schedule 2")
                    am.setExactAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        triggerAt,
                        pi
                    )
                }
            },
        )
        DialogItem(
            R.string.sleep_timer_15min,
            onClick = {

            },
        )
        DialogItem(
            R.string.sleep_timer_20min,
            onClick = {

            },
        )
        DialogItem(
            R.string.sleep_timer_30min,
            onClick = {

            },
        )
        DialogItem(
            R.string.sleep_timer_45min,
            onClick = {

            },
        )
        DialogItem(
            R.string.sleep_timer_60min,
            onClick = {

            },
        )
    }

}