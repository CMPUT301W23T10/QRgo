package com.example.qrgo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.utilities.BasicQrArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrListview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrListview extends Fragment {
    private ArrayList<BasicQRCode> qrCodeList;
    private String comeFrom;
    ListView listView;
    public QrListview() {
        // Required empty public constructor
    }
    public void setQrCodeList(ArrayList<BasicQRCode> qrCodeList) {
        this.qrCodeList = qrCodeList;

    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }
        View rootView = inflater.inflate(R.layout.fragment_qr_listview, container, false);
        listView = rootView.findViewById(R.id.fragment_qr_listview);
        FloatingActionButton closeButton = rootView.findViewById(R.id.back_button);
        BasicQrArrayAdapter qrAdapter = new BasicQrArrayAdapter(requireActivity(), qrCodeList, comeFrom);

        TextView title = rootView.findViewById(R.id.qr_list_title);
        if (comeFrom.equals("home")) {
            title.setText("My QR Codes");
        } else if (comeFrom.equals("homeAll")) {
            title.setText("All QR Codes");
        } else if (comeFrom.equals("player")) {
            title.setText("QR Codes");
        }
        listView.setAdapter(qrAdapter);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the topmost fragment from the back stack
                getActivity().getSupportFragmentManager().popBackStack();
                if (comeFrom.equals("home") || comeFrom.equals("homeAll")) {
                    // Create a new intent to navigate to the HomeActivity
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    // Clear the activity stack so that becomes the new root activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Start the HomeActivity and finish the current activity
                    startActivity(intent);

                } else if (comeFrom.equals("player")) {
                    // Create a new intent to navigate to the ScanActivity
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    // Clear the activity stack so that becomes the new root activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Start the HomeActivity and finish the current activity
                    startActivity(intent);
                }
                getActivity().finish();
      }
        });
        return rootView;
    }
}