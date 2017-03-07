package com.example.swarna.fitnessapp;

/**
 * Created by swarnav on 4/12/16.
 */
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
/**
 * Created by swarnav on 3/12/16.
 */
public class DownloadService extends Service{
    int counter = 0;
    public URL[] urls;

    static final int UPDATE_INTERVAL = 1000;
    private Timer timer = new Timer();

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        //return null;
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        System.out.println("intent values:"+intent.getExtras());


        Object[] objUrls = (Object[]) intent.getExtras().get("URLs");
        URL[] urls = new URL[objUrls.length];
        for (int i=0; i<objUrls.length; i++) {
            urls[i] = (URL) objUrls[i];
            System.out.println("url received in intent:"+urls[i].toString());
        }
        new DoBackgroundTask().execute(urls);

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private int DownloadFile(URL url,int fileno) {

        try {
            //---taking some time to download a file---
            Thread.sleep(2000);

            //create the new connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            connection.setRequestMethod("GET");



            connection.connect();


            File SDCardRoot = Environment.getExternalStorageDirectory();

            String urlname = url.toString();
            String fileName = urlname.substring( urlname. lastIndexOf('/')+1, urlname.length());
            File file = new File(SDCardRoot,fileName);

            FileOutputStream fileOutput = new FileOutputStream(file);

            InputStream input = connection.getInputStream();


            int totalSize = connection.getContentLength();
            int downloadedSize = 0;

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //read through the input buffer and write the contents to the file
            while ( (bufferLength = input.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            //close the output stream when done
            fileOutput.close();


        }catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return 100;
    }

    private class DoBackgroundTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalBytesDownloaded = 0;
            for (int i = 0; i < count; i++) {
                totalBytesDownloaded += DownloadFile(urls[i],i+1);
                publishProgress(i+1);
            }
            return totalBytesDownloaded;
        }

        protected void onProgressUpdate(Integer... progress) {
            String name;
            Log.d("Downloading files",
                    String.valueOf(progress[0]) + " downloaded");
             if(progress[0] == 1 )
                name = "WalkingPlan pdf";
            else
                name = "DietPlan pdf";

            Toast.makeText(getBaseContext(),
                    name + " downloaded!",
                    Toast.LENGTH_LONG).show();
        }

        protected void onPostExecute(Long result) {
            Toast.makeText(getBaseContext(),
                    "Downloaded " +"workout and diet plan" + " pdfs",
                    Toast.LENGTH_LONG).show();
            stopSelf();
        }
    }
}
