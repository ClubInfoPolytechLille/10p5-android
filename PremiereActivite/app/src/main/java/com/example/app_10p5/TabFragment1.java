package com.example.app_10p5;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Jean-loup Beaussart on 24/04/2016.
 */

public class TabFragment1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_1, container, false);
        EditText et = (EditText) v.findViewById(R.id.commande_prix);

        et.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});

        final ArrayList<View> allChildren = getAllChildren(v.findViewById(R.id.commande_table_boutons));
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length() != 0){
                    for (View child : allChildren) {
                        if (child instanceof Button) {
                            Button bouton = (Button) child;
                            bouton.setEnabled(false);
                        }
                    }
                }
                else{
                    for (View child : allChildren) {
                        if (child instanceof Button) {
                            Button bouton = (Button) child;
                            bouton.setEnabled(true);
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}