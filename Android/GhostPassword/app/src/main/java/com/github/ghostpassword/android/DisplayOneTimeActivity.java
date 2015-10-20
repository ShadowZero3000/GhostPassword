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

import com.github.ghostpassword.ghostpasswordbackend.BlueToothDao;
import com.github.ghostpassword.ghostpasswordbackend.GhostPasswordException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DisplayOneTimeActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 0;
    private TextView txResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_one_time);

        //txResult = (TextView) findViewById(R.id.textResult);
        IntentIntegrator integrator = new IntentIntegrator(DisplayOneTimeActivity.this);
        integrator.addExtra("SCAN_WIDTH", 640);
        integrator.addExtra("SCAN_HEIGHT", 480);
        integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
        //customize the prompt message before scanning
        integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
        integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);

    }

    public static Map<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String[] qp=param.split("=");
            String name = qp[0];
            String value = "";
            if(qp.length>1){
                value = qp[1];
            }
            map.put(name, value);
        }
        return map;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Log.d("MainActivity", result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                synchronized (this) {
                    try {
                        BlueToothDao dao = new BlueToothDao();
                        try {
                            String res=result.getContents();
                            URI url = new URI(res);
                            Map<String, String> map = getQueryMap(url.getQuery());
                            Set<String> keys = map.keySet();
                            if(!keys.contains("secret")){
                                Log.d("MainActivity","Invalid QR code");
                                Toast.makeText(this, "Invalid TOTP QR code", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("MainActivity","Code: "+map.get("secret"));
                                System.out.println("True result");
                                System.out.println(map.get("secret"));
                                dao.writeQR(map.get("secret"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        } finally {
                            dao.close();
                        }
                    } catch (GhostPasswordException e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                Intent main = new Intent(this, MainScreen.class);
                startActivity(main);
            }
        } else {
            Log.d("MainActivity", "Weird");
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }


}
