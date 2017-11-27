package com.example.root.lambda;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.root.lambda.venmo.EXTRA_MESSAGE;

public class arduino extends AbstractAdkActivity {


    // For onCreate - check the Activity that this is extending - it is covered there, so do not override


    @Override
    protected void doOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_arduino);

        Toast.makeText(this, "doOnCreate ran arduino", Toast.LENGTH_SHORT).show();

        Intent priorIntent = getIntent();   // For getting the variables from the flavors that they chose.
        String message = priorIntent.getStringExtra(EXTRA_MESSAGE);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        Button startFlow = (Button)findViewById(R.id.startFlow);
        Button cancelFlow = (Button)findViewById(R.id.cancelFlow);

        startFlow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WriteAdk("LEDON");
                Toast.makeText(getApplicationContext(),
                        "LEDON", Toast.LENGTH_SHORT).show();
            }
            });

        cancelFlow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WriteAdk("LEDOFF");
                Toast.makeText(getApplicationContext(), "LEDOFF", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void doAdkRead(String stringIn) {
        Toast.makeText(getApplicationContext(), "return text: " + stringIn, Toast.LENGTH_SHORT).show();

    }
    
    /* Called when the user taps the send button */
    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
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
}
