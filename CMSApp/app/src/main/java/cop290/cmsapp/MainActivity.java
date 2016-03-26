package cop290.cmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cop290.cmsapp.ComplaintListFragment.Complaint;

public class MainActivity extends AppCompatActivity {

    public List<Complaint> complaintListPersonal = new ArrayList<>();
    public List<Complaint> complaintListHostel = new ArrayList<>();
    public List<Complaint> complaintListInstitute = new ArrayList<>();
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewComplaintActivity.class);
                startActivity(intent);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        try {
            tabLayout.getTabAt(0).setText("Personal");
            tabLayout.getTabAt(1).setText("Hostel");
            tabLayout.getTabAt(2).setText("Institute");
        } catch (java.lang.NullPointerException e) {
            Log.d("Null Pointer: ",e.getMessage());
        }

        if (!((MyApplication) getApplication()).isUserLoggedIn()) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Networking.getRequest(2, new String[0], new Networking.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.d("Response",result);
                    JSONArray response = new JSONArray(result);
                    complaintListHostel.clear();
                    complaintListInstitute.clear();
                    complaintListPersonal.clear();
                    for(int i=0; i<response.length(); i++){
                        Complaint complaint = new Complaint(response.getString(i));
                        if (complaint.Level.equals("personal"))
                            complaintListPersonal.add(complaint);
                        else if (complaint.Level.equals("hostel"))
                            complaintListHostel.add(complaint);
                        else if (complaint.Level.equals("institute"))
                            complaintListInstitute.add(complaint);
                    }
                } catch (JSONException e) {
                    Log.d("JsonException",e.getMessage());
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle1 = new Bundle();
        bundle1.putString("group", "personal");
        Bundle bundle2 = new Bundle();
        bundle2.putString("group", "hostel");
        Bundle bundle3 = new Bundle();
        bundle3.putString("group", "institute");

        Fragment personal_fragment = new ComplaintListFragment();
        Fragment hostel_fragment = new ComplaintListFragment();
        Fragment institute_fragment = new ComplaintListFragment();

        personal_fragment.setArguments(bundle1);
        hostel_fragment.setArguments(bundle2);
        institute_fragment.setArguments(bundle3);

        adapter.addFragment(personal_fragment, "PERSONAL");
        adapter.addFragment(hostel_fragment, "HOSEL");
        adapter.addFragment(institute_fragment,"INSTITUTE");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_notifications){
            Intent intent = new Intent(getBaseContext(), NotificationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            ((MyApplication) getApplication()).setMyUser(null);
            Networking.getRequest(1,new String[0], new Networking.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
