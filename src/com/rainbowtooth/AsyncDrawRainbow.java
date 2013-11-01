package com.rainbowtooth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

/**
 * Asynchronously draw a rainbow
 * 
 * @author Khalil Fazal
 */
public abstract class AsyncDrawRainbow extends AbstractAsyncProgress<Integer, Void> implements Runnable {

    /**
     * Where the rainbow is going to be drawn in
     */
    private final View view;

    /**
     * The bitmap of the created rainbow
     */
    protected Bitmap rainbow;

    /**
     * The desired file name
     */
    private String fname;

    /**
     * Used if a new image needs to be created
     */
    private FileInputStream file;

    /**
     * True if we need to make a new file
     */
    private boolean newImage;

    /**
     * Set up the context of the drawing
     * 
     * @param view Where the rainbow is going to be contained in
     * @param progress Shows the progress of the task
     */
    public AsyncDrawRainbow(final View view, final ProgressDialog progress) {
        super(view.getContext(), progress);
        this.view = view;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        final int width = this.view.getMeasuredWidth();
        final int height = this.view.getMeasuredHeight();

        // Set the desired file name
        this.fname = String.format(Locale.CANADA, "%d_%d.webp", width, height);

        // Check to see if the rainbow image file already exists
        this.newImage = !this.fileExists();

        this.execute(width, height);
    }

    /**
     * @return if the file exists
     */
    private boolean fileExists() {
        try {
            this.file = this.ctx.openFileInput(this.fname);
        } catch (final FileNotFoundException e) {
            return false;
        }

        return true;
    }

    /**
     * @see com.rainbowtooth.AbstractAsyncProgress#showProgress()
     */
    @Override
    protected boolean showProgress() {
        return this.newImage;
    }

    /**
     * Start the drawing
     * 
     * @see android.os.AsyncTask#doInBackground(Integer[])
     */
    @Override
    protected Void doInBackground(final Integer... params) {
        this.progressBar.setMax(params[1]);
        this.getRainbow(params[0], params[1]);

        // Save the rainbow
        new SaveThread(this).start();

        return null;
    }

    /**
    * Create or get the rainbow from a file
    * Filename format: "width_height.webp"
    * 
    * @param width the width of the rainbow
    * @param height the height of the rainbow
    */
    private void getRainbow(final int width, final int height) {
        if (this.newImage) {
            this.rainbow = this.drawHSLRainbow(width, height);
        } else {
            final BitmapFactory.Options attributes = new BitmapFactory.Options();
            this.rainbow = BitmapFactory.decodeStream(this.file, null, attributes);

            if (attributes.outWidth != width || attributes.outHeight != height) {
                this.rainbow = this.drawHSLRainbow(width, height);
                this.newImage = true;
            }
        }
    }

    /**
     * Draw a rainbow using the Hue, Saturation and Lightness colour scheme.
     * 
     * hue = [0 .. 2pi], left to right
     * saturation = 1
     * lightness = [1 .. 0], top to bottom
     * 
     * @param horizontalPixels number of horizontal pixels
     * @param verticalPixels number of vertical pixels
     * @return the colours
     */
    private Bitmap drawHSLRainbow(final int horizontalPixels, final int verticalPixels) {
        final int[] colours = new int[horizontalPixels * verticalPixels];

        for (int i = 0; i < verticalPixels; i++) {
            final float lightness = i < 30 ? 1 : (float) (verticalPixels - i) / verticalPixels;

            this.publishProgress(i + 1);

            for (int j = 0; j < horizontalPixels; j++) {
                final float hue = j * 360 / horizontalPixels;
                colours[i * horizontalPixels + j] = ColourHSL.HSLToColor(hue, 1, lightness);
            }
        }

        return Bitmap.createBitmap(colours, horizontalPixels, verticalPixels, Bitmap.Config.ARGB_8888);
    }

    /**
     * Handle the bitmap
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected abstract void onPostExecute(final Void v);

    /**
     * Save the bitmap to a file
     */
    protected void saveImage() {
        if (this.newImage) {
            try {
                this.rainbow.compress(Bitmap.CompressFormat.WEBP, 100, this.ctx.openFileOutput(this.fname, Context.MODE_PRIVATE));
            } catch (final FileNotFoundException message) {
                Log.e(this.getName(), "exception", message);
            }
        }
    }
}
