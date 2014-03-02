package com.example.app;

import android.os.AsyncTask;

/**
 * Created by Alexi on 01/03/14.
 */
public class BackgroundUpdater extends AsyncTask<Void, Void, Void> {

    private TimerListener tl;

    protected Void doInBackground(Void... voids) {
        try { Thread.sleep(5000); }
        catch(InterruptedException e) { System.err.println(e.getMessage()); }
        return null;
    }

    public void init(TimerListener TL)
    {
        tl = TL;
        this.execute();
    }

    protected void onPostExecute(Void ha) {
        tl.timerComplete();
        tl = null;          //poor tl, you will all be missed
    }
}
