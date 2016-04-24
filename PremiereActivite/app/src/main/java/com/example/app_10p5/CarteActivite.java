package com.example.app_10p5;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by beaus on 24/04/2016.
 */
public class CarteActivite extends Activity {

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_carte);

        ////Code NFC -- Nécessaire
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        try {
            ndef.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][]{new String[]{NfcA.class.getName(), MifareClassic.class.getName()}};
    }

    public void onResume() {
        super.onResume();
        if(mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
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
    public void onNewIntent(Intent intent) {
        mToast = Toast.makeText(getApplicationContext(), "ID Carte : ", Toast.LENGTH_SHORT);
        mToast.show();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            String id_carte = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            mToast = Toast.makeText(getApplicationContext(), "ID Carte : " + id_carte, Toast.LENGTH_SHORT);
            mToast.show();
            System.out.println("ID Carte : " + id_carte);

            //Lecture des données
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tag);
            byte[] data;
            String prenom = null, nom = null, login = null;

            if (mfc != null) {
                try {
                    mfc.connect();
                    String cardData = null;

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
                            mToast = Toast.makeText(getApplicationContext(), "Données lues : " + dataStr, Toast.LENGTH_SHORT);
                            mToast.show();
                            System.out.println("Données lues : " + dataStr);
                        }
                    } else {
                        mToast = Toast.makeText(getApplicationContext(), "Erreur lors de la connection au secteur 12.", Toast.LENGTH_SHORT);
                        mToast.show();
                        System.out.println("Erreur lors de la connection au secteur 12.");
                    }
                    mfc.close();
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }

                //Concaténation des données récupérées en login
                login = prenom;
                login.concat(".");
                login.concat(nom);
                System.out.println("Prenom : " + prenom);
                System.out.println("Nom : " + nom);
            } else {
                System.out.println("Pas de connection possible à la technologie Mifare Classic.");
                mToast = Toast.makeText(getApplicationContext(), "Pas de connection possible à la technologie Mifare Classic.", Toast.LENGTH_SHORT);
                mToast.show();
            }

            mToast = Toast.makeText(getApplicationContext(), "Login Lille 1 : " + login, Toast.LENGTH_SHORT);
            mToast.show();
            System.out.println("Login Lille 1 : " + login);


            //Éxécution de la fonction
            taFonction(id_carte, login);
        }
    }

    public void taFonction(String id_carte, String login)
    {
        //code fonction
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }
}
