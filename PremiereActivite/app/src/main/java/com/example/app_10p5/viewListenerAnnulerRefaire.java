package com.example.app_10p5;

import android.view.View;

/**
 * Created by Jean-loup Beaussart on 06/05/2016.
 */
public class viewListenerAnnulerRefaire implements View.OnClickListener {
    private int mIdTransaction;
    private MainActivite mMain;
    private boolean mAnnuler;

    viewListenerAnnulerRefaire(int idTransaction, MainActivite main, boolean annuler){
        mIdTransaction = idTransaction;
        mMain = main;
        mAnnuler = annuler;
    }

    @Override
    public void onClick(View v){
        mMain.annulerTransaction(mIdTransaction, mAnnuler);
    }
}
