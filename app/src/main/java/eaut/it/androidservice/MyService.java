package eaut.it.androidservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private MediaPlayer player;
    private final IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        player.start();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.start();
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
}
