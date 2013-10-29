package com.rainbowtooth;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

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
     * A set of seen dimensions
     */
    private static Set<String> seen = new HashSet<String>();

    /**
     * Get the filename of the image
     * 
     * @param width the width of the image
     * @param height the height of the image
     * @return the filename
     */
    private static String getFileName(final int width, final int height) {
        return String.format(Locale.CANADA, "%d_%d.webp", width, height);
    }

    /**
     * Where the rainbow is going to be drawn in
     */
    private final View view;

    /**
     * The desired file name
     */
    private String fname;

    /**
     * The bitmap of the created rainbow
     */
    protected Bitmap rainbow;

    /**
     * Whether a new image needs to be made
     */
    private boolean newImage;

    /**
     * Set up the context of the drawing
     * 
     * @param ctx Which context to draw the rainbow
     * @param view Where the rainbow is going to be contained in
     */
    public AsyncDrawRainbow(final Context ctx, final View view) {
        super(ctx);
        this.view = view;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        final int width = this.view.getMeasuredWidth();
        final int height = this.view.getMeasuredHeight();
        this.fname = getFileName(width, height);

        this.execute(width, height);
    }

    /**
     * @see com.rainbowtooth.AbstractAsyncProgress#showProgress()
     */
    @Override
    protected boolean showProgress() {
        this.newImage = seen.contains(this.fname);
        return !this.newImage;
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
        try {
            this.rainbow = BitmapFactory.decodeStream(this.ctx.openFileInput(this.fname));

            if (this.rainbow == null) {
                // The bitmap couldn't be decoded, so create a new one
                throw new FileNotFoundException();
            }
        } catch (final FileNotFoundException e) {
            this.rainbow = this.drawHSLRainbow(width, height);
            seen.add(this.fname);
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
            final float lightness = (float) (verticalPixels - i) / verticalPixels;

            this.publishProgress(i + 1);

            for (int j = 0; j < horizontalPixels; j++) {
                final float hue = j * 360 / horizontalPixels;
                colours[i * horizontalPixels + j] = ColourHSL.HSLToColor(hue, 1, lightness);
            }
        }

        return Bitmap.createBitmap(colours, horizontalPixels, verticalPixels, Bitmap.Config.ARGB_8888);
    }

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

    /**
     * Handle the bitmap
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected abstract void onPostExecute(final Void v);
}
