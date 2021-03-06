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
 * Created by Jean-loup Beaussart on 30/04/2016.
 */
class NetworkThread extends AsyncTask<Void, Void, JSONObject> {
    public ASyncResponse delegate = null;
    private final URL mUrl;
    private final HashMap<String, String> mParam;

    NetworkThread(URL url, HashMap<String, String> param){

        mUrl = url;
        mParam = param;
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

            httpCo.setConnectTimeout(20000);
            httpCo.setReadTimeout(20000);

            httpCo.connect();

            boolean debut = true;
            StringBuilder buffer = new StringBuilder();

            for (Map.Entry<String, String> entry : mParam.entrySet()) {
                if(debut){
                    debut = false;
                }
                else
                {
                    buffer.append("&");
                }
                buffer.append(entry.getKey());
                buffer.append("=");
                buffer.append(entry.getValue());
            }

            OutputStreamWriter wr = new OutputStreamWriter(httpCo.getOutputStream());
            wr.write(buffer.toString());
            wr.flush();

            String response = "";

            if (httpCo.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpCo.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "{\"status\":" + httpCo.getResponseCode() + "}";
            }

            json = new JSONObject(response);

            httpCo.disconnect();
        } catch (Throwable t) {
            System.out.println("Exception: " + t.toString());
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