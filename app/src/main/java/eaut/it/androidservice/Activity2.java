package eaut.it.androidservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity2 extends AppCompatActivity {
    MyService mService;
    Boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mIsBound){
            unbindService(serviceConnection);
            mIsBound = false;
        }
    }

    private void bindService(){
        Intent serviceBindIntent =  new Intent(this, MyService.class);
        bindService(serviceBindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            MyService.MyBinder binder = (MyService.MyBinder) iBinder;
            mService = binder.getService();
            mIsBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mIsBound = false;
        }
    };
}