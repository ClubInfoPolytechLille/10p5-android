package com.example.app_10p5;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by beaus on 30/04/2016.
 */
public class NetworkThread extends AsyncTask<Void, Void, JSONObject> {
    public ASyncResponse delegate = null;
    private URL mUrl;
    private HashMap<String, String> mParam;

    NetworkThread(URL url, HashMap<String, String> param){

        mUrl = url;
        mParam = param;
    }

    @Override
    protected void onPreExecute() {
        /**
         * show dialog
         */
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(Void ... params) {

        /**
         * Do network related stuff
         * return string response.
         */

        JSONObject json = new JSONObject();
        try {
            HttpURLConnection httpCo = (HttpURLConnection) mUrl.openConnection();
            httpCo.setDoOutput(true);

            httpCo.connect();

            OutputStreamWriter wr = new OutputStreamWriter(httpCo.getOutputStream());
            for (Map.Entry<String, String> entry : mParam.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                wr.write(key + "=" + value);
            }
            wr.flush();

            String response = "";

            if (httpCo.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpCo.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "Erreur";
            }

            json = new JSONObject(response);

            System.out.println(json.getString("status"));

            httpCo.disconnect();
        } catch (Throwable e) {
            System.out.println(e.toString());
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        /**
         * update ui thread and remove dialog
         */
        super.onPostExecute(result);
        delegate.processFinish(result);
    }
}