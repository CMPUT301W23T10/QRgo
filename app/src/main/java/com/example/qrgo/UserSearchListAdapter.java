package com.example.qrgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.qrgo.models.BasicPlayerProfile;

import java.util.ArrayList;

public class UserSearchListAdapter extends ArrayAdapter<BasicPlayerProfile> {

    public UserSearchListAdapter(Context context, ArrayList<BasicPlayerProfile> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the view for each user in the list
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_user_return, parent, false);
        }

        // Get the user at the current position
        BasicPlayerProfile user = super.getItem(position);

        // Set the user's name to the TextView in the list item layout
        TextView nameTextView = convertView.findViewById(R.id.user_name);


        nameTextView.setText(user.getFirstName() + " " + user.getLastName());

        return convertView;
    }
}

