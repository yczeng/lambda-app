package com.example.root.lambda;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract  class AbstractAdkActivity extends AppCompatActivity {

    private static int RQS_USB_PERMISSION = 0;
    private static final String ACTION_USB_PERMISSION = "arduino-er.usb_permission";
    private PendingIntent PendingIntent_UsbPermission;

    private UsbManager myUsbManager;
    boolean firstRqsPermission;

    //do something in onCreate()
    protected abstract void doOnCreate(Bundle savedInstanceState);
    //do something after adk read
    protected abstract void doAdkRead(String stringIn);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        myUsbManager = (UsbManager)getSystemService(Context.USB_SERVICE);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        registerReceiver(myUsbReceiver, intentFilter);

        //Ask USB Permission from user
        Intent intent_UsbPermission = new Intent(ACTION_USB_PERMISSION);
        PendingIntent_UsbPermission = PendingIntent.getBroadcast(
                this,      //context
                RQS_USB_PERMISSION,  //request code
                intent_UsbPermission, //intent
                0);      //flags
        IntentFilter intentFilter_UsbPermission = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(myUsbPermissionReceiver, intentFilter_UsbPermission);

        firstRqsPermission = true;


        doOnCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(GlobalVariableClass.getInstance().getGlobalAdkInputStream() == null || GlobalVariableClass.getInstance().getGlobalAdkOutputStream() == null){

            UsbAccessory[] usbAccessoryList = myUsbManager.getAccessoryList();
            UsbAccessory usbAccessory = null;
            Toast.makeText(this, "onResume running", Toast.LENGTH_SHORT).show();
            if(usbAccessoryList != null){
                usbAccessory = usbAccessoryList[0];

                if(usbAccessory != null){
                    if(myUsbManager.hasPermission(usbAccessory)){
                        //already have permission
                        Toast.makeText(this, "onresume to openusbacc", Toast.LENGTH_SHORT).show();
                        OpenUsbAccessory(usbAccessory);
                    }else{

                        if(firstRqsPermission){

                            firstRqsPermission = false;

                            synchronized(myUsbReceiver){
                                myUsbManager.requestPermission(usbAccessory,
                                        PendingIntent_UsbPermission);
                            }
                        }

                    }
                }
            }
        }
    }

    //Write String to Adk
    void WriteAdk(String text){

        byte[] buffer = text.getBytes();

        if(GlobalVariableClass.getInstance().getGlobalAdkOutputStream() != null){

            try {
                GlobalVariableClass.getInstance().getGlobalAdkOutputStream().write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //closeUsbAccessory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myUsbReceiver);
        unregisterReceiver(myUsbPermissionReceiver);
    }

    Runnable runnableReadAdk = new Runnable(){

        @Override
        public void run() {
            int numberOfByteRead = 0;
            byte[] buffer = new byte[255];

            while(numberOfByteRead >= 0){

                try {
                    numberOfByteRead = GlobalVariableClass.getInstance().getGlobalAdkInputStream().read(buffer, 0, buffer.length);
                    final StringBuilder stringBuilder = new StringBuilder();
                    for(int i=0; i<numberOfByteRead; i++){
                        stringBuilder.append((char)buffer[i]);
                    }

                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            doAdkRead(stringBuilder.toString());
                        }});


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
                }
            }
        }

    };

    private BroadcastReceiver myUsbReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals(UsbManager.ACTION_USB_ACCESSORY_DETACHED)){

                UsbAccessory usbAccessory =
                        (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

                if(usbAccessory!=null && usbAccessory.equals(GlobalVariableClass.getInstance().getGlobalUsbAccessory())){
                    //closeUsbAccessory();
                }
            }
        }
    };

    private BroadcastReceiver myUsbPermissionReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_USB_PERMISSION)){

                synchronized(this){

                    UsbAccessory usbAccessory =
                            (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

                    if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)){
                        OpenUsbAccessory(usbAccessory);
                    }else{
                        finish();
                    }
                }
            }
        }

    };

    private void OpenUsbAccessory(UsbAccessory acc){
        Toast.makeText(this, "open usb accessory:" + acc.getManufacturer(), Toast.LENGTH_SHORT).show();


        if (GlobalVariableClass.getInstance().getAPFD() == null) {
            GlobalVariableClass.getInstance().setAPFD(myUsbManager.openAccessory(acc));
        }

        if(GlobalVariableClass.getInstance().getAPFD() != null){
            Toast.makeText(this, "myAdkParcelFD != null", Toast.LENGTH_SHORT).show();

            GlobalVariableClass.getInstance().setGlobalUsbAccessory(acc);
            FileDescriptor fileDescriptor = GlobalVariableClass.getInstance().getAPFD().getFileDescriptor();

            GlobalVariableClass.getInstance().setGlobalAdkInputStream(new FileInputStream(fileDescriptor));
            GlobalVariableClass.getInstance().setGlobalAdkOutputStream(new FileOutputStream(fileDescriptor));

            Thread thread = new Thread(runnableReadAdk);
            thread.start();

            Toast.makeText(this, "started new thread", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeUsbAccessory(){
        Toast.makeText(this, "close usb accessory", Toast.LENGTH_SHORT).show();
        if(GlobalVariableClass.getInstance().getAPFD() != null){
            try {
                GlobalVariableClass.getInstance().getAPFD().close();
            } catch (IOException e) {
                Toast.makeText(this, "error in closing usb acc", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        GlobalVariableClass.getInstance().setAPFD(null);
        GlobalVariableClass.getInstance().setGlobalUsbAccessory(null);
    }
}
