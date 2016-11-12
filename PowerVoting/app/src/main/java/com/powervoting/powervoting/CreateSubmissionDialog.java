package com.powervoting.powervoting;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dylan on 11/12/2016.
 */

public class CreateSubmissionDialog extends DialogFragment {
    private View view;
    private DatabaseReference ref;

    @BindView(R.id.title_et)
    EditText title;

    @BindView(R.id.name_et)
    EditText name;

    @BindView(R.id.description_et)
    EditText descriptoin;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.create_submission_dialog, null, false);
        builder.setView(view);
        ButterKnife.bind(this, view);

        ref = FirebaseDatabase.getInstance().getReference();

        return builder.create();
    }

    @OnClick(R.id.done_iv)
    public void onClick() {
        Submission submission = new Submission(title.getText().toString(), name.getText()
                .toString(), 0, descriptoin.getText().toString());

        Calendar current = Calendar.getInstance();

        DatabaseReference newRef = ref.child("day").child("" + current.get(Calendar.YEAR) + "-" +
                current.get(Calendar.MONTH) + "-" + current.get(Calendar.DAY_OF_MONTH)).push();

        newRef.setValue(submission, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                dismiss();
            }
        });
    }
}
