package cop290.cmsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import cop290.cmsapp.ComplaintListFragment.Complaint;

public class ComplaintActivity extends AppCompatActivity {

    // Data for the current activity
    private Complaint complaint;
    private List<Comment> commentList = new ArrayList<>();
    private Comment MyComment;

    // Defining various buttons and views
    private ImageView AddCommentButton;
    private TextView EditCommentButton;
    private RelativeLayout NewCommentView;
    private LinearLayout MyCommentView;
    private TextView MyNameView;
    private TextView MyCommentTextView;
    private EditText NewCommentEditText;
    private LinearLayout MarkResovedButton;
    private CardView PokeButton;
    private TextView EditComplaintButton;
    private ImageView UpvoteButton;
    private ImageView DownvoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        // Get complaint ID from Intent
        loadComplaint(getIntent().getIntExtra("ComplaintID", 0));

        // Load all buttons and views
        NewCommentView = (RelativeLayout) findViewById(R.id.NewCommentView);
        MyCommentView = (LinearLayout) findViewById(R.id.MyCommentTextView);
        MyNameView = (TextView) findViewById(R.id.MyName);
        MyCommentTextView = (TextView) findViewById(R.id.MyComment);
        NewCommentEditText = (EditText) findViewById(R.id.NewCommentEditText);
        MarkResovedButton = (LinearLayout) findViewById(R.id.MarkResolvedButton);

        // Set click listener for adding comment
        AddCommentButton = (ImageView) findViewById(R.id.NewCommentButton);
        AddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewCommentView.setVisibility(View.GONE);
                MyCommentView.setVisibility(View.VISIBLE);

                if (MyComment==null)
                    MyComment = new Comment(NewCommentEditText.getText().toString(),((MyApplication) getApplication()).getMyUser().Name);
                else {
                    MyComment.comment = NewCommentEditText.getText().toString();
                    MyComment.setCommenterName("You");
                }

                MyNameView.setText(MyComment.commenterName);
                MyCommentTextView.setText(MyComment.comment);

                // Send network request for posting new comment
                JSONObject args1 = new JSONObject();
                try {
                    args1 = new JSONObject("{vote: {comment: \"" + MyComment.comment + "\"}}");
                } catch (JSONException e) {
                    Log.d("JSON Exception : ", e.getMessage());
                }
                String args2[] = {Integer.toString(complaint.ID)};
                Networking.postRequest(8, args2, args1, new Networking.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getBaseContext(), "Comment Posted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        EditCommentButton = (TextView) findViewById(R.id.EditCommentButton);
        EditCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCommentView.setVisibility(View.GONE);
                NewCommentView.setVisibility(View.VISIBLE);
                NewCommentEditText.setText(MyComment.comment);
            }
        });

        UpvoteButton = (ImageView) findViewById(R.id.upvoteButton);
        UpvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyComment == null || MyComment.VoteType != -1) {
                    if (MyComment == null)
                        MyComment = new Comment("", "");

                    if (MyComment.VoteType == 1) {
                        MyComment.VoteType = 0;
                        complaint.Upvotes = complaint.Upvotes - 1;
                        UpvoteButton.setAlpha((float)0.4);
                    } else {
                        MyComment.VoteType = 1;
                        complaint.Upvotes = complaint.Upvotes + 1;
                        UpvoteButton.setAlpha((float)1);
                    }
                    updateVotesDisplayed();
                    sendVoteToServer();
                }
            }
        });

        DownvoteButton = (ImageView) findViewById(R.id.downvoteButton);
        DownvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyComment == null || MyComment.VoteType != 1) {
                    if (MyComment == null)
                        MyComment = new Comment("", "");

                    if (MyComment.VoteType == -1) {
                        MyComment.VoteType = 0;
                        complaint.Downvotes = complaint.Downvotes - 1;
                        DownvoteButton.setAlpha((float)0.4);
                    } else {
                        MyComment.VoteType = -1;
                        complaint.Downvotes = complaint.Downvotes + 1;
                        DownvoteButton.setAlpha((float)1);
                    }
                    updateVotesDisplayed();
                    sendVoteToServer();
                }
            }
        });

        PokeButton = (CardView) findViewById(R.id.poke_button);
        PokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String args[] = {Integer.toString(complaint.ID)};
                Networking.getRequest(12, args, new Networking.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        PokeButton.setVisibility(View.GONE);
                    }
                });
            }
        });

        MarkResovedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String args[] = {Integer.toString(complaint.ID)};
                Networking.getRequest(11, args, new Networking.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        complaint.isResolved = (!complaint.isResolved);
                        markedResolved();
                    }
                });
            }
        });

        EditComplaintButton = (TextView) findViewById(R.id.EditComplaintButton);
        EditComplaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getBaseContext(), NewComplaintActivity.class);
                intent.putExtra("EditMode", true);
                intent.putExtra("ComplaintID", complaint.ID);
                startActivity(intent);
            }
        });
    }

    // Send network request to update vote on server
    private void sendVoteToServer(){
        JSONObject args1 = new JSONObject();
        try {
            args1 = new JSONObject("{vote: {vote_type: " + MyComment.VoteType + "}}");
        } catch (JSONException e) {
            Log.d("JSON Exception : ", e.getMessage());
        }
        String args2[] = {Integer.toString(complaint.ID)};
        Networking.postRequest(9, args2, args1, new Networking.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getBaseContext(), "Vote Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Change attributes for showing complaint as resolved
    private void markedResolved(){
        Toast.makeText(getBaseContext(), (complaint.isResolved)?"Marked Resolved":"Marked Unresolved", Toast.LENGTH_SHORT).show();
        ImageView resolved_tick = (ImageView) findViewById(R.id.resolved_tick);
        TextView resolved_text = (TextView) findViewById(R.id.resolved_text);
        if (complaint.isResolved){
            resolved_tick.setImageResource(R.drawable.ic_done_green_24dp);
            resolved_text.setTextColor(getResources().getColor(R.color.emerald));
            resolved_text.setText("Resolved");
        } else {
            resolved_tick.setImageResource(R.drawable.ic_done_black_24dp);
            resolved_text.setTextColor(getResources().getColor(R.color.textColour));
            resolved_text.setText("Unresolved");
        }
    }

    private void updateVotesDisplayed(){
        ((TextView) findViewById(R.id.numUpvotes)).setText(Integer.toString(complaint.Upvotes));
        ((TextView) findViewById(R.id.numDownvotes)).setText(Integer.toString(complaint.Downvotes));
    }

    private void loadComplaint(Integer complaintID){
        String args[] = {Integer.toString(complaintID)};
        Networking.getRequest(7, args, new Networking.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                complaint = new Complaint(result, true);

                try {
                    Integer MyUserID = ((MyApplication) getApplication()).getMyUser().ID;
                    JSONObject response = new JSONObject(result);
                    JSONArray commentsJSON = response.getJSONArray("comments");
                    JSONArray commentersJSON = response.getJSONArray("commenter_names");
                    for (int i = 0; i < commentsJSON.length(); i++) {
                        Comment comment = new Comment(commentsJSON.getString(i));
                        comment.setCommenterName(commentersJSON.getString(i));
                        if (comment.userID != MyUserID)
                            commentList.add(comment);
                        else {
                            MyComment = comment;
                            MyComment.setCommenterName("You");
                        }
                    }
                    JSONArray MyCommentJson = response.getJSONArray("user_activity");
                    if (MyCommentJson.length() > 0){
                        MyComment = new Comment(MyCommentJson.getString(0));
                        MyComment.setCommenterName("You");
                    }
                    ((TextView) findViewById(R.id.postingDateTextView)).setText(response.getString("created_at"));
                    String Admins = response.getString("admin_user_names");
                    Admins = Admins.subSequence(1,Admins.length()-1).toString();
                    Admins = Admins.replace("\"", "");
                    Admins = Admins.replace(",", ", ");
                    ((TextView) findViewById(R.id.posterTextView)).setText(Admins);

                    if (response.getBoolean("is_pokable"))
                        PokeButton.setVisibility(View.VISIBLE);
                    else
                        PokeButton.setVisibility(View.GONE);
                } catch (JSONException e){
                    Log.d("JSON Exception : ", e.getMessage());
                }
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
        //complaintDetails.setTypeface(MainActivity.MyriadPro);

        LinearLayout commentsView = (LinearLayout) findViewById(R.id.commentsList);
        CommentsListAdapter adapter = new CommentsListAdapter(this, commentList);
        for (int i=0; i<adapter.getCount(); i++)
            commentsView.addView(adapter.getView(i, null, null));

        if ((MyComment != null)&&(MyComment.VoteType!=null)){
            if (MyComment.VoteType == 1)
                UpvoteButton.setAlpha((float)1);
            if (MyComment.VoteType == -1)
                DownvoteButton.setAlpha((float)1);
        }
        updateVotesDisplayed();

        ImageView resolved_tick = (ImageView) findViewById(R.id.resolved_tick);
        TextView resolved_text = (TextView) findViewById(R.id.resolved_text);
        if (complaint.isResolved){
            resolved_tick.setImageResource(R.drawable.ic_done_green_24dp);
            resolved_text.setTextColor(getResources().getColor(R.color.emerald));
            resolved_text.setText("Resolved");
        } else {
            resolved_tick.setImageResource(R.drawable.ic_done_black_24dp);
            resolved_text.setTextColor(getResources().getColor(R.color.textColour));
            resolved_text.setText("Unresolved");
        }

        if ((MyComment == null)||(MyComment.comment == null)||(MyComment.comment.equals(""))||(MyComment.comment.equals("null"))){
            NewCommentView.setVisibility(View.VISIBLE);
            MyCommentView.setVisibility(View.GONE);
        } else {
            MyCommentView.setVisibility(View.VISIBLE);
            NewCommentView.setVisibility(View.GONE);
            MyNameView.setText(MyComment.commenterName);
            MyCommentTextView.setText(MyComment.comment);
        }

        if (complaint.ResolvingUsers.contains(((MyApplication) getApplication()).getMyUser().ID)) {
            MarkResovedButton.setClickable(true);
            findViewById(R.id.complaint_is_resolving_user).setVisibility(View.VISIBLE);
        } else {
            MarkResovedButton.setClickable(false);
            findViewById(R.id.complaint_is_resolving_user).setVisibility(View.GONE);
        }

        MyApplication.User MyUser = ((MyApplication) getApplication()).getMyUser();
        Integer MyUserID = MyUser.ID;
        if (complaint.AdminUsers.contains(MyUserID))
            EditComplaintButton.setVisibility(View.VISIBLE);
        else
            EditComplaintButton.setVisibility(View.GONE);

        if (MyUser.UserType!=2){
            UpvoteButton.setClickable(false);
            DownvoteButton.setClickable(false);
        } else {
            UpvoteButton.setClickable(true);
            DownvoteButton.setClickable(true);
        }
    }

    public static class Comment {
        String comment;
        String commenterName = "";
        Integer userID;
        Integer VoteType = 0;

        Comment(String comment, String commenterName) {
            this.comment = comment;
            this.commenterName = commenterName;
        }

        public Comment (String JsonString){
            try {
                JSONObject commentJson = new JSONObject(JsonString);
                comment = commentJson.getString("comment");
                userID = commentJson.getInt("user_id");
                VoteType = commentJson.getInt("vote_type");
            } catch (JSONException e) {
                Log.d("JSON Exception : ", e.getMessage());
            }
        }

        public void setCommenterName (String commenter){
            commenterName = commenter;
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
