package com.rainbowtooth;

import java.util.Set;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbowtooth.bluetooth.BluetoothBinder;
import com.rainbowtooth.bluetooth.ConnectTask;
import com.rainbowtooth.bluetooth.DeviceDialog;
import com.rainbowtooth.bluetooth.SimpleMap;
import com.rainbowtooth.drawing.AsyncDrawRainbow;
import com.rainbowtooth.drawing.ImageSetter;
import com.rainbowtooth.drawing.RainbowTouch;

/**
 * Shows a rainbow of the HSL spectrum
 * 
 * @author Khalil Fazal
 */
public class RainbowActivity extends ActionBarActivity implements ImageSetter, BluetoothBinder {

    /**
     * Request code to enable Bluetooth.
     */
    private static final int RQ_ENABLE_BLUETOOTH = 0;

    /**
     * The rainbow
     */
    private ImageView rainbowView;

    /**
     * Shows the progress of the task
     */
    protected ProgressDialog progressBar;

    /**
     * The default Bluetooth Adapter.
     */
    private final BluetoothAdapter adapter;

    /**
     * The rainbow that will be shown to the user
     */
    protected Bitmap rainbow;

    /**
     * Listens for touch events on the rainbow
     */
    private RainbowTouch touchListener;

    /**
     * Construct the activity
     */
    public RainbowActivity() {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
    }

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
        this.rainbowView.post(new AsyncDrawRainbow(this.rainbowView, this.progressBar));
    }

    /**
     * @see com.rainbowtooth.drawing.ImageSetter#nullProgressBar()
     */
    @Override
    public boolean nullProgressBar() {
        return this.progressBar == null;
    }

    /**
     * Called after the rainbow is created.
     * Insert the rainbow into the {@link ImageView} and set the {@link OnTouchListener}.
     * 
     * @see com.rainbowtooth.drawing.ImageSetter#setImage(android.graphics.Bitmap)
     */
    @Override
    public void setImage(final Bitmap rainbow) {
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
        paint.setStrokeWidth(5);
        paint.setColor(0x00000000);

        // Set the empty view's background
        final View border = this.findViewById(R.id.border);
        border.setBackground(background);

        // Set the rainbow touch listener
        this.touchListener = new RainbowTouch(message, rainbow, border, paint);
        this.rainbowView.setOnTouchListener(this.touchListener);
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
     * Inflate the bluetooth menu
     * Don't inflate if there is no blueooth adapter
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (this.adapter != null) {
            this.getMenuInflater().inflate(R.menu.bluetooth, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Action on menu selection
     * 
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect:
                if (!this.adapter.isEnabled()) {
                    final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    this.startActivityForResult(intent, RQ_ENABLE_BLUETOOTH);
                } else {
                    this.showDeviceDialog();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when an {@link Intent} returns to the Application.
     * 
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(final int request, final int result, final Intent data) {
        if (result == RESULT_OK && request == RQ_ENABLE_BLUETOOTH) {
            this.showDeviceDialog();
        }
    }

    /**
     * When called, assume that Bluetooth is turned on.
     * 
     * Shows a dialog where the user chooses a bluetooth device.
     */
    private void showDeviceDialog() {
        final DeviceDialog dialog = new DeviceDialog();
        final Bundle bundle = new Bundle();
        final Set<BluetoothDevice> bondedDevices = this.adapter.getBondedDevices();

        if (bondedDevices.isEmpty()) {
            bundle.putBoolean("empty", true);
        } else {
            final SimpleMap deviceMap = new SimpleMap(bondedDevices.size());

            for (final BluetoothDevice device : bondedDevices) {
                deviceMap.put(device.getName(), device.getAddress());
            }

            bundle.putBoolean("empty", false);
            bundle.putParcelable("devices", deviceMap);
        }

        dialog.setArguments(bundle);
        dialog.show(this.getFragmentManager(), "deviceDialog");
    }

    /**
     * @see com.rainbowtooth.bluetooth.BluetoothBinder#bind(java.lang.String)
     */
    @Override
    public void bind(final String address) {
        Log.i("Rainbowtooth", address);
        final BluetoothDevice device = this.adapter.getRemoteDevice(address);
        new ConnectTask(this, this.touchListener).execute(device);
    }
}