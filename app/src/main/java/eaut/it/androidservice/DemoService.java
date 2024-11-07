package eaut.it.androidservice;

import static android.app.Notification.FOREGROUND_SERVICE_IMMEDIATE;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import androidx.core.content.ContextCompat;

public class DemoService extends Service {
    private MediaPlayer player;
    private final IBinder mBinder = new MyBinder();
    private NotificationManager notificationManager;
    private Notification notification;

    public final String CHANNEL_ID = "1001";

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action == "foreground") startForeground();
        else if (action == "stop") stopSelf();
        else player.start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        player.start();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    public class MyBinder extends Binder {
        DemoService getService() {
            return DemoService.this;
        }
    }

    private void startForeground() {
        if (!havePermission()) {
            Toast.makeText(getApplicationContext(), "You don't have permission!", Toast.LENGTH_LONG).show();
            return;
        }

        createNotification();

        try {
            ServiceCompat.startForeground(this, 100, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE);
            player.start();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception here", Toast.LENGTH_LONG).show();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < 26) return;

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Foreground Service channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNotification() {
        Intent intent = new Intent(this, Activity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        Intent stopIntent = new Intent(this, DemoService.class);
        stopIntent.setAction("stop");
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent,PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);

        createNotificationChannel();

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("Foreground Service demonstration")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Stop", pendingStopIntent)
                .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
                .build();
    }

    private boolean havePermission() {
        int postNotification =
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS);
        int foregroundService = ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE);

        return (postNotification == PackageManager.PERMISSION_GRANTED && foregroundService == PackageManager.PERMISSION_GRANTED);
    }
}
