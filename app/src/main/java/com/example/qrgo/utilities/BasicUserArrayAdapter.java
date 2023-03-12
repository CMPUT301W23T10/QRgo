package com.example.qrgo.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrgo.R;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.BasicQRCode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BasicUserArrayAdapter extends ArrayAdapter<BasicPlayerProfile> {

    private Context mContext;
    private ArrayList<BasicPlayerProfile> mUserList;

    public BasicUserArrayAdapter(Context context, ArrayList<BasicPlayerProfile> userList) {
        super(context, 0, userList);
        mContext = context;
        mUserList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the current item from the data array
        BasicPlayerProfile currentUser = getItem(position);

        // Inflate the list item layout if necessary
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_items, parent, false);
        }

        // Get references to the views in the list item layout
        TextView nameTextView = convertView.findViewById(R.id.user_name);
        TextView scoreTextView = convertView.findViewById(R.id.user_score);
        ImageView user_icon = convertView.findViewById(R.id.user_image);

        Picasso.get()
                .load(R.drawable.demo_picture)
                .transform(new CircleTransform())
                .into(user_icon);

        // Set the text for the views
        nameTextView.setText(currentUser.getFirstName());
        scoreTextView.setText(currentUser.getTotalScore() + " pts");

        return convertView;
    }
}
