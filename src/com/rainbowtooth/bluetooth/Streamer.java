package com.rainbowtooth.bluetooth;

import java.io.OutputStream;

/**
 * Able to send data to an output stream
 * 
 * @author Khalil Fazal
 */
public interface Streamer {

    /**
     * Set the output stream to the bluetooth device
     * 
     * @param stream the output stream
     */
    void setStream(OutputStream stream);

}