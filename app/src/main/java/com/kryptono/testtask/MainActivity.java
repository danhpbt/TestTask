package com.kryptono.testtask;

import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import static android.os.AsyncTask.SERIAL_EXECUTOR;
import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class MainActivity extends AppCompatActivity {

    static ForegroundBackgroundListener instance = null;

    final static int INTENT_AUTHENTICATE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Secure don't return thumbnail recent
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Task task1 = new Task();
                Task task2 = new Task();
                Task task3 = new Task();


                task1.executeOnExecutor(SERIAL_EXECUTOR,"TaskOne ");
                task2.executeOnExecutor(SERIAL_EXECUTOR, "TaskTwo ");
                task3.executeOnExecutor(SERIAL_EXECUTOR, "TaskTwo ");


                Log.d("Task: ", "AAAAA");
                Log.d("Task: ", "BBBBB");
                Log.d("Task: ", "CCCCC");
                Log.d("Task: ", "DDDDD");
                Log.d("Task: ", "EEEEE");


            }
        });

        if (instance == null)
            instance = new ForegroundBackgroundListener(this);

        getLifecycle().addObserver(instance);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_AUTHENTICATE) {
            if (resultCode == RESULT_OK) {
                //do something you want when pass the security
                MainApplication.isForeground = true;
                instance.bKeyGuardOpen = false;
            }
            else {
                instance.lockScreen();
//                MainApplication.isForeground = false;
//                instance.bKeyGuardOpen = false;
            }

        }
    }

    public static void waitInMilisecond(int miliseconds)
    {
        SystemClock.sleep(miliseconds);
    }


    private static class Task extends AsyncTask<String, Integer, Void> {



        private Task() {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... unused) {

            for(int i = 0; i < 10; i++)
            {
                Log.d("Task: ", unused[0] + i);
                waitInMilisecond(1000);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void unused) {

        }


    }

}
