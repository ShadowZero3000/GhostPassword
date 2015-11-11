package com.github.ghostpassword.ghostpasswordbackend;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
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
    private BluetoothDevice device;
    private boolean connected;

    public  BlueToothDao() throws GhostPasswordException{
        connected = false;
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

        /*synchronized (blueAdapter) {
            if (blueAdapter.isDiscovering()) {
                blueAdapter.cancelDiscovery();
            }
        }*/
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if (bondedDevices.size() > 0) {
                    ArrayList<BluetoothDevice> devices = new ArrayList<>();
                    for (BluetoothDevice thisDevice : bondedDevices) {
                        if(thisDevice.getName().equals("GhostPassword")) {
                            devices.add(thisDevice);
                            System.out.println("I added device: ");
                            System.out.println(thisDevice);
                            System.out.println("Notice: Will only work with the first GP device available.");
                        }
                    }
                    if(devices.size() == 0){
                        throw new GhostPasswordException("No paired Ghost Password devices", new Throwable("Bluetooth"));
                    }
                    device = devices.get(0);

                    ParcelUuid[] uuids = device.getUuids();
                    try {
                        System.out.println("Creating socket: " + uuids[0].getUuid());
                        connectSocket();
                        //socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                        connected = true;
                        System.out.println("Socket created ");
                        //socket.connect();
                   } catch (ArrayIndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
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
        } else {
            Log.e("error", "Bluetooth is not available.");
            throw new GhostPasswordException("Bluetooth not available on this device.", new Throwable("Bluetooth"));
        }
    }

    private void connectSocket() throws GhostPasswordException{
        try {
            try {
                Method m=device.getClass().getMethod("createRfcommSocket", int.class);
                socket=(BluetoothSocket)m.invoke(device,1);
            }
            catch ( NoSuchMethodException e) {
                socket=device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            }
            socket.connect();
        }
        catch ( Exception e) {
            Log.e("error", "Couldn't connect to device.");
            socket=null;
            throw new GhostPasswordException("Unable to connect to bluetooth device", new Throwable("Bluetooth"));
        }
    }
    public void write(String s) throws IOException {
        if(!connected){
            connectSocket();
        }
        outputStream = socket.getOutputStream();
        if (outputStream == null) {
            System.out.println("Output stream is null.");
        }
        //outputStream.flush();
        s = ':' + s + ':';
        System.out.println("\n\tWriting string to bluetooth: " + s); //TODO: take this out!
        outputStream.write(s.getBytes());
        outputStream.flush();
        //try{Thread.sleep(1000);}catch (Exception e){}
    }

    public void writeQR(String s) throws IOException {
        if(!connected){
            connectSocket();
        }
        outputStream = socket.getOutputStream();
        if (outputStream == null) {
            System.out.println("Output stream is null.");
        }
        s = '-' + s + ':';
        System.out.println("\n\tWriting string to bluetooth: " + s); //TODO: take this out!
        outputStream.write(s.getBytes());
        outputStream.flush();
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
