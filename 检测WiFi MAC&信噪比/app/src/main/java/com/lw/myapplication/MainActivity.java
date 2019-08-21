package com.lw.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView t;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final StringBuilder[] stringBuilder = new StringBuilder[1];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = findViewById(R.id.textView);
        b = findViewById(R.id.button);
        b.setOnClickListener(v -> {
                    WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
                    if (!wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(true);
                    }
                    wifiManager.startScan();
                    List<ScanResult> scanWifiList = wifiManager.getScanResults();
                    stringBuilder[0] = new StringBuilder();
                    for (ScanResult item : scanWifiList) {
                        stringBuilder[0].append(item.SSID + "\n" + item.BSSID + "   " + item.level + "\n\n\n");
                    }
                    //

                }
        );
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("完成");
                t.setText(stringBuilder[0]);
            }
        },new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        checkPermission += ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
        checkPermission += ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        checkPermission += ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        checkPermission += ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        checkPermission += ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_WIFI_STATE);
        checkPermission += ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CHANGE_WIFI_STATE);
        checkPermission += ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WAKE_LOCK);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int grantResult : grantResults) {
                if (grantResult != 0) {
                    Toast.makeText(MainActivity.this, "未获取权限", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
