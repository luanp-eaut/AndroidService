package eaut.it.androidservice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button start, stop, btnGoto, foregroundBtn;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        foregroundBtn = findViewById(R.id.foregroundButton);
        btnGoto = findViewById(R.id.btnGoto);
        start = findViewById(R.id.startButton);
        stop = findViewById(R.id.stopButton);
        foregroundBtn.setOnClickListener(this);
        btnGoto.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        });

        requestPermission();
    }

    @Override
    public void onClick(View view) {
        if (view == start) {
            startService(new Intent(this, DemoService.class));
        } else if (view == stop) {
            stopService(new Intent(this, DemoService.class));
        } else if (view == btnGoto) {
            Intent intent = new Intent(this, Activity2.class);
            startActivity(intent);
        } else if (view == foregroundBtn) {
            Intent intent = new Intent(this, DemoService.class);
            intent.setAction("foreground");
            Context context = getApplicationContext();
            context.startForegroundService(intent);
        }
    }

    private void requestPermission() {
        int p1 = ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS);
        int p2 = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.FOREGROUND_SERVICE);
        if (p1 != PackageManager.PERMISSION_GRANTED ||
                p2 != PackageManager.PERMISSION_GRANTED)
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }
}