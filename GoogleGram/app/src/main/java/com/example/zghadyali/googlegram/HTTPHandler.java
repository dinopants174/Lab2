package com.example.zghadyali.googlegram;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The HTTPHandler class creates a queue using Volley, adds a request when the user searches for
 * images to the queue, and puts the returned images' links in an ArrayList that is then used in a
 * callback function
 */
public class HTTPHandler {
    public RequestQueue queue;

    public HTTPHandler(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void searchGoogleImages(String searchQuery, final Callback callback){
        String search = searchQuery.replaceAll(" ", "+");
        String url = "https://www.googleapis.com/customsearch/v1?";
        String apiKey = "AIzaSyBcyL37rZJjf9L-gq-BFYiIEn_xJnEsfXE";
        String searchEngineID = "006637100029389407945:jgtijyiaby0";
        url = url + "key=" + apiKey + "&cx=" + searchEngineID + "&q=" + search;
        url = url + "&searchType=image" + "&imgSize=medium";    //ensures we only get images that
        //fit into the WebView

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        ArrayList <String> itemLink = new ArrayList<>();
                        try {
                            JSONArray images = response.getJSONArray("items");
                            for (int i=0; i<images.length(); i++){
                                JSONObject image = images.getJSONObject(i);
                                String link = image.getString("link");
                                Log.d("Link: ", link);  //logs the link, not necessary to do
                                itemLink.add(link);
                            }
                            callback.tracksCallback(itemLink);  //provides itemLink ArrayList as
                            //input to callback function
                        } catch (Exception e){
                            Log.e("ERROR: ", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        if (error != null){
                        Log.e("ERROR", error.getMessage());}
                    }
                }
        );
        queue.add(request);
    }
}
