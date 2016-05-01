package com.example.app_10p5;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;


/**
 * Created by beaus on 24/04/2016.
 */
public class MainActivite extends FragmentActivity implements ASyncResponse {

    public static final int STATE_RIEN = 0;
    public static final int STATE_COMMANDE = 3;
    public static final int STATE_VIDANGE = 4;
    public static final int STATE_RECHARGEMENT = 2;
    public static final int STATE_CREATION_COMPTE = 1;
    public static final int STATE_CONNEXION = 5;
    public static final long EXPIRATION = 1000*60*10;

    private int mState;
    private String mToken;
    private long mTimeToken;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        mState = STATE_RIEN;
        mTimeToken = -1;
        mToken = "";

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addTab(tabLayout.newTab().setText("Connexion"));
        tabLayout.addTab(tabLayout.newTab().setText("Commande"));
        tabLayout.addTab(tabLayout.newTab().setText("Rechargement"));
        tabLayout.addTab(tabLayout.newTab().setText("Création"));
        tabLayout.addTab(tabLayout.newTab().setText("Vidange"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mToken != "" && System.currentTimeMillis() - mTimeToken < EXPIRATION){
                    viewPager.setCurrentItem(tab.getPosition());
                }
                else{
                    viewPager.setCurrentItem(tab.getPosition());    //Empeche un bug graphique
                    viewPager.setCurrentItem(0);
                    Toast.makeText(MainActivite.this, "Veuillez vous connecter.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void valideCommande(View v)
    {
        if((mToken != "") && ((System.currentTimeMillis() - mTimeToken) < EXPIRATION)) {
            EditText champMontant = (EditText) findViewById(R.id.commande_prix);
            EditText champQuantite = (EditText) findViewById(R.id.commande_quantite);
            float montant = Float.parseFloat(champMontant.getText().toString());
            int quantite = Integer.parseInt(champQuantite.getText().toString());

            if ((montant > 0.0) && (montant < 200.0)) {
                mState = STATE_COMMANDE;
                Intent intent = new Intent(this, CarteActivite.class);
                intent.putExtra("token", mToken);
                intent.putExtra("state", mState);
                intent.putExtra("montant", montant);
                intent.putExtra("quantite", quantite);
                startActivityForResult(intent, mState);
            }
        }
        else{
            Toast.makeText(this, "Veuillez vous reconnecter.", Toast.LENGTH_LONG).show();
            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setCurrentItem(0);
        }
    }

    public void valideRechargement(View v)
    {
        if((mToken != "") && ((System.currentTimeMillis() - mTimeToken) < EXPIRATION)) {

        }
        else{
            Toast.makeText(this, "Veuillez vous reconnecter.", Toast.LENGTH_LONG).show();
            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setCurrentItem(0);
        }
    }

    public void valideConnection(View v)
    {
        EditText viewUser = (EditText) findViewById(R.id.connection_username);
        EditText viewPsw = (EditText) findViewById(R.id.connection_password);

        String user = viewUser.getText().toString();
        String password = viewPsw.getText().toString();

        System.out.println(user + " " + password);

        if ((user != "") && (password != "")) {
            mState = STATE_CONNEXION;

            try{
                System.out.println("try");
                URL url = new URL(CarteActivite.HOST + "api/utilisateur/connexion");
                System.out.println("url");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //TODO: faire des choses avec ca
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
                if(output.get("status").toString() == "ok"){
                    mToken = output.get("token").toString();
                    mTimeToken = System.currentTimeMillis();
                }
                else{
                    Toast.makeText(this, "Erreur dans la requête: " + output.get("status"), Toast.LENGTH_LONG).show();
                }
            }
            catch(Throwable t){
                Toast.makeText(this, "WTF, le cancer est dans l'application!!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Impossible de se connecter au serveur", Toast.LENGTH_LONG).show();
        }
    }
}
