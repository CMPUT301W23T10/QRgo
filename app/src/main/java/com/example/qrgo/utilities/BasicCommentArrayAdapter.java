package com.example.qrgo.utilities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.R;
import com.example.qrgo.listeners.QRCodeListener;
import com.example.qrgo.models.Comment;
import com.example.qrgo.models.QRCode;
import com.example.qrgo.utilities.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 BasicCommentArrayAdapter is an ArrayAdapter for displaying a list of Comment objects
 in a ListView. The adapter extends the ArrayAdapter class and overrides its getView()
 method to customize how each item in the ListView is displayed.
 */
public class BasicCommentArrayAdapter extends ArrayAdapter<Comment> {

    private Context mContext;
    private int mResource;
    private ArrayList<Comment> mComments;


    /**
     Constructor for the BasicCommentArrayAdapter class.
     @param context the context of the activity or fragment that is using the adapter.
     @param resource the resource ID of the layout file that defines the appearance of each item in the ListView.
     @param comments the list of Comment objects to be displayed in the ListView.
     */
    public BasicCommentArrayAdapter(Context context, int resource, ArrayList<Comment> comments) {
        super(context, resource, comments);
        this.mContext = context;
        this.mResource = resource;
        this.mComments = comments;
    }

    /**
     Overrides the getView() method of the ArrayAdapter class to customize the appearance of each item in the ListView.
     @param position the position of the item in the ListView.
     @param convertView the recycled view to populate.
     @param parent the parent ViewGroup that this view will eventually be attached to.
     @return the View for the item at the specified position.
     */
    FirebaseConnect db = new FirebaseConnect();
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = mComments.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView commentNameTextView = convertView.findViewById(R.id.commentor_name);
        TextView commentTimeTextView = convertView.findViewById(R.id.commented_time);
        TextView commentBodyTimeTextView = convertView.findViewById(R.id.comment_body);
        TextView commentedOnTextView = convertView.findViewById(R.id.commented_on);
        ImageView commentsProfilePictureImageView = convertView.findViewById(R.id.comment_profile_picture);

        // Calculate the time since the comment was posted
        long currentTime = System.currentTimeMillis();
        long dateMs = comment.getDate().getTime();
        long diffMs = currentTime - dateMs;
        int diffHours = (int) (diffMs / 3600000);
        // If the comment was posted more than 24 hours ago, display the number of days
        if (diffHours >= 24) {
            int diffDays = diffHours / 24;
            commentTimeTextView.setText(diffDays + " days ago");
            // If the comment was posted less than 24 hours ago, display the number of hours
        } else {
            commentTimeTextView.setText(diffHours + " h ago");
        }

        commentNameTextView.setText(comment.getPlayerFirstName() + ' ' + comment.getPlayerLastName());

        commentBodyTimeTextView.setText(comment.getCommentString());
        db.getQRCodeManager().getQRCode(comment.getQrCodeId(), new QRCodeListener() {
            @Override
            public void onQRCodeRetrieved(QRCode qrCode) {
                String name = qrCode.getHumanReadableQR();
                // Truncate the commented on string to 18 characters
                if (name.length() > 18) {
                    name = name.substring(0, 18) + "...";
                }
                commentedOnTextView.setText(name);
            }

            @Override
            public void onQRCodeNotFound() {
                Toast.makeText(mContext, "QR Code not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQRCodeRetrievalFailure(Exception e) {
                Toast.makeText(mContext, "QR Code retrieval failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Load user image into the ImageView
        ImageViewController imageViewController = new ImageViewController();
        imageViewController.setImage(comment.getPlayerFirstName(),commentsProfilePictureImageView);

        return convertView;
    }
}

