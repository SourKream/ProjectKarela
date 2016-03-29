package cop290.cmsapp;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Networking {

    private final static String urlBase = "http://10.192.47.7:3000";

    private final static String extensions[][] = {
            new String[] {"/login.json"},                   //0
            new String[] {"/logout.json"},                  //1
            new String[] {"/complaints.json"},              //2
            new String[] {"/complaint_types.json"},         //3
            new String[] {"/notifs.json"},                  //4
            new String[] {"/users/", ".json"},              //5
            new String[] {"/users.json"},                   //6
            new String[] {"/complaints/",".json"},          //7
            new String[] {"/complaints/","/comment.json"},  //8
            new String[] {"/complaints/","/vote.json"},     //9
            new String[] {"/notifs/","/mark_seen.json"},    //10
            new String[] {"/complaints/","/mark_resolved.json"}, //11
            new String[] {"/complaints/","/poke.json"}      //12
    };

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    // provide list of courses, grades and notifs on login
    public static void getRequest(int extensionCode, String[] optArgs, final VolleyCallback callback){

        // construct appropriate url
        String url = urlBase + extensions[extensionCode][0];

        for (int i=0; i<optArgs.length; i++){
            url = url + optArgs[i] + extensions[extensionCode][i+1];
        }

        Log.d("URL SENT: ", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        MyApplication.mRequestQueue.add(stringRequest);
    }

    public static void postRequest(int extensionCode, String[] optArgs, final Map<String, String> postParams, final VolleyCallback callback){

        String url = urlBase + extensions[extensionCode][0];

        for (int i=0; i<optArgs.length; i++){
            url = url + optArgs[i] + extensions[extensionCode][i+1];
        }

        Log.d("URL SENT: ", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }) {
                    @Override
                    protected Map<String,String> getParams(){
                        return postParams;
                    }
        };

        MyApplication.mRequestQueue.add(stringRequest);
    }

    public static void postRequest(int extensionCode, String[] optArgs, JSONObject postParams, final VolleyCallback callback){

        String url = urlBase + extensions[extensionCode][0];

        for (int i=0; i<optArgs.length; i++){
            url = url + optArgs[i] + extensions[extensionCode][i+1];
        }

        Log.d("URL SENT: ", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.mRequestQueue.add(jsonObjectRequest);
    }

    public static void putRequest(int extensionCode, String[] optArgs, JSONObject postParams, final VolleyCallback callback){

        String url = urlBase + extensions[extensionCode][0];

        for (int i=0; i<optArgs.length; i++){
            url = url + optArgs[i] + extensions[extensionCode][i+1];
        }

        Log.d("URL SENT: ", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.mRequestQueue.add(jsonObjectRequest);
    }
}