package com.example.app_10p5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Jean-loup Beaussart on 05/05/2016.
 */
public class NFCFragment extends NFC {
    private HashMap<String, String> mParam;
    private String mAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_carte, container, false);

        mParam = new HashMap<String, String>();

        Bundle b = getArguments();

        try {
            mParam.put("jeton", URLEncoder.encode(b.getString("token"), "UTF-8"));
            switch (b.getInt("state")) {
                case MainActivite.STATE_COMMANDE:
                    if(b.getInt("quantite") != 0){
                        mParam.put("quantite", URLEncoder.encode(String.valueOf(b.getInt("quantite")), "UTF-8"));
                    }
                    else{
                        mParam.put("montant", URLEncoder.encode(String.valueOf(b.getFloat("montant")), "UTF-8"));

                    }
                    mAPI = "api/client/payer";
                    break;
                case MainActivite.STATE_CREATION_COMPTE:
                    mParam.put("solde", URLEncoder.encode(String.valueOf(b.getFloat("montant")), "UTF-8"));
                    mAPI = "api/client/ajouter";
                    break;
                case MainActivite.STATE_RECHARGEMENT:
                    mParam.put("montant", URLEncoder.encode(String.valueOf(b.getFloat("montant")), "UTF-8"));
                    mAPI = "api/client/recharger";
                    break;
                case MainActivite.STATE_VIDANGE:
                    mAPI = "api/client/vidange";
                    break;
                case MainActivite.STATE_CONNEXION:  //Impossible c'est pas géré ici
                case MainActivite.STATE_ANNULER:
                case MainActivite.STATE_REFAIRE:
                case MainActivite.STATE_RIEN:
                default:
                    Snackbar.make(getActivity().findViewById(R.id.coordinator), "WTF, le cancer est dans l'application!!", Snackbar.LENGTH_INDEFINITE).show();
            }
        }
        catch (Throwable t){
            System.out.println("Exception: " + t.toString());
            Snackbar.make(getActivity().findViewById(R.id.coordinator), "WTF, le cancer est dans l'application!! " + t.toString(), Snackbar.LENGTH_INDEFINITE).show();
        }

        return ret;
    }

    @Override
    public void handleIntent(Intent intent){
        String id_carte = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
        mParam.put("idCarte", id_carte);
        clientAPI();
    }


    public void clientAPI() {
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            URL url = new URL(settings.getString("server_address", null) + mAPI);
            NetworkThread nt = new NetworkThread(url, mParam);
            nt.delegate = (MainActivite) getActivity();
            nt.execute();
        }
        catch (Throwable t){
            Toast.makeText(getActivity().getApplicationContext(), "WTF, le cancer est dans l'application!! " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
