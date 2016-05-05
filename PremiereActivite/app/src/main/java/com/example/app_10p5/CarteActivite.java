package com.example.app_10p5;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by beaus on 24/04/2016.
 */
public class CarteActivite extends Activity implements ASyncResponse {

    private NfcAdapter mNfcAdapter;
    private HashMap<String, String> mParam;
    private String mAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_carte);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        mParam = new HashMap<String, String>();

        try {
            switch (getIntent().getIntExtra("state", MainActivite.STATE_RIEN)) {
                case MainActivite.STATE_COMMANDE:
                    //TODO: XOR du cancer
                    //mParam.put("quantite", String.valueOf(getIntent().getIntExtra("quantite", -1)));
                    mParam.put("montant", URLEncoder.encode(String.valueOf(getIntent().getFloatExtra("montant", -1)), "UTF-8"));
                    mParam.put("jeton", URLEncoder.encode(getIntent().getStringExtra("token"), "UTF-8"));
                    mAPI = "api/client/payer";
                    break;
                case MainActivite.STATE_CREATION_COMPTE:
                    mParam.put("solde", URLEncoder.encode(String.valueOf(getIntent().getFloatExtra("montant", -1)), "UTF-8"));
                    mParam.put("jeton", URLEncoder.encode(getIntent().getStringExtra("token"), "UTF-8"));
                    mAPI = "api/client/ajouter";
                    break;
                case MainActivite.STATE_RECHARGEMENT:
                    System.out.println("bite");
                    mParam.put("montant", URLEncoder.encode(String.valueOf(getIntent().getFloatExtra("montant", -1)), "UTF-8"));
                    mParam.put("jeton", URLEncoder.encode(getIntent().getStringExtra("token"), "UTF-8"));
                    mAPI = "api/client/recharger";
                    break;
                case MainActivite.STATE_VIDANGE:
                    mParam.put("jeton", URLEncoder.encode(getIntent().getStringExtra("token"), "UTF-8"));
                    mAPI = "api/client/vidange";
                    break;
                case MainActivite.STATE_CONNEXION:  //Impossible c'est pas géré ici
                case MainActivite.STATE_RIEN:
                default:
                    Toast.makeText(this, "WTF, le cancer est dans l'application!!", Toast.LENGTH_LONG).show();
                    finish();
                    return;
            }
        }
        catch (Throwable t){
            System.out.println("Exception: " + t.toString());
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    // Convertit l'array de byte en chaîne hexadécimale (si le byte = 0x63, str = "63").
    private String ByteArrayToHexString(byte [] inarray) {
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

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    private void handleIntent(Intent intent){
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            String id_carte = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            mParam.put("idCarte", id_carte);

            //Lecture des données
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tag);
            byte[] data;
            String prenom = null, nom = null, login = null;

            if (mfc != null) {
                try {
                    mfc.connect();

                    //Clé A
                    byte[] cleA = new byte[]{(byte) 0xa0, (byte) 0xa1, (byte) 0xa2,
                            (byte) 0xa3, (byte) 0xa4, (byte) 0xa5};

                    //On veut juste lire le secteur 12
                    boolean estConnecte = mfc.authenticateSectorWithKeyA(12, cleA);

                    if (estConnecte) {
                        //Il y a 4 blocs dans le secteur 12 -> INE, numéro étudiant, prénom, nom
                        //On ne veut que prénom et nom, donc on passe les deux premiers
                        for (int i = 2, bIndex = mfc.sectorToBlock(12) + i; i < 4; i++, bIndex++) {
                            //sectorToBlock : Renvoie l'indice (global, parmis tous les blocs de
                            // tous les secteurs) du premier bloc du secteur 12
                            //Lit les données du bloc
                            data = mfc.readBlock(bIndex);

                            //Convertit les bytes en String
                            String dataStr = new String(data);
                            if (i == 2) //Prénom
                                prenom = dataStr;
                            else if (i == 3) //Nom
                                nom = dataStr;
                        }
                    } else {
                        Toast.makeText(this, "Impossible de lire le secteur 12", Toast.LENGTH_LONG).show();
                    }
                    mfc.close();
                } catch (Throwable t) {
                    Toast.makeText(this, "WTF, le cancer est dans l'application!! Autodéstruction dans 5 secondes !!!!!!!!!" + t.toString(), Toast.LENGTH_LONG).show();
                }

                //Concaténation des données récupérées en login
                login = prenom;
                login.concat(".");
                login.concat(nom);
            } else {
                Toast.makeText(this, "Pas de Mifare Classic", Toast.LENGTH_SHORT).show();
            }

            //Éxécution de la fonction
            clientAPI();
            }
        }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter){
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
        adapter.enableForegroundDispatch(activity, pendingIntent, null, null);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    public void clientAPI() {
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            URL url = new URL(settings.getString("server_address", null) + mAPI);
            NetworkThread nt = new NetworkThread(url, mParam);
            nt.delegate = this;
            nt.execute();
        }
        catch (Throwable t){
            Toast.makeText(this, "WTF, le cancer est dans l'application!! " + t.toString(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }


    /* Retour du NetworkThread */
    @Override
    public void processFinish(JSONObject output) {
        Intent intent = new Intent(this, CarteActivite.class);
        intent.putExtra("json", output.toString());
        setResult(0, intent);
        finish();
    }
}
