package com.rainbowtooth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

/** 
 * Asynchronously perform a task with a progress bar
 * 
 * @author Khalil Fazal
 * @param <Result> The type of the result
 */
public abstract class AbstractAsyncProgress<Result> extends AsyncTask<Integer, Integer, Result> {

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
     */
    public AbstractAsyncProgress(final Context ctx) {
        this.ctx = ctx;
        this.resource = ctx.getResources();
        this.progressBar = new ProgressDialog(this.ctx);
        this.progressBar.setTitle(R.string.progressTitle);
        this.progressBar.setIndeterminate(false);
        this.progressBar.setProgressNumberFormat(this.getText(R.string.empty));
        this.progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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
     * Show the progress bar
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        this.progressBar.show();
    }

    /**
     * Update the progress bar
     * 
     * @see android.os.AsyncTask#onProgressUpdate(java.lang.Integer[])
     */
    @Override
    protected void onProgressUpdate(final Integer... progress) {
        this.progressBar.setProgress(progress[0]);
    }
}
