package com.heungjun.gaincontrol.viewmodel

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.heungjun.gaincontrol.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "firebase_channel"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // 서버에 토큰을 전송하려면 이곳에 로직 추가
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message received: ${remoteMessage.data}")

        // 알림 데이터 추출
        val title = remoteMessage.notification?.title ?: "Default Title"
        val body = remoteMessage.notification?.body ?: "Default Body"
        val category = remoteMessage.data["category"] ?: "default"

        // 알림 표시
        showNotification(title, body, category, this)
    }

    private fun showNotification(
        title: String,
        body: String,
        category: String,
        context: Context
    ) {
        // 채널 생성(Android 8.0 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Firebase Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Firebase Notifications"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val remoteInput = androidx.core.app.RemoteInput.Builder("key_text_reply")
            .setLabel("Reply here")
            .build()

        val replyIntent = Intent(context, ReplyReceiver::class.java).apply {
            putExtra("category", category)
        }

        // 수정된 부분: FLAG_MUTABLE로 변경
        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE // FLAG_MUTABLE로 수정
        )

        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(replyAction)
            .setAutoCancel(true)
            .build()

        // 권한 체크 후 알림 보내기
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없으면 리턴
            return
        }

        NotificationManagerCompat.from(context).notify(category.hashCode(), notification)
    }
}



