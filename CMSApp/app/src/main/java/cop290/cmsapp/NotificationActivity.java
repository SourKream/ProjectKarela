package cop290.cmsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private List<Notification> notifications = new ArrayList<>();
    private ListView notificationsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getAllNotifications();

        notificationsListView = (ListView) findViewById(R.id.NotificationsListView);
        notificationsListView.setAdapter(new NotificationListAdapter(this, notifications));
        notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(getResources().getColor(R.color.backgroudCustom));
                notifications.get(position).markSeen();
                goToComplaint(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mark_all_read){
            for (int i=0; i<notifications.size(); i++)
                notifications.get(i).markSeen();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllNotifications(){
        Networking.getRequest(4, new String[0], new Networking.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.d("Response",result);
                    JSONArray response = new JSONArray(result);
                    notifications.clear();
                    for(int i=0; i<response.length(); i++)
                        notifications.add(new Notification(response.getString(i)));
                    ((NotificationListAdapter) notificationsListView.getAdapter()).notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("JsonException",e.getMessage());
                }
            }
        });
    }

    private void goToComplaint(int position){
        Intent intent = new Intent(this, ComplaintActivity.class);
        intent.putExtra("ComplaintID", notifications.get(position).ComplaintID);
        startActivity(intent);
    }

    // Class to store details of a notification
    public static class Notification{
        Integer NotificationID;
        Integer ComplaintID;
        Boolean isSeen;
        String Details;

        public Notification (String JsonString){
            try {
                JSONObject notification = new JSONObject(JsonString);
                NotificationID = notification.getInt("notification_id");
                isSeen = notification.getBoolean("is_seen");
                ComplaintID = notification.getInt("complaint_id");
                Details = notification.getString("details");
            } catch (JSONException e) {
                Log.d("JSON Exception : ", e.getMessage());
            }
        }

        public void markSeen(){
            isSeen = true;
            String args[] = {Integer.toString(NotificationID)};
            Networking.getRequest(10, args, new Networking.VolleyCallback() {
                @Override
                public void onSuccess(String result) {}
            });
        }
    }

    // Custom adapter to populate list view
    class NotificationListAdapter extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;
        private List<Notification> notificationsList;

        public NotificationListAdapter(Activity activity, List<Notification> list) {
            this.activity = activity;
            this.notificationsList = list;
        }

        @Override
        public int getCount() {
            return notificationsList.size();
        }

        @Override
        public Object getItem(int position) {
            return notificationsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_item_notification, null);

            TextView notificationDescription = (TextView) convertView.findViewById(R.id.notification);
            Notification notification = notificationsList.get(position);

            notificationDescription.setText(notification.Details);

            if (!notificationsList.get(position).isSeen)
                convertView.setBackgroundColor(getResources().getColor(R.color.notifHighlight));

            return convertView;
        }
    }
}
