
package com.example.qrgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrgo.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BasicCommentArrayAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mResource;
    private ArrayList<String> mHoursAgo;
    private ArrayList<String> mCommentBody;
    private ArrayList<String> mCommentedOn;

    public BasicCommentArrayAdapter(Context context, int resource, ArrayList<String> hoursAgo, ArrayList<String> commentBody, ArrayList<String> commentedOn) {
        super(context, resource, hoursAgo);
        this.mContext = context;
        this.mResource = resource;
        this.mHoursAgo = hoursAgo;
        this.mCommentBody = commentBody;
        this.mCommentedOn = commentedOn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = "John";
        String hoursAgo = mHoursAgo.get(position);
        String commentBody = mCommentBody.get(position);
        String commentedOn = mCommentedOn.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView commentNameTextView = convertView.findViewById(R.id.commentor_name);
        TextView commentTimeTextView = convertView.findViewById(R.id.commented_time);
        TextView commentBodyTimeTextView = convertView.findViewById(R.id.comment_body);
        TextView commentedOnTextView = convertView.findViewById(R.id.commented_on);
        ImageView commentsProfilePictureImageView = convertView.findViewById(R.id.comment_profile_picture);

        commentNameTextView.setText(name);
        commentTimeTextView.setText(hoursAgo);
        commentBodyTimeTextView.setText(commentBody);
        commentedOnTextView.setText(commentedOn);

        Picasso.get().load("https://i.imgur.com/DvpvklR.png").transform(new CircleTransform()).into(commentsProfilePictureImageView);

        return convertView;
    }
}
