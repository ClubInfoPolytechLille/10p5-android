package com.example.app_10p5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Jean-loup Beaussart on 07/05/2016.
 */
public class Dialogue extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Session expirée")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivite parent = (MainActivite) getActivity();
                        parent.disconnect();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
