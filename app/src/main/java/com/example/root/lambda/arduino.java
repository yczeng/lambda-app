package com.example.root.lambda;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static com.example.root.lambda.venmo.EXTRA_MESSAGE;

public class arduino extends AppCompatActivity {


    // For onCreate - check the Activity that this is extending - it is covered there, so do not override


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);

        Intent priorIntent = getIntent();   // For getting the variables from the flavors that they chose.
        String message = priorIntent.getStringExtra(EXTRA_MESSAGE);

        Toast.makeText(this, "From Intent: " + message, Toast.LENGTH_SHORT).show();

        final Button startFlow = (Button)findViewById(R.id.startFlow);
        //Button cancelFlow = (Button)findViewById(R.id.cancelFlow);
        startFlow.setEnabled(true);


        startFlow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Toast.makeText(arduino.this, "Pressed - about to send: " + GlobalVariableClass.getInstance().getFlavor(), Toast.LENGTH_SHORT).show();

            // For the audio sending - below

            MediaPlayer mp;

            if(GlobalVariableClass.getInstance().getFlavor().equals("milk")) {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.hz36_1s);
            }
            else if(GlobalVariableClass.getInstance().getFlavor().equals("mango")) {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.hz36_point8s); // Else, create it for MANGO
            }
            else if(GlobalVariableClass.getInstance().getFlavor().equals("taro")) {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.hz36_point5s); // Else, create it for MANGO
            }
            else {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.hz36_point2s); // Else, create it for MANGO
            }

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    // TODO Auto-generated method stub
                    mp.reset();
                    mp.release();
                    mp = null;
                }

            });
            mp.start();

            // For the Audio sending - above
`1``
            Toast.makeText(getApplicationContext(),
                    "Sent: " + GlobalVariableClass.getInstance().getFlavor(), Toast.LENGTH_SHORT).show();

            startFlow.setEnabled(false);
            goNext();
            }
        });
    }


    /* Called when the user taps the send button */
    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /* Called when the user taps the send button */
    public void goNext() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //Nothing
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
}
