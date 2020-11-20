package com.example.rabitturtle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int rab=0,tur=0;
    private SeekBar seekBar,seekBar2;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        seekBar2 = findViewById(R.id.seekBar2);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);

                rab=0;
                tur=0;
                seekBar.setProgress(0);
                seekBar2.setProgress(0);
                runThread();
                runAsyncTask();
            }
        });
    }

    private void runThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (rab<=100 && tur<=100){
                    try {
                        Thread.sleep(100);
                        rab += (int)(Math.random()*3);
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void runAsyncTask(){

        new AsyncTask<Void,Integer,Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids){
                while (tur <= 100 && rab < 100){
                    try {
                        Thread.sleep(100);
                        tur += (int)(Math.random()*3);
                        publishProgress(tur);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                return true;
            }

            @Override
            protected void onProgressUpdate(Integer... values){
                super.onProgressUpdate(values);
                seekBar2.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean){
                super.onPostExecute(aBoolean);
                if (tur>=100 && rab<100){
                    Toast.makeText(MainActivity.this,"烏龜勝利",Toast.LENGTH_SHORT).show();
                    button.setEnabled(true);
                }
            }
        }.execute();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    seekBar.setProgress(rab);
                    break;
            }
            if (rab>=100 && tur<100){
                Toast.makeText(MainActivity.this, "兔子勝利", Toast.LENGTH_SHORT).show();
                button.setEnabled(true);
            }
            return false;
        }
    });


}