package eu.brsolutions.eggtimer;

import androidx.appcompat.app.AppCompatActivity;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

public class MainActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;
    long timeLeft = 0;
    public void startCount(View view){

        final TextView textTimer = findViewById(R.id.textTime);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String timeFormatted = textTimer.getText().toString();
        LocalTime time = LocalTime.parse(timeFormatted);

        Log.i("Time parsed: ", time.toString());


        countDownTimer = startCountDownTimer(textTimer, time);

    }

    public void pauseCount(View view){
        this.countDownTimer.cancel();
    }

    public void resume(View view){
        final TextView textTimer = findViewById(R.id.textTime);
        String timeFormatted = textTimer.getText().toString();
        LocalTime time = LocalTime.parse(timeFormatted);

        this.startCountDownTimer(textTimer, time);
    }

    private CountDownTimer startCountDownTimer(final TextView textTimer, final LocalTime time) {
       return  new CountDownTimer(time.toNanoOfDay()/1000000, 1000){
            //called ervery second
            public void onTick(long millisecondsUntilDone){

                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisecondsUntilDone),
                        TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisecondsUntilDone)),
                        TimeUnit.MILLISECONDS.toSeconds(millisecondsUntilDone) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecondsUntilDone)));

                Log.i("Seconds left: ", hms);
                timeLeft = millisecondsUntilDone;

                textTimer.setText(hms);
            }

            public void onFinish(){
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.whistle);
                mp.start();
                Log.i("Countdown finished ", "Yupy");
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar seekTimeBar = findViewById(R.id.seekTimeBar);
        final TextView textTimer = findViewById(R.id.textTime);

        seekTimeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                String minutes = "0" + i;
                String hours = "00";
                String seconds = "00";
                if( i > 9){
                    minutes = String.valueOf(TimeUnit.MINUTES.toMinutes(i));
                }
                if(minutes.equals("60")){
                    minutes = "00";
                    hours = String.valueOf(Integer.parseInt(hours)+1);
                }


                textTimer.setText(""+String.format("%s:%s:%s",
                        hours,
                        minutes,
                        seconds));

                Log.i("Text Timer: ", textTimer.getText().toString() );

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //each second for 10 seconds
        new CountDownTimer(10000, 1000){
            //called ervery second
            public void onTick(long milisecondsUntilDone){
                Log.i("Seconds left: ", String.valueOf(milisecondsUntilDone/1000));
            }

            public void onFinish(){
                Log.i("Countdown finished ", "Yupy");
            }
        }.start();

        //final Handler handler = new Handler();

//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                Log.i("Timer : ", "A second has passed");
//                handler.postDelayed(this, 1000);
//            }
//        };
//
//        handler.post(run);
    }
}