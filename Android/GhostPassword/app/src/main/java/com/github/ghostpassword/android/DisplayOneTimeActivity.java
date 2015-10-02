package com.github.ghostpassword.android;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class DisplayOneTimeActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 0;
    private TextView txResult;

    private OutputStream outputStream;
    private InputStream inStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_one_time);

        txResult = (TextView) findViewById(R.id.textResult);

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Button button = (Button) findViewById(R.id.callZxing);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(DisplayOneTimeActivity.this);
                integrator.addExtra("SCAN_WIDTH", 640);
                integrator.addExtra("SCAN_HEIGHT", 480);
                integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
                //customize the prompt message before scanning
                integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
                integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                try {
                    write(result.getContents());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("MainActivity", "Weird");
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void write(String s) throws IOException {
        if(outputStream == null){
            System.out.println("Output stream is null.");
        }
        outputStream.write(s.getBytes());
    }

    public void init() throws IOException{
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if(bondedDevices.size() > 0){
                    ArrayList<BluetoothDevice> devices = new ArrayList<>();
                    for(BluetoothDevice device : bondedDevices){
                        devices.add(device);
                    }
                    BluetoothDevice device = devices.get(0);
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = null;
                    try {
                        socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                        socket.connect();
                        outputStream = socket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("error", "No appropriate paired devices.");
                }
            }else{
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

}
