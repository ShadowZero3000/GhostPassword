package rocks.codethat.ghostpassword;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jboss.aerogear.security.otp.Totp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 10;
    private BluetoothChatService mChatService = null;
    public static final String PREFS_NAME = "GhostPasswordData";
    public HashSet<String> OTP_keys;
    public HashSet<Totp> OTP_key_set;


    @Override
    protected void onDestroy(){
        if(mChatService != null){
            Log.d("MainAct","Stopping chat");
            mChatService.stop();
        }
        super.onDestroy();
    }

    protected void startChat() throws GhostPasswordException{
        mChatService = BluetoothChatService.getInstance();
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            mChatService.connect(mChatService.device, true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            startChat();
        } catch (GhostPasswordException e) {
             //Not much else we can do here
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChatService == null){
                    try {
                        startChat();
                    } catch (GhostPasswordException e) {
                        return;
                    }
                }
                if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                    Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
                    synchronized (this){
                        mChatService.connect(mChatService.device, true);
                    }
                }

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    doCaptureQR();
                }

            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChatService == null){
                    try {
                        startChat();
                    } catch (GhostPasswordException e) {
                        return;
                    }
                }
                if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                    Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
                    mChatService.connect(mChatService.device,true);
                    return;
                }
                try {
                    mChatService.writeString("Test message");
                } catch (IOException|GhostPasswordException e) {
                    Toast.makeText(getActivity(), "Unable to send message", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        FloatingActionButton time_button = (FloatingActionButton) findViewById(R.id.time);
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChatService == null){
                    try {
                        startChat();
                    } catch (GhostPasswordException e) {
                        return;
                    }
                }
                if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                    Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
                    mChatService.connect(mChatService.device,true);
                    return;
                }
                try {
                    mChatService.writeTime();
                } catch (IOException|GhostPasswordException e) {
                    Toast.makeText(getActivity(), "Unable to send time", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        // Loading settings?
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        OTP_keys = (HashSet<String>)settings.getStringSet("otp_keys", null);
        OTP_key_set = new HashSet<Totp>();
        if(OTP_keys != null) {
            Log.i("KeyStore", "Keys from memory");
            Log.i("KeyStore", OTP_keys.toString());
        } else {
            OTP_keys = new HashSet<String>();
        }
        if(OTP_keys.size() == 0){
            OTP_keys.add("Test");
            SharedPreferences.Editor editor = settings.edit();
            editor.putStringSet("otp_keys", OTP_keys);
            editor.apply();
            Log.i("KeyStore","Keys just loaded");
            Log.i("KeyStore",OTP_keys.toString());
        }
        for (String key: OTP_keys){
            Totp op = new Totp(key);
            OTP_key_set.add(op);
            Log.i("OTP:",op.now());
        }

    }

    private void saveKeys(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet("otp_keys", OTP_keys);
        editor.apply();

    }
    final public FragmentActivity getActivity() {
        return this; //mHost == null ? null : (FragmentActivity) mHost.getActivity();
    }
    public void doCaptureQR() {

        //txResult = (TextView) findViewById(R.id.textResult);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra("SCAN_WIDTH", 640);
        integrator.addExtra("SCAN_HEIGHT", 480);
        integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
        //customize the prompt message before scanning
        integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
        integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);

    }

    public void checkPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    doCaptureQR();

                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.content_main),"Permission to use camera not granted",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Log.d("MainActivity", result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
                synchronized (this) {
                    //try {
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
                                // TODO: UNCOMMENT THIS LINE!!!
                                mChatService.writeQR(map.get("secret"));
                                OTP_keys.add(result.getContents());
                                saveKeys();
                            } //TODO: We should save all QR TOTP's so that you can re-send them
                            // Also, we should build TOTP into the app itself, because why not.
                            //TODO: We need to have a way to sync time to the device
                        //TODO: UNCOMMENT } catch (IOException e) {
                        //TODO: UNCOMMENT     e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        } catch (java.io.IOException | GhostPasswordException e) {
                            e.printStackTrace();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }
}
