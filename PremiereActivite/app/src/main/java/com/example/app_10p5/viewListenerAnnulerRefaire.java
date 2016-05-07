package com.example.app_10p5;

import android.view.View;

/**
 * Created by Jean-loup Beaussart on 06/05/2016.
 */
class viewListenerAnnulerRefaire implements View.OnClickListener {
    private final int mIdTransaction;
    private final MainActivite mMain;
    private final boolean mAnnuler;

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
