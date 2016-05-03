package com.example.app_10p5;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;


/**
 * Created by beaus on 24/04/2016.
 */
public class MainActivite extends Activity implements ASyncResponse, main_tab_frag.OnFragmentInteractionListener {

    public static final int STATE_RIEN = 0;
    public static final int STATE_COMMANDE = 3;
    public static final int STATE_VIDANGE = 4;
    public static final int STATE_RECHARGEMENT = 2;
    public static final int STATE_CREATION_COMPTE = 1;
    public static final int STATE_CONNEXION = 5;
    public static final long EXPIRATION = 1000*60*10;

    private int mState;
    private String mToken;
    private int mDroit;
    private long mTimeToken;
    private String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        mState = STATE_RIEN;
        mTimeToken = -1;
        mToken = "";

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        main_tab_frag fragment = new main_tab_frag();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
                return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String s){

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("token", mToken);
        savedInstanceState.putInt("state", mState);
        savedInstanceState.putString("user", mUser);
        savedInstanceState.putInt("droit", mDroit);
        savedInstanceState.putLong("timeToken", mTimeToken);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mTimeToken = savedInstanceState.getLong("timeToken");
        mToken = savedInstanceState.getString("user");
        mState = savedInstanceState.getInt("state");
        mUser = savedInstanceState.getString("user");
        mDroit = savedInstanceState.getInt("droit");

        super.onRestoreInstanceState(savedInstanceState);
    }

    public void valideCreationCompte(View v){
        if((mToken != "") && ((System.currentTimeMillis() - mTimeToken) < EXPIRATION)) {
            EditText champMontant = (EditText) findViewById(R.id.creation_montant);
            float montant = 0.0f;

            try{
                montant = Float.parseFloat(champMontant.getText().toString());
            }
            catch (Throwable t){
                Toast.makeText(this, "Remplir le champ montant avec un nombre: " + t.toString(), Toast.LENGTH_LONG).show();
            }

            if((montant > 0.0f) && (montant < 200.0f) && (mDroit >= 1)){
                mState = STATE_CREATION_COMPTE;
                Intent intent = new Intent(this, CarteActivite.class);
                intent.putExtra("token", mToken);
                intent.putExtra("state", mState);
                intent.putExtra("montant", montant);
                startActivityForResult(intent, mState);
            }
            else{
                Toast.makeText(this, "Valeur incorrecte ou droit insuffisant.", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Veuillez vous reconnecter.", Toast.LENGTH_LONG).show();
            /*final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setCurrentItem(0);*/
        }
    }

    public void valideCommande(View v)
    {
        if((mToken != "") && ((System.currentTimeMillis() - mTimeToken) < EXPIRATION)) {
            EditText champMontant = (EditText) findViewById(R.id.commande_prix);
            EditText champQuantite = (EditText) findViewById(R.id.commande_quantite);
            float montant = 0.0f;
            int quantite = 0;

            try{
                montant = Float.parseFloat(champMontant.getText().toString());
                quantite = Integer.parseInt(champQuantite.getText().toString());
            }
            catch (Throwable t)
            {
                Toast.makeText(this, "Remplir les champs avec des nombres: " + t.toString(), Toast.LENGTH_LONG).show();
            }

            if ((montant > 0.0f) && (montant < 200.0f) && (quantite > 0) && (mDroit >= 1)) {
                mState = STATE_COMMANDE;
                Intent intent = new Intent(this, CarteActivite.class);
                intent.putExtra("token", mToken);
                intent.putExtra("state", mState);
                intent.putExtra("montant", montant);
                intent.putExtra("quantite", quantite);
                startActivityForResult(intent, mState);
            }
            else{
                Toast.makeText(this, "Valeur incorrecte ou droit insuffisant.", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Veuillez vous reconnecter.", Toast.LENGTH_LONG).show();
            /*final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setCurrentItem(0);*/
        }
    }

    public void valideRechargement(View v)
    {
        if((mToken != "") && ((System.currentTimeMillis() - mTimeToken) < EXPIRATION)) {
            EditText champMontant = (EditText) findViewById(R.id.rechargement_champ_montant);
            float montant = 0.0f;

            try{
                montant = Float.parseFloat(champMontant.getText().toString());
            }
            catch (Throwable t){
                Toast.makeText(this, "Remplir le champ montant avec un nombre: " + t.toString(), Toast.LENGTH_LONG).show();
            }

            if((montant > 0.0f) && (montant < 200.0f) && (mDroit >= 2)){
                mState = STATE_RECHARGEMENT;
                Intent intent = new Intent(this, CarteActivite.class);
                intent.putExtra("token", mToken);
                intent.putExtra("state", mState);
                intent.putExtra("montant", montant);
                startActivityForResult(intent, mState);
            }
            else{
                Toast.makeText(this, "Valeur incorrecte ou droit insuffisant.", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Veuillez vous reconnecter.", Toast.LENGTH_LONG).show();
            /*final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setCurrentItem(0);*/
        }
    }

    public void valideConnection(View v)
    {
        EditText viewUser = (EditText) findViewById(R.id.connection_username);
        EditText viewPsw = (EditText) findViewById(R.id.connection_password);

        String user = viewUser.getText().toString();
        String password = viewPsw.getText().toString();

        if ((user != "") && (password != "")) {
            mState = STATE_CONNEXION;

            try{
                URL url = new URL(CarteActivite.HOST + "api/utilisateur/connexion");
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("login", user);
                param.put("mdp", password);
                NetworkThread nt = new NetworkThread(url, param);
                nt.delegate = this;
                nt.execute();
            }
            catch (Throwable t) {
                //TODO: gérer les exceptions du cancer de la connexion
            }
        }
        else{
            Toast.makeText(this, "Veuillez remplir les champs.", Toast.LENGTH_LONG).show();
        }
    }

    public void valideVidange(View v){
        if((mToken != "") && ((System.currentTimeMillis() - mTimeToken) < EXPIRATION)) {

            if((mDroit >= 2)){
                mState = STATE_VIDANGE;
                Intent intent = new Intent(this, CarteActivite.class);
                intent.putExtra("token", mToken);
                intent.putExtra("state", mState);
                startActivityForResult(intent, mState);
            }
            else{
                Toast.makeText(this, "Droit insuffisant.", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Veuillez vous reconnecter.", Toast.LENGTH_LONG).show();
            /*final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setCurrentItem(0);*/
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //TODO: faire des choses avec ca

        try{
            JSONObject json = new JSONObject(data.getStringExtra("json"));
            Toast.makeText(this, "Status: " + json.getString("status"), Toast.LENGTH_SHORT).show();

        }
        catch (Throwable t){
            Toast.makeText(this, "WTF, le cancer est dans l'application!!", Toast.LENGTH_LONG).show();
        }


        switch (requestCode) {
            case STATE_COMMANDE:
                break;
            case STATE_CONNEXION:
                break;
            case STATE_CREATION_COMPTE:
                break;
            case STATE_RECHARGEMENT:
                break;
            case STATE_VIDANGE:
                break;
            case STATE_RIEN:
            default:
                Toast.makeText(this, "WTF, le cancer est dans l'application!!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /* Retour du network thread */
    @Override
    public void processFinish(JSONObject output) {
        if(output.length() != 0){
            try{
                if(output.get("status").toString().equals("ok")){
                    mToken = output.get("jeton").toString();
                    mTimeToken = System.currentTimeMillis();
                    mDroit = output.getInt("droit");
                    mUser = output.get("login").toString();
                    Toast.makeText(this, "Bonjour " + mUser + " vous êtes bien connecté pour " + EXPIRATION / (1000 * 60) + " minutes.", Toast.LENGTH_LONG).show();
                    EditText coUser = (EditText) findViewById(R.id.connection_password);
                    coUser.setText("");
                    /*final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                    viewPager.setCurrentItem(1);*/
                }
                else{
                    Toast.makeText(this, "Erreur dans la requête: " + output.get("status"), Toast.LENGTH_LONG).show();
                }
            }
            catch(Throwable t){
                Toast.makeText(this, "WTF, le cancer est dans l'application!!" + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Impossible de se connecter au serveur", Toast.LENGTH_LONG).show();
        }
    }

    public String getToken(){
        return mToken;
    }

    public long getTimeToken(){
        return mTimeToken;
    }
}
