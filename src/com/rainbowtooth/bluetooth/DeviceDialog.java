package com.rainbowtooth.bluetooth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.rainbowtooth.R;

/**
 * Shows a dialog where the user chooses a bluetooth device.
 * 
 * @author Khalil Fazal
 */
public class DeviceDialog extends DialogFragment {

    /**
     * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Bundle bundle = this.getArguments();
        final AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity()).setTitle(R.string.chooserTitle);

        if (bundle.getBoolean("empty")) {
            alert.setItems(R.array.noDevices, null);
        } else {
            final SimpleMap devices = (SimpleMap) bundle.getParcelable("devices");

            alert.setItems(devices.getKeys(), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    final BluetoothBinder binder = (BluetoothBinder) DeviceDialog.this.getActivity();
                    binder.bind(devices.valueAt(which));
                }
            });
        }

        return alert.show();
    }
}