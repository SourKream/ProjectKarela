package cop290.cmsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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


public class ComplaintListFragment extends Fragment {

    // List of courses to populate fragment
    public ListView complaintListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint_list, container, false);

        // Initialise listview by setting adapter and item click listener
        complaintListView = (ListView) view.findViewById(R.id.complaint_list);

        MainActivity MyActivity = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        String group = bundle.getString("group");

        if (group.equals("personal"))
            complaintListView.setAdapter(new ComplaintListAdapter(this.getActivity(), MyActivity.complaintListPersonal));
        else if (group.equals("hostel"))
            complaintListView.setAdapter(new ComplaintListAdapter(this.getActivity(), MyActivity.complaintListHostel));
        else if (group.equals("institute"))
            complaintListView.setAdapter(new ComplaintListAdapter(this.getActivity(), MyActivity.complaintListInstitute));

        complaintListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ComplaintActivity.class);
                intent.putExtra("ComplaintID", ((Complaint)complaintListView.getAdapter().getItem(position)).ID);
                startActivity(intent);
            }
        });
        return view;
    }

    // Class to store details of a course
    public static class Complaint{
        int ID;
        int ComplaintTypeID;
        String Group;
        String Title;
        String Details;
        String Level;
        Integer Upvotes = 0;
        Integer Downvotes = 0;
        List<Integer> ActionUsers = new ArrayList<>();
        List<Integer> AdminUsers = new ArrayList<>();
        List<Integer> ResolvingUsers = new ArrayList<>();

        // Constructor parses JSON string and stores data in object
        public Complaint (String JsonString){
            try {
                JSONObject complaint = new JSONObject(JsonString);
                ID = complaint.getInt("id");
                ComplaintTypeID = complaint.getInt("complaint_type_id");
                Group = complaint.getString("group");
                Title = complaint.getString("title");
                Details = complaint.getString("details");
//                Upvotes = complaint.getInt("upvotes");
//                Downvotes = complaint.getInt("downvotes");
                JSONArray action_users = complaint.getJSONArray("action_users");
                for (int i=0; i<action_users.length(); i++)
                    ActionUsers.add(action_users.getInt(i));
                JSONArray admin_users = complaint.getJSONArray("admin_users");
                for (int i=0; i<admin_users.length(); i++)
                    AdminUsers.add(admin_users.getInt(i));
                JSONArray resolving_users = complaint.getJSONArray("resolving_users");
                for (int i=0; i<resolving_users.length(); i++)
                    ResolvingUsers.add(resolving_users.getInt(i));
                Level = complaint.getString("level");
            } catch (JSONException e) {
                Log.d("JSON Exception : ", e.getMessage());
            }
        }
    }

    // Custom adapter to populate list view
    class ComplaintListAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private List<Complaint> complaintList;

        public ComplaintListAdapter(Activity activity, List<Complaint> complaintList){
            this.activity = activity;
            this.complaintList = complaintList;
        }

        @Override
        public int getCount() {
            return (complaintList == null)?0:complaintList.size();
        }

        @Override
        public Object getItem(int position) {
            return complaintList.get(position);
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
                convertView = inflater.inflate(R.layout.list_item_complaint, null);

            TextView complaintSNo           = (TextView) convertView.findViewById(R.id.SNo);
            TextView complaintTitle         = (TextView) convertView.findViewById(R.id.complaintTitle);
            TextView complaintDescription   = (TextView) convertView.findViewById(R.id.complaintDescription);
            //TextView complaintPostedBy      = (TextView) convertView.findViewById(R.id.complaintPostedBy);

            Complaint complaint = complaintList.get(position);

            complaintSNo.setText(String.format("%d",position+1));
            complaintTitle.setText(complaint.Title);
            complaintTitle.setTypeface(MainActivity.MyriadPro);
            complaintDescription.setText(complaint.Details);
            complaintDescription.setTypeface(MainActivity.Garibaldi);

            return convertView;
        }
    }
}
