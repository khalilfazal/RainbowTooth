package com.rainbowtooth.drawing;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.rainbowtooth.R;
import com.rainbowtooth.bluetooth.Streamer;

/**
 * Event triggered when the rainbow is touched
 * 
 * @author Khalil Fazal
 */
public class RainbowTouch implements OnTouchListener, Streamer {

    /**
     * Where to show the selected colour
     */
    private final TextView messageBox;

    /**
     * A (Point -> Colour) map
     */
    private final Bitmap rainbow;

    /**
     * The string's format
     */
    private final String format;

    /**
     * The boarder's painter
     */
    private final Paint paint;

    /**
     * Used to send the selected colour to
     */
    private OutputStream socketStream;

    /**
     * @param message Where to display the colour of the touched pixel
     * @param rainbow The bitmap used to find colors of pixels    
     * @param paint the boarder's painter
     */
    public RainbowTouch(final TextView message, final Bitmap rainbow, final Paint paint) {
        this.messageBox = message;
        this.rainbow = rainbow;
        this.format = message.getContext().getResources().getText(R.string.format).toString();
        this.paint = paint;
    }

    /**
     * @see com.rainbowtooth.bluetooth.Streamer#setStream(java.io.OutputStream)
     */
    @Override
    public void setStream(final OutputStream stream) {
        this.socketStream = stream;
    }

    /** 
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        try {
            final int pixel = this.rainbow.getPixel((int) event.getX(), (int) event.getY());

            // Set the border's colour
            this.paint.setColor(pixel);

            // Redraw the border
            this.messageBox.invalidate();

            // Send the colour to the device
            if (this.socketStream != null) {
                try {
                    final ByteBuffer buffer = ByteBuffer.allocate(4);
                    buffer.putInt(pixel);
                    this.socketStream.write(buffer.array());
                } catch (final IOException e) {}
            }

            final int red = Color.red(pixel);
            final int green = Color.green(pixel);
            final int blue = Color.blue(pixel);

            final SpannableString text = new SpannableString(String.format(Locale.CANADA, this.format, red, green, blue));

            text.setSpan(new ForegroundColorSpan(Color.rgb(red, 0, 0)), 0, 3, 0);
            text.setSpan(new ForegroundColorSpan(Color.rgb(0, green, 0)), 4, 7, 0);
            text.setSpan(new ForegroundColorSpan(Color.rgb(0, 0, blue)), 8, 11, 0);

            this.messageBox.setText(text, BufferType.SPANNABLE);
        } catch (final IllegalArgumentException e) {
            //Log.e(this.getClass().toString(), "exception", e);
            return false;
        }

        return true;
    }
}
