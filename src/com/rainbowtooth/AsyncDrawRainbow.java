package com.rainbowtooth;

import java.io.FileNotFoundException;
import java.util.Locale;

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
public abstract class AsyncDrawRainbow extends AbstractAsyncProgress<Bitmap> implements Runnable {

    /**
     * Where the rainbow is going to be drawn in
     */
    private final View view;

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
        this.execute(this.view.getMeasuredWidth(), this.view.getMeasuredHeight());
    }

    /**
     * Start the drawing
     * 
     * @see android.os.AsyncTask#doInBackground(Integer[])
     */
    @Override
    protected Bitmap doInBackground(final Integer... params) {
        this.progressBar.setMax(params[1]);
        return this.getRainbow(params[0], params[1]);
    }

    /**
    * Create or get the rainbow from a file
    * Filename format: "width_height.webp"
    * 
    * @param width the width of the rainbow
    * @param height the height of the rainbow
    * @return the rainbow as a bitmap
    */
    private Bitmap getRainbow(final int width, final int height) {
        final String fname = String.format(Locale.CANADA, "%d_%d.webp", width, height);
        Bitmap rainbow = null;

        try {
            rainbow = BitmapFactory.decodeStream(this.ctx.openFileInput(fname));

            if (rainbow == null) {
                // The bitmap couldn't be decoded, so create a new one
                throw new FileNotFoundException();
            }
        } catch (final FileNotFoundException e) {
            rainbow = Bitmap.createBitmap(this.drawHSLRainbow(width, height), width, height, Bitmap.Config.ARGB_8888);

            try {
                rainbow.compress(Bitmap.CompressFormat.WEBP, 100, this.ctx.openFileOutput(fname, Context.MODE_PRIVATE));
            } catch (final FileNotFoundException message) {
                Log.e(this.getName(), "exception", message);
            }
        }

        return rainbow;
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
    private int[] drawHSLRainbow(final int horizontalPixels, final int verticalPixels) {
        final int[] colours = new int[horizontalPixels * verticalPixels];

        for (int i = 0; i < verticalPixels; i++) {
            final float lightness = (float) (verticalPixels - i) / verticalPixels;

            this.publishProgress(i + 1);

            for (int j = 0; j < horizontalPixels; j++) {
                final float hue = j * 360 / horizontalPixels;
                colours[i * horizontalPixels + j] = ColourHSL.HSLToColor(hue, 1, lightness);
            }
        }

        return colours;
    }

    /**
     * Handle the bitmap
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected abstract void onPostExecute(final Bitmap rainbow);
}
