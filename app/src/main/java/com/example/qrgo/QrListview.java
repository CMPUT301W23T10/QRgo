package com.example.qrgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<BasicQRCode> qrCodeList;
    ListView listView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QrListview() {
        // Required empty public constructor
    }
    public void setQrCodeList(ArrayList<BasicQRCode> qrCodeList) {
        this.qrCodeList = qrCodeList;

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QrListview.
     */
    // TODO: Rename and change types and number of parameters
    public static QrListview newInstance(String param1, String param2) {

        QrListview fragment = new QrListview();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



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
        BasicQrArrayAdapter qrAdapter = new BasicQrArrayAdapter(requireActivity(), qrCodeList);
        listView.setAdapter(qrAdapter);



        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }

        });
        return rootView;
    }
}