package cop290.cmsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import cop290.cmsapp.ComplaintListFragment.Complaint;

public class ComplaintActivity extends AppCompatActivity {

    private Complaint complaint;
    private List<Comment> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        loadComplaint(getIntent().getIntExtra("ComplaintID", 0));

        LinearLayout commentsView = (LinearLayout) findViewById(R.id.commentsList);

        commentList.add(new Comment("This is the first comment", "Ajay"));
        commentList.add(new Comment("Hey! I can comment too", "Sonakshi"));

        CommentsListAdapter adapter = new CommentsListAdapter(this, commentList);
        for (int i=0; i<adapter.getCount(); i++){
            commentsView.addView(adapter.getView(i, null, null));
        }
    }

    private void loadComplaint(Integer complaintID){
        String args[] = {Integer.toString(complaintID)};
        Networking.getRequest(7, args, new Networking.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                complaint = new Complaint(result);
                displayComplaint();
            }
        });
    }

    private void displayComplaint(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(complaint.Title);
        setSupportActionBar(toolbar);

        TextView complaintDetails = (TextView) findViewById(R.id.complaint_details);
        complaintDetails.setText(complaint.Details);
    }

    public static class Comment {
        String comment;
        String commenterName;

        Comment(String comment, String commenterName) {
            this.comment = comment;
            this.commenterName = commenterName;
        }
    }

    class CommentsListAdapter extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater;
        private List<Comment> commentsList;

        public CommentsListAdapter(Activity activity, List<Comment> list) {
            this.activity = activity;
            this.commentsList = list;
        }

        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentsList.get(position);
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
                convertView = inflater.inflate(R.layout.list_item_comment, null);

            TextView comment = (TextView) convertView.findViewById(R.id.comment);
            TextView commenterName = (TextView) convertView.findViewById(R.id.commenter);
            Comment commentItem = commentsList.get(position);
            commenterName.setText(commentItem.commenterName);
            comment.setText(commentItem.comment);

            return convertView;
        }
    }
}
