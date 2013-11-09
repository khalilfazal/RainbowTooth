package com.rainbowtooth.drawing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.rainbowtooth.R;

/** 
 * Asynchronously perform a task with a progress bar
 * 
 * @author Khalil Fazal
 * @param <Params> The type of arguments sent to the task on execution
 * @param <Result> The type of the result
 */
public abstract class AbstractAsyncProgress<Params, Result> extends AsyncTask<Params, Integer, Result> {

    /**
     * Where the task is being done
     */
    protected final Context ctx;

    /**
     * Shows the progress of the task
     */
    protected final ProgressDialog progressBar;

    /**
     * From where strings can be found
     */
    private final Resources resource;

    /**
     * Set up the context
     * 
     * @param ctx The context for the drawing
     * @param progress shows the progress of the task
     */
    public AbstractAsyncProgress(final Context ctx, final ProgressDialog progress) {
        this.ctx = ctx;
        this.resource = ctx.getResources();
        this.progressBar = progress;
        this.progressBar.setProgressNumberFormat(this.getText(R.string.blank));
    }

    /**
     * Get a string by id
     * 
     * @param id the string's id
     * @return the string
     */
    protected String getText(final int id) {
        return this.resource.getText(id).toString();
    }

    /**
     * @return The name of the Application
     */
    protected String getName() {
        return this.getText(R.string.app_name);
    }

    /**
     * @return Whether to show the progress bar
     */
    protected abstract boolean showProgress();

    /**
     * Show the progress bar
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        if (this.showProgress()) {
            this.progressBar.show();
        }
    }

    /**
     * Start the drawing
     * 
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    @Override
    protected abstract Result doInBackground(final Params... params);

    /**
     * Update the progress bar
     * 
     * @see android.os.AsyncTask#onProgressUpdate(java.lang.Integer[])
     */
    @Override
    protected void onProgressUpdate(final Integer... progress) {
        this.progressBar.setProgress(progress[0]);
    }

    /**
     * Handle the bitmap
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected abstract void onPostExecute(final Result rainbow);
}
