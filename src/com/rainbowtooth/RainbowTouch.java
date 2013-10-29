package com.rainbowtooth;

import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * Event triggered when the rainbow is touched
 * 
 * @author Khalil Fazal
 */
public class RainbowTouch implements OnTouchListener {

    /**
     * The string's format
     */
    private final String format;

    /**
     * A (Point -> Colour) map
     */
    private final Bitmap rainbow;

    /**
     * Where to show the selected colour
     */
    private final TextView message;

    /**
     * @param message Where to display the colour of the touched pixel
     * @param rainbow the bitmap used to find colors of pixels
     */
    public RainbowTouch(final TextView message, final Bitmap rainbow) {
        this.message = message;
        this.rainbow = rainbow;
        this.format = message.getContext().getResources().getText(R.string.format).toString();
    }

    /** 
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        try {
            final int pixel = this.rainbow.getPixel((int) event.getX(), (int) event.getY());

            final int red = Color.red(pixel);
            final int green = Color.green(pixel);
            final int blue = Color.blue(pixel);

            final SpannableString text = new SpannableString(String.format(Locale.CANADA, this.format, red, green, blue));

            text.setSpan(new ForegroundColorSpan(Color.rgb(red, 0, 0)), 0, 3, 0);
            text.setSpan(new ForegroundColorSpan(Color.rgb(0, green, 0)), 4, 7, 0);
            text.setSpan(new ForegroundColorSpan(Color.rgb(0, 0, blue)), 8, 11, 0);

            this.message.setText(text, BufferType.SPANNABLE);
        } catch (final IllegalArgumentException e) {
            //Log.e(this.getClass().toString(), "exception", e);
            return false;
        }

        return true;
    }
}
