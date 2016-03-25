package cop290.cmsapp;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

//TODO
public class Networking {

    private final static String urlBase = "http://10.0.2.2:3000";

    private final static String extensions[][] = {                                        //   optional inputs
            new String[] {"/login.json?"},                 //0
    };

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    // provide list of courses, grades and notifs on login
    public static void getData(int extensionCode, String[] optArgs, final VolleyCallback callback){

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

    public static void postData(int extensionCode, final Map<String, String> postParams, final VolleyCallback callback){

        String url = urlBase + extensions[extensionCode][0];

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
}