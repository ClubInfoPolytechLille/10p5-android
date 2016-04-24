package com.example.florian.premiereactivite;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;

public class Menu extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private Button valider = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        TextView text = new TextView(this);
        text.setText("Veuillez entrer les informations suivantes:");
        setContentView(text);
        // l'utilisateur entre un identifiant
        EditText identifiant = new EditText(this);
        identifiant.setHint(R.string.identifiant);
        identifiant.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        identifiant.setLines(2);
        // l'utilisateur entre un mot de passe
        EditText password = new EditText(this);
        password.setHint(R.string.password);
        password.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        password.setLines(2);
        //element de validation
        valider = (Button) findViewById(R.id.boutton);
        valider.setOnTouchListener(this);
        valider.setOnClickListener(this);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        /*reagir au toucher */
        return true;
    }
    @Override
    public void onClick(View v){
        /* reagir au clic */
    }

}
