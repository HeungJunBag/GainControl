package com.heungjun.gaincontrol.viewmodel

import android.Manifest
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val replyText = remoteInput?.getCharSequence("key_text_reply").toString()

        val category = intent.getStringExtra("category") ?: "default"

        Log.d("ReplyReceiver", "Reply received: $replyText for category: $category")

        // 확인 알림을 보냄
        val confirmationNotification = NotificationCompat.Builder(context, MyFirebaseMessagingService.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Reply Sent")
            .setContentText("Your reply: \"$replyText\" has been sent.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        // 권한 체크 후 알림 보내기
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        NotificationManagerCompat.from(context).notify(category.hashCode(), confirmationNotification)
    }
}

