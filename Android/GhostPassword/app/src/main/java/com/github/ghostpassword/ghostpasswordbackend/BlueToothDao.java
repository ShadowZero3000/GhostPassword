package com.github.ghostpassword.ghostpasswordbackend;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by udeyoje on 10/2/15.
 */
public class BlueToothDao {

    private OutputStream outputStream;
    private BluetoothSocket socket;

    public  BlueToothDao() {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if (bondedDevices.size() > 0) {
                    ArrayList<BluetoothDevice> devices = new ArrayList<>();
                    for (BluetoothDevice device : bondedDevices) {
                        devices.add(device);
                    }
                    BluetoothDevice device = devices.get(0);
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = null;
                    try {
                        //socket = device.createInsecureRfcommSocketToServiceRecord(uuids[0].getUuid());
                        //socket.
                        socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                        socket.connect();
                        outputStream = socket.getOutputStream();
                    } catch (IOException | ArrayIndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
                        e.printStackTrace();
                        System.out.println("\n--------------------Could not connect to bluetooth device!---------------\n");
                        //TODO: error handling here -- display a message or something
                    }

                } else {
                    Log.e("error", "No appropriate paired devices.");
                }
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }

    }

    public void write(String s) throws IOException {
        if (outputStream == null) {
            System.out.println("Output stream is null.");
        }
        System.out.println("\n\tWriting string to bluetooth: " + s); //TODO: take this out!
        outputStream.write(s.getBytes());
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
