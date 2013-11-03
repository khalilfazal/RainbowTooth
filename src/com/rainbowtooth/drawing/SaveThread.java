package com.rainbowtooth.drawing;

/**
 * Saves the image in a different thread
 * 
 * @author Khalil Fazal
 */
public class SaveThread extends Thread {

    /**
     * What will save the image
     */
    private final AsyncDrawRainbow drawer;

    /**
     * @param drawer what saves the image
     */
    public SaveThread(final AsyncDrawRainbow drawer) {
        super();
        this.drawer = drawer;
    }

    /**
     * Saves the image
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        this.drawer.saveImage();
    }
}
