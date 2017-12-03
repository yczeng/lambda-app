package com.example.root.lambda;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static com.example.root.lambda.venmo.EXTRA_MESSAGE;

public class arduino extends AbstractAdkActivity {


    // For onCreate - check the Activity that this is extending - it is covered there, so do not override


    @Override
    protected void doOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_arduino);

        Intent priorIntent = getIntent();   // For getting the variables from the flavors that they chose.
        String message = priorIntent.getStringExtra(EXTRA_MESSAGE);

        Toast.makeText(this, "From Intent: " + message, Toast.LENGTH_SHORT).show();

        Button startFlow = (Button)findViewById(R.id.startFlow);
        //Button cancelFlow = (Button)findViewById(R.id.cancelFlow);

        GlobalVariableClass.getInstance().setCanVend(true);

//        if (message.equals("mang")) {
//            GlobalVariableClass.getInstance().setFlavor("mango");
//        }
//        else if (message.equals("milk") ) {
//            GlobalVariableClass.getInstance().setFlavor("milk");
//        }

        startFlow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (GlobalVariableClass.getInstance().getCanVend()) {
                    WriteAdk(GlobalVariableClass.getInstance().getFlavor());

                    Toast.makeText(getApplicationContext(),
                            "Sending: " + GlobalVariableClass.getInstance().getFlavor(), Toast.LENGTH_SHORT).show();

                    GlobalVariableClass.getInstance().setCanVend(false);

                    goNext();
                }

            }
        });


//        cancelFlow.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                WriteAdk("milk");
//                Toast.makeText(getApplicationContext(), "Sending milk", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    protected void doAdkRead(String stringIn) {
        Toast.makeText(getApplicationContext(), "return text: " + stringIn, Toast.LENGTH_SHORT).show();

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
