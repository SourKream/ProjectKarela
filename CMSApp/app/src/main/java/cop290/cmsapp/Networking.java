package cop290.cmsapp;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;

//TODO
public class Networking {

    private final static String urlBase = "http://192.168.0.7:8000";

    private final static String extensions[][] = {                                        //   optional inputs
            new String[] {"/default/login.json?userid=","&password=",""},                 //0
            new String[] {"/default/logout.json"},                                        //1
            new String[] {"/courses/list.json"},                                          //2
            new String[] {"/default/notifications.json"},                                 //3
            new String[] {"/default/grades.json"},                                        //4
            new String[] {"/courses/course.json/","/assignments"},                        //5- course code
            new String[] {"/courses/assignment.json/",""},                                //6- assignment #
            new String[] {"/courses/course.json/","/grades"},                             //7- course code
            new String[] {"/courses/course.json/","/threads"},                            //8- course code
            new String[] {"/threads/thread.json/",""},                                    //9- thread #
            new String[] {"/threads/new.json?title=","&description=","&course_code=",""}, //10
            new String[] {"/threads/post_comment.json?thread_id=","&description=",""},    //11
            new String[] {"/users/user.json/",""}                                         //12
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
            public void onErrorResponse(VolleyError error) {
            }});

//        MyApplication.mRequestQueue.add(stringRequest);
    }

}