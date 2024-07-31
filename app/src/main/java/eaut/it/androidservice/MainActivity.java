package eaut.it.androidservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button start, stop, btnGoto;

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
        btnGoto = findViewById(R.id.btnGoto);
        start = findViewById(R.id.startButton);
        stop = findViewById(R.id.stopButton);
        btnGoto.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == start) {
            startService(new Intent(this, MyService.class));
        } else if (view == stop) {
            stopService(new Intent(this, MyService.class));
        }else if(view==btnGoto){
            Intent intent = new Intent(this,Activity2.class);
            startActivity(intent);
        }
    }
}