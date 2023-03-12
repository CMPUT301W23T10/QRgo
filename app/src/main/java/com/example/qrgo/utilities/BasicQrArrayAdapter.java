package com.example.qrgo.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.qrgo.R;
import com.example.qrgo.models.BasicQRCode;

import java.util.ArrayList;

public class BasicQrArrayAdapter extends ArrayAdapter<BasicQRCode> {
    private Context mContext;
    private ArrayList<BasicQRCode> mQRList;

    public BasicQrArrayAdapter(Context context, ArrayList<BasicQRCode> qrList) {
        super(context, 0, qrList);
        mContext = context;
        mQRList = qrList;
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
