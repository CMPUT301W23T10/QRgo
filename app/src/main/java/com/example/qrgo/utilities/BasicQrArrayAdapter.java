package com.example.qrgo.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.HomeActivity;
import com.example.qrgo.QrProfileActivity;
import com.example.qrgo.R;
import com.example.qrgo.listeners.OnUserDeleteFromQRCodeListener;
import com.example.qrgo.models.BasicQRCode;

import java.util.ArrayList;

/**
 * A custom ArrayAdapter class for displaying BasicQRCode objects in a ListView
 */
public class BasicQrArrayAdapter extends ArrayAdapter<BasicQRCode> {
    private Context mContext;
    private ArrayList<BasicQRCode> mQRList;

    private String caller = "";

    /**
     * Constructor for BasicQrArrayAdapter
     *
     * @param context the context in which the adapter is created
     * @param qrList  the list of BasicQRCode objects to be displayed in the ListView
     * @param caller  the caller of the adapter (player, homeAll, etc.)
     */
    public BasicQrArrayAdapter(Context context, ArrayList<BasicQRCode> qrList, String caller) {
        super(context, 0, qrList);
        mContext = context;
        mQRList = qrList;
        this.caller = caller;
    }

    /**
     * Returns the view for a specific position in the ListView
     *
     * @param position    the position of the item in the ListView
     * @param convertView the view to be converted or inflated
     * @param parent      the parent ViewGroup of the view
     * @return the view for the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the current item from the data array
        BasicQRCode currentQRCode = getItem(position);
        // Inflate the list item layout if necessary
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(com.example.qrgo.R.layout.qr_items, parent, false);
            ImageView qrDeleteIcon = convertView.findViewById(R.id.qr_delete_icon);
            SharedPreferences sharedPreferences = convertView.getContext().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString("user", "");
            String qrCodeId = currentQRCode.getQRString();

            qrDeleteIcon.setOnClickListener(v -> {
                FirebaseConnect firebaseConnect = new FirebaseConnect();
                firebaseConnect.getQRCodeManager().deleteUserFromQRCode(
                        qrCodeId,
                        user,
                        new OnUserDeleteFromQRCodeListener() {
                            @Override
                            public void onUserDeleteFromQRCode(boolean success) {
                                if (success) {
                                    // Refresh the list
                                    Intent intent = new Intent(v.getContext(), HomeActivity.class);
                                    v.getContext().startActivity(intent);
                                } else {
                                    Toast.makeText(v.getContext(), "Error deleting user from QR code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            });
        }
        View.OnClickListener QRprofileListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), QrProfileActivity.class);
                if (currentQRCode.getQRString() != "NaN") {
                    intent.putExtra("qr_code", currentQRCode.getQRString());
                    // if we don't come from home page or player page, that means we came
                    // from carousel page or scanned page, so we enable the camera button
                    // in the qr profile page
                    if (!(caller.equals("player")) && !(caller.equals("homeAll"))) {
                        intent.putExtra("comeFrom", "scanned");
                    }
                    view.getContext().startActivity(intent);
                }

            }
        };


        ImageView qrArrowIcon = convertView.findViewById(R.id.qr_arrow_icon);
        qrArrowIcon.setOnClickListener(QRprofileListener);

        ImageView qrDeleteIcon = convertView.findViewById(R.id.qr_delete_icon);
        // Get references to the views in the list item layout
        TextView nameTextView = convertView.findViewById(R.id.qr_name);
        nameTextView.setOnClickListener(QRprofileListener);

        TextView scoreTextView = convertView.findViewById(R.id.qr_score);
        scoreTextView.setOnClickListener(QRprofileListener);
        TextView rankTextView = convertView.findViewById(R.id.qr_rank);
        rankTextView.setOnClickListener(QRprofileListener);


        if (this.caller.equals("player") || this.caller.equals("homeAll")) {
            // set height to match parent
            qrArrowIcon.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            // set width to 50dp
            qrArrowIcon.getLayoutParams().width = 150;
            // remove margin top and right and set margin to 10
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) qrArrowIcon.getLayoutParams();
            marginParams.setMargins(10, 10, 10, 10);
            qrArrowIcon.requestLayout();
            // make this is Gone
            qrDeleteIcon.setVisibility(View.INVISIBLE);

        }
        // Set the text for the views
        String name = currentQRCode.getHumanReadableQR();
        if (name.length() > 8) {
            name = name.substring(0, 8) + "..";
        }
        nameTextView.setText(name);
        scoreTextView.setText(currentQRCode.getQrCodePoints() + " pts");
        rankTextView.setText("#" + (position + 1));

        return convertView;
    }
}