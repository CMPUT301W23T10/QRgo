package com.example.qrgo.utilities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrgo.PlayerActivity;
import com.example.qrgo.R;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.BasicQRCode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This class extends ArrayAdapter to provide a customized adapter for a ListView displaying BasicPlayerProfile objects.
 */
public class BasicUserArrayAdapter extends ArrayAdapter<BasicPlayerProfile> {

    private Context mContext;
    private ArrayList<BasicPlayerProfile> mUserList;
    private String field;

    /**
     * Constructor for BasicUserArrayAdapter.
     *
     * @param context  the context in which the adapter is used
     * @param userList an ArrayList of BasicPlayerProfile objects to be displayed in the ListView
     * @param field    the field for which the list is being displayed for.
     */
    public BasicUserArrayAdapter(Context context, ArrayList<BasicPlayerProfile> userList, String field) {
        super(context, 0, userList);
        mContext = context;
        this.field = field;
        mUserList = userList;
    }

    /**
     * This method creates a View for a ListView item at the given position.
     *
     * @param position    the position of the item in the ListView
     * @param convertView the old view to reuse, if possible
     * @param parent      the parent ViewGroup
     * @return the View for the item at the given position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the current item from the data array
        BasicPlayerProfile currentUser = getItem(position);

        // Inflate the list item layout if necessary
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_items, parent, false);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                // Put the username in the intent
                intent.putExtra("username", currentUser.getUsername());
                view.getContext().startActivity(intent);
            }

        });

        // Get references to the views in the list item layout
        TextView nameTextView = convertView.findViewById(R.id.user_name);
        TextView scoreTextView = convertView.findViewById(R.id.user_score);
        ImageView user_icon = convertView.findViewById(R.id.user_image);
        // Set images
        ImageViewController imageViewController = new ImageViewController();
        imageViewController.setImage(currentUser.getFirstName(),user_icon);


        // Set the text for the views
        nameTextView.setText(currentUser.getUsername());
        if (field == "totalScore") {
            scoreTextView.setText(currentUser.getTotalScore() + " pts");
        } else if (field == "highScore") {
            scoreTextView.setText(currentUser.getHighestScore() + " pts");
        } else if (field == "totalScans") {
        scoreTextView.setText(currentUser.getTotalScans() + " scans");
    }
        return convertView;
    }
}

