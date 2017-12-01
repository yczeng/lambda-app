package com.example.root.lambda;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.java_websocket.WebSocket;
import tech.gusavila92.websocketclient.WebSocketClient;

import static java.math.BigInteger.probablePrime;


public class venmo extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    Thread thread1 = new Thread() {
        public void run() {
            playVideo();
        }
    };

    Thread thread2 = new Thread() {
        public void run() {
            createWebSocketClient();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setContentView(R.layout.activity_venmo);

        thread1.start();
        thread2.start();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(this, "Volume button is disabled", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            Toast.makeText(this, "Volume button is disabled", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private WebSocketClient webSocketClient;
    private void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI("ws://34.213.12.212:9001");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            long B = 2261516941L;
            long C = 4252492303L;
            BigInteger A_bi = BigInteger.probablePrime(32, new Random());
            long A = Math.abs(A_bi.intValue());
//            long A = 41;
            String A_C_string = String.valueOf(A * C);

            @Override
            public void onOpen() {
                System.out.println("onOpen");
                long send = B * A;
//                webSocketClient.send("A IS: " + String.valueOf(A));
                Intent current = getIntent();
                String message = current.getStringExtra(DisplayMessageActivity.EXTRA_MESSAGE);

                webSocketClient.send(message + String.valueOf(send));
                verifyConnection(null);
            }

            @Override
            public void onTextReceived(String message) {
                if (message.equals(A_C_string)) {
                    webSocketClient.send("now vending boba");
                    move();
                }
                System.out.println("onTextReceived");
            }

            @Override
            public void onBinaryReceived(byte[] data) {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                ((TextView)findViewById(R.id.textView)).setText("Waiting for connection...");
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(500);
//        webSocketClient.setReadTimeout(180000);
        webSocketClient.enableAutomaticReconnection(500);
        webSocketClient.connect();
    }

    public void move() {
        Intent current = getIntent();
        String message = current.getStringExtra(DisplayMessageActivity.EXTRA_MESSAGE);

        Intent intent = new Intent(this, arduino.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        finish();
    }

    /* Called when the user taps the send button */
    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void verifyConnection(View view) {
        ((TextView)findViewById(R.id.textView)).setText("Please venmo $3.50 to @LambdaTea to continue.");
    }

    public void playVideo(){
        final VideoView mVideoView2 = (VideoView)findViewById(R.id.videoView6);

        mVideoView2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mVideoView2.start(); //need to make transition seamless.
            }
        });

        String uriPath2 = "android.resource://com.example.root.lambda/"+R.raw.loading;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        mVideoView2.start();
    }
}
