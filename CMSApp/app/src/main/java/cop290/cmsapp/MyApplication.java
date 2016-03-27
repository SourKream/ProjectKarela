package cop290.cmsapp;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

public class MyApplication extends Application{

    // Volley Queue Global throughout the app
    public static RequestQueue mRequestQueue;

    // Info of the user to check login
    private User MyUser = null;

    public void onCreate() {
        super.onCreate();

        // Instantiating Global Volley Queue
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        // Instantiating Cookie Handler
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
    }

    public boolean isUserLoggedIn(){
        return MyUser!=null;
    }
    public void setMyUser (User user){
        MyUser = user;
    }
    public User getMyUser() {return MyUser;}

    public static class User {
        int ID;
        String Name;
        int UserType;
        String UserTypeName;
        String Group;
        String ContactNo;

        public User (String JsonString){
            try {
                JSONObject user = new JSONObject(JsonString);
                ID = user.getInt("id");
                Name = user.getString("name");
                UserType = user.getInt("user_type_id");
                UserTypeName = user.getString("user_type");
                Group = user.getString("group");
                ContactNo = user.getString("contact_no");
            } catch (JSONException e) {
                Log.d("JSON Exception : ", e.getMessage());
            }
        }
    }
}
