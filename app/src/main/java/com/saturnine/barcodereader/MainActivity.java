package com.saturnine.barcodereader;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {
    TextView result_text;
    Button scan_button;
    final static int REQUEST_CODE = 100;
    final static int PERMISSION_REQUEST = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result_text = findViewById(R.id.result_text);
        scan_button = findViewById(R.id.button_scan);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST);
        }

        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result_text.post(new Runnable() {
                    @Override
                    public void run() {
                        result_text.setText(barcode.displayValue);
                        Toast.makeText(MainActivity.this, barcode.displayValue, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}