package com.example.qrgo;

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
    ListView qr_users_listview;
    public QRListFragment() {
    }

    public void setQrCodeList(ArrayList<BasicPlayerProfile> playerList) {
        this.playerList = playerList;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qr_all_users_listview, container, false);
         qr_users_listview = rootView.findViewById(R.id.qr_users_listview);
        BasicUserArrayAdapter userAdapter = new BasicUserArrayAdapter(requireActivity(), playerList, "totalScore");
        qr_users_listview.setAdapter(userAdapter);

        FloatingActionButton back = rootView.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        return rootView;
    }
}