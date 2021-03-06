package com.github.ghostpassword.ghostpasswordbackend;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by udeyoje on 10/2/15.
 *
 * Good gosh, not thread safe. Call all methods from a synced block.
 */
public class BlueToothDao {

    private OutputStream outputStream;
    private BluetoothSocket socket;

    public  BlueToothDao() throws GhostPasswordException{
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if (bondedDevices.size() > 0) {
                    ArrayList<BluetoothDevice> devices = new ArrayList<>();
                    for (BluetoothDevice device : bondedDevices) {
                        if(device.getName().equals("GhostPassword")) {
                            devices.add(device);
                            System.out.println("I added device: ");
                            System.out.println(device);
                            System.out.println("Notice: Will only work with the first GP device available.");
                        }
                    }
                    if(devices.size() == 0){
                        throw new GhostPasswordException("No paired Ghost Password devices", new Throwable("Bluetooth"));
                    }
                    BluetoothDevice device = devices.get(0);

                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = null;
                    try {
                        socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                        socket.connect();
                        outputStream = socket.getOutputStream();
                    } catch (IOException | ArrayIndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
                        System.out.println("\n--------------------Could not connect to bluetooth device!---------------\n");
                        e.printStackTrace();
                        throw new GhostPasswordException("Unable to connect to bluetooth device", new Throwable("Bluetooth"));
                    }

                } else {
                    Log.e("error", "No appropriate paired devices.");
                    throw new GhostPasswordException("No paired Ghost Password devices", new Throwable("Bluetooth"));
                }
            } else {
                Log.e("error", "Bluetooth is disabled.");
                throw new GhostPasswordException("Bluetooth disabled. Please enable.", new Throwable("Bluetooth"));
            }
        }

    }

    public void write(String s) throws IOException {
        if (outputStream == null) {
            System.out.println("Output stream is null.");
        }
        outputStream.flush();
        s = ':' + s + ':';
        System.out.println("\n\tWriting string to bluetooth: " + s); //TODO: take this out!
        outputStream.write(s.getBytes());
        outputStream.flush();
        try{Thread.sleep(1000);}catch (Exception e){}
    }

    public void writeQR(String s) throws IOException {
        if (outputStream == null) {
            System.out.println("Output stream is null.");
        }
        outputStream.flush();
        s = '-' + s + ':';
        System.out.println("\n\tWriting string to bluetooth: " + s); //TODO: take this out!
        outputStream.write(s.getBytes());
        outputStream.flush();
        try{Thread.sleep(1000);}catch (Exception e){}
    }

    public void close() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
