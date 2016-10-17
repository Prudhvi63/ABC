package com.rogues.abc;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    BgmMusic bgmMusic;
    ImageView imageView;
    public static final int SWIPE_DELAY = 1000;
    int [] list = {R.drawable.acorn,R.drawable.banana,R.drawable.chicken,R.drawable.duck,R.drawable.eggs,
            R.drawable.fish,R.drawable.gold,R.drawable.hat,R.drawable.ice_cream,R.drawable.jeans,
            R.drawable.kiwi,R.drawable.lemon,R.drawable.mango,R.drawable.drum,R.drawable.orange,
            R.drawable.pig,R.drawable.drum,R.drawable.rabbit,R.drawable.sheep,R.drawable.tiger,
            R.drawable.umbrella,R.drawable.violin,R.drawable.wolf,R.drawable.drum,R.drawable.yogurt,
            R.drawable.zebra};
    int [] capsList = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,
                       R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,
                       R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,
                       R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s,R.drawable.t,
                       R.drawable.u,R.drawable.v,R.drawable.w,R.drawable.x,R.drawable.y,
                       R.drawable.z};
    int index =0;
    float intialX = Float.MIN_VALUE;
    float intialY = Float.MIN_VALUE;
    float finalX = Float.MIN_VALUE;
    float finalY = Float.MIN_VALUE;
    Long lastSwipe = System.currentTimeMillis();


    public class BgmMusic extends AsyncTask<Context,Void,Void>{

        @Override
        protected Void doInBackground(Context... params) {
            mediaPlayer =  MediaPlayer.create(params[0], R.raw.bgm);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1.0f,1.0f);
            mediaPlayer.start();

            return null;
        }
        protected void done(){
            Log.i(" Task completed","#####################################################");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        bgmMusic = new BgmMusic();
        index=0;
        imageView = (ImageView) findViewById(R.id.imageView);

    }

   public void onPause(){
       super.onPause();
       Log.i(" On pause","-------------------------------------------------------------------------------");
       bgmMusic.cancel(true);
       mediaPlayer.release();
       mediaPlayer = null;
    }

    public void onStop(){
        super.onStop();
        Log.i(" On Stop","-------------------------------------------------------------------------------");

    }

    public void onRestart(){
        super.onRestart();
        Log.i(" On Restart","-------------------------------------------------------------------------------");

    }
    public void onResume() {
        super.onResume();
        Log.i("On Resume","--------------------------------------------------------------------------------");
        if(bgmMusic.getStatus() != AsyncTask.Status.RUNNING) {
            Log.i(" BGM status is ",bgmMusic.getStatus() +"_______________________________________________________________________________");
            bgmMusic = new BgmMusic();
            bgmMusic.execute(this);
        }

    }

    boolean isCaps = true;
    public void imageTapped(View view){
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        if(isCaps)
            imageView.setImageResource(list[index]);
        else
            imageView.setImageResource(capsList[index]);

        isCaps = !isCaps;

       // Toast.makeText(this, "image tapped",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed(){

        Log.i("Back Pressed ","$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@$$$$$$$$$$$$$$$");
        Toast.makeText(this," Back button disabled",Toast.LENGTH_SHORT).show();;
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i("On Destroy","--------------------------------------------------------------------------------");


    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        int action = MotionEventCompat.getActionMasked(motionEvent);
        Log.i(" Action : ", Integer.toString(action));

        if(action == motionEvent.ACTION_DOWN) {
            intialX = motionEvent.getX();
            intialY = motionEvent.getY();
        }
        if(action == motionEvent.ACTION_UP) {
            finalX = motionEvent.getX();
            finalY = motionEvent.getY();
        }
        Log.i("X Position ",Float.toString(motionEvent.getX()));
        Log.i("Y position ",Float.toString(motionEvent.getY()));

        if(intialX != Float.MIN_VALUE && intialY != Float.MIN_VALUE && finalX != Float.MIN_VALUE && finalY != Float.MIN_VALUE) {
            Log.i(" Timestamp diff is ", Long.toString(System.currentTimeMillis() - lastSwipe));
            if (finalX - intialX > 100 && System.currentTimeMillis() - lastSwipe > SWIPE_DELAY) {

                Log.i(" Swipe ", " Right");
                imageView.setAlpha(0.1f);
                if (index > 0) {
                    index--;
                }
                isCaps = true;
                imageView.setImageResource(capsList[index]);
                lastSwipe = System.currentTimeMillis();
                Log.i("ztest ", "fdfd");
                imageView.animate().alpha(1f).setDuration(1000);

            } else if (intialX - finalX > 100 && System.currentTimeMillis() - lastSwipe > SWIPE_DELAY) {
                Log.i(" Swipe ", " Left");
                imageView.setAlpha(0.1f);
                index++;
                if (index > 25) {
                    index = index - 26 ;
                }
                imageView.setImageResource(capsList[index]);
                lastSwipe = System.currentTimeMillis();
                imageView.animate().alpha(1f).setDuration(1000);
                isCaps = true;
            } else
                Log.i(" Swipe ", " No H swipe Detected");

/*            if(finalY - intialY > 100){
                Log.i(" Swipe "," Down");
            }else if (intialY - finalY > 100){
                Log.i(" Swipe "," Up");
            }else
                Log.i(" Swipe "," No V swipe Detected");*/

            intialX = Float.MIN_VALUE;
            intialY = Float.MIN_VALUE;

            finalX = Float.MIN_VALUE;
            finalY = Float.MIN_VALUE;

        }
        return false;
    }
}
