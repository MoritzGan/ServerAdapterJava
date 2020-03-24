package com.demobledistance;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class ScanForMobiles {

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private Activity activity;

    public ScanForMobiles(Activity activity) {
       bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
       bluetoothAdapter = bluetoothManager.getAdapter();
       this.activity = activity;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();

                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                Log.d(""," " + rssi);

                if(rssi > -60 && deviceName != null) {
                    // Do sth here. Devices here are near enough

                }
            }
        }
    };

    public void startScan() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(receiver, filter); // onFound receiver is called

        bluetoothAdapter.startDiscovery();
    }

    public JSONObject createCookie(String userid) throws JSONException {
        JSONObject cookie = new JSONObject();

        byte[] data = userid.getBytes(StandardCharsets.UTF_8);
        String encrData = Base64.encodeToString(data, Base64.DEFAULT);
        String timestamp = String.valueOf(new java.util.Date().getTime());

        cookie.put("UUID", encrData);
        cookie.put("Timestamp", timestamp);
        
        return cookie;
    }
}
