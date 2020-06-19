package com.saturnine.barcodereader;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;

public class ScanActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    BarcodeDetector barcodeDetector;
    SurfaceHolder surfaceHolder;
    CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setZOrderMediaOverlay(true);
        surfaceHolder = surfaceView.getHolder();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        if(!barcodeDetector.isOperational()){
            Toast.makeText(getApplicationContext(), "Could not set up a detector", Toast.LENGTH_LONG).show();
            finish();
        }

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1024)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if(ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED){
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() > 0){
                    Intent intent = new Intent();
                    intent.putExtra("barcode", barcodes.valueAt(0));
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });

    }
}