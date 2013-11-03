package com.rainbowtooth.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author Khalil Fazal
 */
public class ConnectTask extends AsyncTask<BluetoothDevice, String, Void> {

    /**
     * UUID to use the SDP protocol
     */
    private static final UUID sdpUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * Where the thread will post messages in the UI
     */
    private final Context ctx;

    /**
     * Where the data to the bluetooth device will be sent
     */
    private final Streamer streamer;

    /**
     * @param ctx Where the thread will post messages in the UI
     * @param streamer Where the data to the bluetooth device will be sent
     */
    public ConnectTask(final Context ctx, final Streamer streamer) {
        this.ctx = ctx;
        this.streamer = streamer;
    }

    /**
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    @SuppressWarnings("resource")
    @Override
    protected Void doInBackground(final BluetoothDevice... params) {
        try {
            final BluetoothSocket socket = params[0].createRfcommSocketToServiceRecord(sdpUUID);
            socket.connect();
            this.streamer.setStream(socket.getOutputStream());
        } catch (final IOException e) {
            this.publishProgress(e.toString());
        }

        return null;
    }

    /**
     * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
     */
    @Override
    protected void onProgressUpdate(final String... progress) {
        Toast.makeText(this.ctx, progress[0], Toast.LENGTH_SHORT).show();
    }
}
