package com.lw.attendancesystem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class AppJavaScriptProxy {

    WifiManager wifiManager;
    AppJavaScriptProxy(Activity activity, WebView webView) {
        wifiManager= (WifiManager) activity.getApplication().getSystemService(WIFI_SERVICE);
        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<ScanResult> scanWifiList = wifiManager.getScanResults();
                ArrayList<String> bssId = new ArrayList<>();
                scanWifiList.forEach((item)-> bssId.add(item.BSSID));
                System.out.println("Scan完成");
                Toast.makeText(activity.getApplicationContext(),JSON.toJSONString(bssId),Toast.LENGTH_LONG).show();
                webView.loadUrl("javascript:sendResult('"+JSON.toJSONString(bssId) +"')");
            }
        },new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }
    @JavascriptInterface
    public void getWifiList() {
        System.out.println("开始搜索~");
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
    }

}