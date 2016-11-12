package com.powervoting.powervoting;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private SubmissionsListAdapter adapter;
    @BindView(R.id.submissions_lv)
    ListView submissionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultValues();
        queryForSubmissions();
    }

    public void defaultValues() {
        ButterKnife.bind(this);
        ref = FirebaseDatabase.getInstance().getReference();
        adapter = new SubmissionsListAdapter(getApplicationContext());
        submissionsView.setAdapter(adapter);
    }

    public void queryForSubmissions() {
        Calendar current = Calendar.getInstance();
        Query query = ref.child("day").child("" + current.get(Calendar.YEAR) + "-" +
                current.get(Calendar.MONTH) + "-" + current.get(Calendar.DAY_OF_MONTH));

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Submission submission = dataSnapshot.getValue(Submission.class);
                submission.setKey(dataSnapshot.getKey());

                adapter.addSubmission(submission);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @OnLongClick(R.id.submissions_lv)
    public void vote(int position) {
        Calendar current = Calendar.getInstance();
        Query query = ref.child("day").child("" + current.get(Calendar.YEAR) + "-" +
                current.get(Calendar.MONTH) + "-" + current.get(Calendar.DAY_OF_MONTH));
        Submission submission = (Submission) adapter.getItem(position);

        DatabaseReference incrementRef = ref.child("day").child("" + current.get(Calendar.YEAR) + "-" +
                current.get(Calendar.MONTH) + "-" + current.get(Calendar.DAY_OF_MONTH));

        incrementRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Submission incremenetSubmission = mutableData.getValue()
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    @OnClick(R.id.add_fab)
    public void onClick() {
        CreateSubmissionDialog dialog = new CreateSubmissionDialog();
        dialog.show(getSupportFragmentManager(), null);
    }

    public static class SubmissionsListAdapter extends BaseAdapter {
        private List<Submission> list = new ArrayList<>();
        private Context context;

        public SubmissionsListAdapter(Context context) {
            this.context = context;
        }

        public void addSubmission(Submission submission) {
            list.add(submission);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).hashCode();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;

            if (view != null) {
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.row_submission, parent, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }

            Submission submission = (Submission) getItem(position);

            viewHolder.title.setText(submission.getTitle());
            viewHolder.description.setText(submission.getDetail());
            viewHolder.votes.setText("" + submission.getVotes());

            return view;
        }

        static class ViewHolder {
            @BindView(R.id.votes_tv)
            TextView votes;
            @BindView(R.id.description_tv)
            TextView description;
            @BindView(R.id.title_tv)
            TextView title;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
