package com.example.app_10p5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by Jean-loup Beaussart on 05/05/2016.
 */
public class ConnectionFragment extends NFC {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.layout_connection, container, false);
        return ret;
    }

    @Override
    public void handleIntent(Intent intent) {
        try{
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            URL url = new URL(settings.getString("server_address", null) + "api/utilisateur/connexion");
            HashMap<String, String> param = new HashMap<String, String>();
            String id_carte = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            param.put("idCarte", id_carte);
            NetworkThread nt = new NetworkThread(url, param);
            nt.delegate = (MainActivite) getActivity();
            nt.execute();
        }
        catch (Throwable t) {
            Toast.makeText(getActivity(), "Erreur: " + t.toString(), Toast.LENGTH_LONG).show();
            System.out.println("Exception: " + t.toString());
        }
    }
}
