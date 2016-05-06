package com.example.app_10p5;

import org.json.JSONObject;

/**
 * Created by Jean-loup Beaussart on 30/04/2016.
 */
public interface ASyncResponse {
    void processFinish(JSONObject output);
}
