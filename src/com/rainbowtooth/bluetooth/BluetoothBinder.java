package com.rainbowtooth.bluetooth;

/**
 * Creates a bluetooth socket from a bluetooth device's MAC address
 * 
 * @author Khalil Fazal
 */
public interface BluetoothBinder {

    /**
     * @param address the bluetooth address to bind to
     */
    void bind(String address);
}