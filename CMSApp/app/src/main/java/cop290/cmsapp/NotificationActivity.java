package cop290.cmsapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private List<Notification> notifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ListView notificationsListView = (ListView) findViewById(R.id.NotificationsListView);
        notificationsListView.setAdapter(new NotificationListAdapter(this, notifications));
        notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO : Set links to required activities
/*                view.setBackgroundColor(getResources().getColor(R.color.backgroundCustom));
                notifications.get(position).Seen();
                // Go to the required threads activity on item click
                String description = notifications.get(position).description;
                Log.d("ThreadID : ", description);
                description = description.substring(description.indexOf("/threads/thread/") + 16, description.indexOf("'>thread"));
                Integer ThreadID = Integer.parseInt(description);
                goToThread(ThreadID);
*/
            }
        });
    }

    // Class to store details of a notification
    public static class Notification{
        //TODO : Give own definition
        String createdAt;
        String description;
        Integer ID;
        Integer isSeen;
        Integer UserID;

        public Notification (String JsonString){
            try {
                JSONObject notification = new JSONObject(JsonString);
                ID = notification.getInt("id");
                isSeen = notification.getInt("is_seen");
                UserID = notification.getInt("user_id");
                createdAt = notification.getString("created_at");
                description = notification.getString("description");
            } catch (JSONException e) {
                Log.d("JSON Exception : ", e.getMessage());
            }
        }

        public void Seen(){isSeen = 1;}
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
            // TODO : Return custom list item view
/*            if (convertView == null)
                convertView = inflater.inflate(R.layout.notifications_list_item, null);

            TextView notificationDescription = (TextView) convertView.findViewById(R.id.notification);

            Notification notification = notificationsList.get(position);

            notificationDescription.setText(Html.fromHtml(notification.description).toString());
            if (notificationsList.get(position).isSeen == 0)
                convertView.setBackgroundColor(getResources().getColor(R.color.highlightColor));
*/
            return convertView;
        }
    }
}
