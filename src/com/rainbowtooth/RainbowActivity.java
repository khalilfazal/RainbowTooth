package com.rainbowtooth;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Shows a rainbow of the HSL spectrum
 * 
 * @author Khalil Fazal
 */
public class RainbowActivity extends Activity {

    /** 
     * The rainbow
     */
    private ImageView rainbowView;

    /**
     * Create the Rainbow Activity
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout
        this.setContentView(R.layout.activity_rainbow);

        // Get the image view
        this.rainbowView = (ImageView) this.findViewById(R.id.rainbow);

        // Run after views get measured
        this.rainbowView.post(new AsyncDrawRainbow(this, this.rainbowView) {
            @Override
            protected void onPostExecute(final Void v) {

                // Dismiss the progress bar
                this.progressBar.dismiss();

                // Set up the UI
                RainbowActivity.this.setRainbow(this.rainbow);

                // Save the rainbow
                new SaveThread(this).start();
            }
        });
    }

    /**
     * Called after the rainbow is created.
     * Insert the rainbow into the {@link ImageView} and set the {@link OnTouchListener}.
     * 
     * @param rainbow The rainbow that needs to be setup
     */
    protected void setRainbow(final Bitmap rainbow) {
        // Set the rainbow image
        this.rainbowView.setImageBitmap(rainbow);

        // Get the TextView for where the touched colour should appear
        final TextView message = (TextView) this.findViewById(R.id.message);

        // Set the rainbow touch listener
        this.rainbowView.setOnTouchListener(new RainbowTouch(message, rainbow));
    }
}