package com.powervoting.powervoting;

import com.google.firebase.database.Exclude;

/**
 * Created by Dylan on 11/12/2016.
 */

public class Submission {
    private String title;
    private String submitter;
    private int votes;
    private String detail;

    @Exclude
    private String key;

    public Submission() {

    }

    public Submission(String title, String submitter, int votes, String detail) {
        this.title = title;
        this.submitter = submitter;
        this.votes = votes;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
