package cop290.cmsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewComplaintActivity extends AppCompatActivity {

    private List<ComplaintType> complaintTypes = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();
    private List<String> studentNames = new ArrayList<>();
    private Map<String, Integer> studentIDs = new HashMap<>();

    private Spinner levelSpinner;
    private Spinner categorySpinner;

    private MultiAutoCompleteTextView AdminUsersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);

        levelSpinner = (Spinner) findViewById(R.id.level_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Complaint_Levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryList));

        getAllComplaintType();

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCategoryList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ComplaintTypeID = 0;
                for (int i = 0; i < complaintTypes.size(); i++)
                    if (complaintTypes.get(i).Level.toLowerCase().equals(levelSpinner.getSelectedItem().toString().toLowerCase()) && complaintTypes.get(i).Category.toLowerCase().equals(categorySpinner.getSelectedItem().toString().toLowerCase())) {
                        ComplaintTypeID = complaintTypes.get(i).ID;
                        break;
                    }

                String Title = ((EditText) findViewById(R.id.complaint_title)).getText().toString();
                String Details = ((EditText) findViewById(R.id.complaint_details)).getText().toString();

                List<Integer> AdminUsers = new ArrayList<>();
                String AdminUserNames[] = AdminUsersTextView.getText().toString().split(", ");
                for (int i=0; i<AdminUserNames.length; i++)
                    AdminUsers.add(studentIDs.get(AdminUserNames[i]));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject("{complaint: {complaint_type_id: " + ComplaintTypeID + ", admin_users: " + AdminUsers.toString() + ", title:\"" + Title + "\", details: \"" + Details + "\"}}");
                } catch (JSONException e) {
                    jsonObject = new JSONObject();
                    Log.d("JsonException", e.getMessage());
                }
                Networking.postRequest(2, jsonObject, new Networking.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        complaintPosted();
                    }
                });
            }
        });

        // Multi Auto Complete Text View
        AdminUsersTextView = (MultiAutoCompleteTextView) findViewById(R.id.AdminUsers);
        AdminUsersTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        AdminUsersTextView.setThreshold(1);
        AdminUsersTextView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, studentNames));

        loadStudentUsers();
    }

    private void loadStudentUsers(){
        Networking.getRequest(6, new String[0], new Networking.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    studentIDs.clear();
                    studentNames.clear();
                    JSONArray response = new JSONArray(result);
                    for (int i=0; i<response.length(); i++){
                        JSONObject user = response.getJSONObject(i);
                        if (user.getInt("user_type_id") == 2) {
                            studentNames.add(user.getString("name"));
                            studentIDs.put(user.getString("name"), user.getInt("id"));
                        }
                    }
                    ((ArrayAdapter) AdminUsersTextView.getAdapter()).notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                }
            }
        });
    }

    private void complaintPosted(){
        Toast.makeText(this, "Complaint Posted", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getAllComplaintType(){
        Networking.getRequest(3, new String[0], new Networking.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray response = new JSONArray(result);
                    for(int i=0; i<response.length(); i++)
                        complaintTypes.add(new ComplaintType(response.getString(i)));
                    setCategoryList();
                } catch (JSONException e) {
                    Log.d("JsonException",e.getMessage());
                }
            }
        });
    }

    private void setCategoryList(){
        categoryList.clear();
        String level = levelSpinner.getSelectedItem().toString().toLowerCase();
        for (int i=0; i<complaintTypes.size(); i++)
            if (complaintTypes.get(i).Level.equals(level))
                categoryList.add(complaintTypes.get(i).Category);
        ((ArrayAdapter) categorySpinner.getAdapter()).notifyDataSetChanged();
    }

    public static class ComplaintType{
        int ID;
        String Level;
        String Category;

        // Constructor parses JSON string and stores data in object
        public ComplaintType (String JsonString){
            try {
                JSONObject complaint = new JSONObject(JsonString);
                ID = complaint.getInt("id");
                Level = complaint.getString("level");
                Category = complaint.getString("type_name");
            } catch (JSONException e) {
                Log.d("JSON Exception : ", e.getMessage());
            }
        }
    }

}
