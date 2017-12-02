package com.example.root.lambda;


import android.hardware.usb.UsbAccessory;
import android.os.ParcelFileDescriptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GlobalVariableClass {

    private static GlobalVariableClass mInstance = null;

    private ParcelFileDescriptor globalAdkParcelFileDescriptor = null;

    private UsbAccessory globalUsbAccessory = null;

    private FileInputStream globalAdkInputStream = null;
    private FileOutputStream globalAdkOutputStream = null;

    private boolean canVend = false;
    private String  flavor  = "milk";

    private GlobalVariableClass() {
        // The empty class
    }

    static synchronized GlobalVariableClass getInstance() {
        if (null == mInstance) {
            mInstance = new GlobalVariableClass();
        }
        return mInstance;
    }

    ParcelFileDescriptor getAPFD() {
        return globalAdkParcelFileDescriptor;
    }
    void setAPFD(ParcelFileDescriptor newGlobalAdkParcelFileDescriptor) { globalAdkParcelFileDescriptor = newGlobalAdkParcelFileDescriptor; }

    UsbAccessory getGlobalUsbAccessory() {
        return globalUsbAccessory;
    }
    void setGlobalUsbAccessory(UsbAccessory newGlobalUsbAccessory) { globalUsbAccessory = newGlobalUsbAccessory; }

    FileInputStream getGlobalAdkInputStream() {
        return globalAdkInputStream;
    }
    void setGlobalAdkInputStream(FileInputStream newGlobalAdkInputStream) { globalAdkInputStream = newGlobalAdkInputStream; }

    FileOutputStream getGlobalAdkOutputStream() {
        return globalAdkOutputStream;
    }
    void setGlobalAdkOutputStream(FileOutputStream newGlobalAdkOutputStream) { globalAdkOutputStream = newGlobalAdkOutputStream; }

    boolean getCanVend() {return canVend;}
    void setCanVend(boolean newCanVend) {canVend = newCanVend;}

    String getFlavor() {return flavor;}
    void setFlavor(String newFlavor) {flavor = newFlavor;}


    private boolean kioskMode = false;
    boolean getKioskMode(){return kioskMode; }
    void setKioskMode(boolean boo) {kioskMode = boo;}

    void resetEverything() {
        globalAdkParcelFileDescriptor = null;
        globalUsbAccessory = null;
        globalAdkInputStream = null;
        globalAdkOutputStream = null;
    }
}

