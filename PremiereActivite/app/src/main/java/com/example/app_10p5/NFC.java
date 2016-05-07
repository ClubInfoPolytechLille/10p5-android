package com.example.app_10p5;

import android.app.Fragment;
import android.content.Intent;

/**
 * Created by Jean-loup Beaussart on 07/05/2016.
 */
public abstract class NFC extends Fragment {

    // Convertit l'array de byte en chaîne hexadécimale (si le byte = 0x63, str = "63").
    public String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";
        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


    public abstract void handleIntent(Intent intent);
}
