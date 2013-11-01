package com.rainbowtooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.View;
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
     * Shows the progress of the task
     */
    protected ProgressDialog progressBar;

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

        // Create the progress bar
        this.progressBar = new ProgressDialog(this);
        this.progressBar.setTitle(R.string.progressTitle);
        this.progressBar.setIndeterminate(false);
        this.progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // Run after views get measured
        this.rainbowView.post(new AsyncDrawRainbow(this.rainbowView, this.progressBar) {
            @Override
            protected void onPostExecute(final Void v) {
                if (RainbowActivity.this.progressBar != null) {
                    // Set up the UI
                    RainbowActivity.this.setRainbow(this.rainbow);
                }
            }
        });
    }

    /**
     * Dismiss the progress bar on rotate
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (this.progressBar != null) {
            this.progressBar.dismiss();
        }

        this.progressBar = null;
    }

    /**
     * Called after the rainbow is created.
     * Insert the rainbow into the {@link ImageView} and set the {@link OnTouchListener}.
     * 
     * @param rainbow The rainbow that needs to be setup
     */
    protected void setRainbow(final Bitmap rainbow) {
        // Dismiss the progress bar
        this.progressBar.dismiss();

        // Set the rainbow image
        this.rainbowView.setImageBitmap(rainbow);

        // Get the TextView for where the touched colour should appear
        final TextView message = (TextView) this.findViewById(R.id.message);

        // Create a new background
        final ShapeDrawable background = new ShapeDrawable();

        // Initialize the paint
        final Paint paint = background.getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(0x00000000);

        // Set the empty view's background
        final View border = this.findViewById(R.id.border);
        border.setBackground(background);

        // Set the rainbow touch listener
        this.rainbowView.setOnTouchListener(new RainbowTouch(message, rainbow, border, paint));
    }
}