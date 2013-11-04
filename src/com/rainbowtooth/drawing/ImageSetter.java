package com.rainbowtooth.drawing;

import android.graphics.Bitmap;

/**
 * Set's up an image after it has been created.
 * Must use a progress bar to create the image.
 * 
 * @author Khalil Fazal
 */
public interface ImageSetter {
    /**
     * Sets up the image into the view
     * 
     * @param image the image
     */
    public void setImage(final Bitmap image);

    /**
     * @return if the progress bar is null
     */
    public boolean nullProgressBar();
}