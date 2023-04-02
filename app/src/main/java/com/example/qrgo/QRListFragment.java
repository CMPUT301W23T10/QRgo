package com.example.qrgo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.utilities.BasicUserArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class QRListFragment extends Fragment {
    private ArrayList<BasicPlayerProfile> playerList;

    private String qr_id;
    ListView qr_users_listview;
    public QRListFragment() {
    }
    public void setQRid (String qr_id) {
        this.qr_id = qr_id;
    }

    public void setQrCodeList(ArrayList<BasicPlayerProfile> playerList) {
        this.playerList = playerList;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qr_all_users_listview, container, false);
         qr_users_listview = rootView.findViewById(R.id.qr_users_listview);
        BasicUserArrayAdapter userAdapter = new BasicUserArrayAdapter(requireActivity(), playerList, "totalScore");
        qr_users_listview.setAdapter(userAdapter);

        FloatingActionButton back = rootView.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the topmost fragment from the back stack
                getActivity().getSupportFragmentManager().popBackStack();
                // Create a new intent to navigate to the HomeActivity
                Intent intent = new Intent(getActivity(), QrProfileActivity.class);
                intent.putExtra("qr_code", qr_id);
                // Clear the activity stack so that becomes the new root activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start the HomeActivity and finish the current activity
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }
}