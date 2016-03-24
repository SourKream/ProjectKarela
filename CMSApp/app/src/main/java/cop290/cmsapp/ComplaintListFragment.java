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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ComplaintListFragment extends Fragment {

    // List of courses to populate fragment
    private List<Complaint> complaintList = new ArrayList<>();
    private ListView complaintListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint_list, container, false);

        populateCourseList();
        // Initialise listview by setting adapter and item click listener
        complaintListView = (ListView) view.findViewById(R.id.complaint_list);
        complaintListView.setAdapter(new ComplaintListAdapter(this.getActivity(), complaintList));
        complaintListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Go to the specific course activity when the course item is clicked
                // Pass coursecode in Intent to populate next activity with the required data
                Intent intent = new Intent(getActivity(), ComplaintActivity.class);
                // TODO: Correct argument
                intent.putExtra("coursename", complaintList.get(position).CourseCode);
                startActivity(intent);
            }
        });
        return view;
    }

    public void populateCourseList(){
        // TODO

    }

    // Class to store details of a course
    public static class Complaint{
        // TODO: Correct parameters
        int ID;
        int Credits;
        String CourseCode;
        String CourseName;
        String CourseDescription;
        String LTP;

        // Constructor parses JSON string and stores data in object
        public Complaint (String JsonString){
            try {
                JSONObject course = new JSONObject(JsonString);
                ID = course.getInt("id");
                Credits = course.getInt("credits");
                CourseName = course.getString("name");
                CourseCode = course.getString("code");
                CourseDescription = course.getString("description");
                LTP = course.getString("l_t_p");
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
            return complaintList.size();
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
//            if (convertView == null)
//                convertView = inflater.inflate(R.layout.courses_list_item, null);
/*
            TextView courseCode = (TextView) convertView.findViewById(R.id.courseCode);
            TextView courseName = (TextView) convertView.findViewById(R.id.courseName);
            TextView LTP = (TextView) convertView.findViewById(R.id.ltp);

            Course course = courseList.get(position);

            courseCode.setText(course.CourseCode.toUpperCase());
            courseCode.setTypeface(MainActivity.MyriadPro);
            courseName.setText(course.CourseName);
            courseName.setTypeface(MainActivity.Garibaldi);
            LTP.setText(String.format("(%s)",course.LTP));
            LTP.setTypeface(MainActivity.MyriadPro);
*/
            return convertView;
        }
    }
}
