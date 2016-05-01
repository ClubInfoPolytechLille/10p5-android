package com.example.app_10p5;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by beaus on 24/04/2016.
 */
public class CarteActivite extends Activity implements ASyncResponse {

    private NfcAdapter mNfcAdapter;
    private HashMap<String, String> mParam;
    private String mAPI;

    public static final String HOST = "https://10p5.clubinfo.frogeye.fr/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_carte);

        switch (getIntent().getIntExtra("state", MainActivite.STATE_RIEN)) {

            case MainActivite.STATE_COMMANDE:
                mParam.put("quantite", String.valueOf(getIntent().getIntExtra("quantite", -1)));
                mParam.put("montant", String.valueOf(getIntent().getFloatExtra("montant", -1)));
                mParam.put("token", getIntent().getStringExtra("token"));
                mAPI = "api/client/payer";
                break;
            case MainActivite.STATE_CREATION_COMPTE:
                //TODO: param
                mAPI = "api/client/ajouter";
                break;
            case MainActivite.STATE_RECHARGEMENT:
                //TODO: param
                mAPI = "api/client/recharger";
                break;
            case MainActivite.STATE_VIDANGE:
                //TODO: param
                mAPI = "api/client/vidange";
                break;
            case MainActivite.STATE_CONNEXION:
            case MainActivite.STATE_RIEN:
            default:
                Toast.makeText(this, "WTF, le cancer est dans l'application!!", Toast.LENGTH_LONG).show();
                finish();
                return;
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
            Toast toast;
            String id_carte = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            mParam.put("ID", id_carte);
            toast = Toast.makeText(getApplicationContext(), "ID Carte : " + id_carte, Toast.LENGTH_SHORT);
            toast.show();

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
                            toast = Toast.makeText(getApplicationContext(), "Données lues : " + dataStr, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        toast = Toast.makeText(getApplicationContext(), "Erreur lors de la connection au secteur 12.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    mfc.close();
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }

                //Concaténation des données récupérées en login
                login = prenom;
                login.concat(".");
                login.concat(nom);
            } else {
                toast = Toast.makeText(getApplicationContext(), "Pas de connection possible à la technologie Mifare Classic.", Toast.LENGTH_SHORT);
                toast.show();
            }

            toast = Toast.makeText(getApplicationContext(), "Login Lille 1 : " + login, Toast.LENGTH_SHORT);
            toast.show();

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
            URL url = new URL(HOST + mAPI);
            NetworkThread nt = new NetworkThread(url, mParam);
            nt.delegate = this;
            nt.execute();
        }
        catch (Throwable t){
            Toast.makeText(this, "WTF, le cancer est dans l'application!!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }


    /* Retour du NetworkThread */
    @Override
    public void processFinish(JSONObject output) {
        //TODO: faire un retour vers l'activity parente des données reçues.
    }
}
