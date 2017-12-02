package com.example.root.lambda;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class dispensing extends AppCompatActivity {

    Thread thread1 = new Thread() {
        public void run() {
            playVideo();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispensing);

        thread1.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 10000);
    }



    public void playVideo(){
        final VideoView mVideoView2 = (VideoView)findViewById(R.id.videoView7);

        mVideoView2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mVideoView2.start(); //need to make transition seamless.
            }
        });

        String uriPath2 = "android.resource://com.example.root.lambda/" + R.raw.loading;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        mVideoView2.start();
    }
}
