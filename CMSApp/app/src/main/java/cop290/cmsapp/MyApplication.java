package cop290.cmsapp;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

public class MyApplication extends Application{

    // Volley Queue Global throughout the app
    public static RequestQueue mRequestQueue;

    public void onCreate() {
        super.onCreate();

        // Instantiating Global Volley Queue
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        // Instantiating Cookie Handler
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
    }
}
