package com.example.qrgo.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrgo.R;
import com.example.qrgo.models.BasicQRCode;

import java.util.ArrayList;

public class BasicQrArrayAdapter extends ArrayAdapter<BasicQRCode> {
    private Context mContext;
    private ArrayList<BasicQRCode> mQRList;

    private String caller = "";

    public BasicQrArrayAdapter(Context context, ArrayList<BasicQRCode> qrList, String caller) {
        super(context, 0, qrList);
        mContext = context;
        mQRList = qrList;
        this.caller = caller;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the current item from the data array
        BasicQRCode currentQRCode = getItem(position);

        // Inflate the list item layout if necessary
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(com.example.qrgo.R.layout.qr_items, parent, false);
        }

        // Get references to the views in the list item layout
        TextView nameTextView = convertView.findViewById(R.id.qr_name);
        TextView scoreTextView = convertView.findViewById(R.id.qr_score);
        TextView rankTextView = convertView.findViewById(R.id.qr_rank);

        if (this.caller.equals("player") || this.caller.equals("homeAll")) {
            ImageView qr_arrow_icon = convertView.findViewById(R.id.qr_arrow_icon);
            // set height to match parent
            qr_arrow_icon.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            // set width to 50dp
            qr_arrow_icon.getLayoutParams().width = 150;
            // remove margin top and right and set margin to 10
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) qr_arrow_icon.getLayoutParams();
            marginParams.setMargins(10, 10, 10, 10);
            qr_arrow_icon.requestLayout();

            // make this is Gone
            ImageView qr_delete_icon = convertView.findViewById(R.id.qr_delete_icon);
            qr_delete_icon.setVisibility(View.INVISIBLE);
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
