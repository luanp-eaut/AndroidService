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
    public int onStartCommand(Intent intent, int flags, int startId) {
        run();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        run();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stop();
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }


    private void run() {
        if (player == null)
            player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        player.start();
    }

    private void stop(){
        if(player!=null) player.stop();
    }
}
