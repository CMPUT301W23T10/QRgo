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

public class SearchArrayAdapter extends ArrayAdapter<BasicPlayerProfile> {

    private Context mContext;
    private ArrayList<BasicPlayerProfile> mUserList;

    /**
     * Constructor for BasicUserArrayAdapter.
     *
     * @param context  the context in which the adapter is used
     * @param userList an ArrayList of BasicPlayerProfile objects to be displayed in the ListView
     */
    public SearchArrayAdapter(Context context, ArrayList<BasicPlayerProfile> userList) {
        super(context, 0, userList);
        mContext = context;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_item, parent, false);
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
        TextView nameTextView = convertView.findViewById(R.id.search_user_name);
        ImageView userIcon = convertView.findViewById(R.id.search_user_image);

        // Load user image into the ImageView
        ImageViewController imageViewController = new ImageViewController();
        imageViewController.setImage(currentUser.getFirstName(),userIcon);

        // Set the text for the views
        nameTextView.setText(currentUser.getUsername());
        return convertView;
    }
}

