package com.example.root.lambda;

import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public abstract  class AbstractAdkActivity extends AppCompatActivity {


    //do something in onCreate()
    protected abstract void doOnCreate(Bundle savedInstanceState);
//    //do something after adk read
//    protected abstract void doAdkRead(String stringIn);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        doOnCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Write String to Adk
    void WriteAdk(String text){
        UsbManager myUsbManager;
        UsbAccessory usbAccessory;

        myUsbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
        Log.e("lambdax", "1");
        while(myUsbManager == null){
            Log.e("lamdax", "1.5");
            Toast.makeText(this, "myUsbManager is stuck in null", Toast.LENGTH_SHORT).show();
            myUsbManager = (UsbManager)getSystemService(Context.USB_SERVICE);}
        Log.e("lambdax", "2");
        usbAccessory = myUsbManager.getAccessoryList()[0];
        while(usbAccessory == null) {
            Log.e("lamdax", "2.5");
            Toast.makeText(this, "usbAccessory is stuck in null", Toast.LENGTH_SHORT).show();
            usbAccessory = myUsbManager.getAccessoryList()[0];
        }
        Log.e("lambdax", "3");
        if(usbAccessory != null){
            Log.e("lambdax", "4");
            if(myUsbManager.hasPermission(usbAccessory)){
                Log.e("lambdax", "5");
                ParcelFileDescriptor parcelFileDescriptor = myUsbManager.openAccessory(usbAccessory);

                if(parcelFileDescriptor != null){
                    Log.e("lambdax", "6");

                    FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

                    try {fileOutputStream.write(text.getBytes());}
                    catch (IOException e) { Toast.makeText(this, "fileOutputStream.write FAILED", Toast.LENGTH_LONG).show();}


                    try {fileOutputStream.close();}
                    catch (IOException e) {Toast.makeText(this, "fileOutputStream.close FAILED", Toast.LENGTH_LONG).show();;}
                    Log.e("lambdax", "7");
                } else{
                    Toast.makeText(this, "Parcel File Desc is null", Toast.LENGTH_LONG).show();
                }

                try {parcelFileDescriptor.close();}
                catch (IOException e) {Toast.makeText(this, "parcelFileDescriptor.close FAILED", Toast.LENGTH_LONG).show();}

            } else{
                Toast.makeText(this, "USB Accessory does not have permission.", Toast.LENGTH_LONG).show();
            }
        }
        Toast.makeText(this, "usb Accessory is null!", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}