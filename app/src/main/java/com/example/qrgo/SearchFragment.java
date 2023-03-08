package com.example.qrgo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.utilities.FirebaseConnect;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText searchUserEditText;
    private LinearLayout loadingScreen;

    private ListView userList;

    private ArrayList<BasicPlayerProfile> users = new ArrayList<>();

    private FirebaseConnect fb = new FirebaseConnect();

    private BasicPlayerProfile testBasicPlayerProfile;

    private UserSearchListAdapter userAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

        Log.d("TAG", "onCreateView: activated");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.search_user_fragment, container, false);


        searchUserEditText = rootView.findViewById(R.id.search_user_edit_text);
        //loadingScreen = rootView.findViewById(R.id.loading_screen);
        userList = rootView.findViewById(R.id.user_list);

        userAdapter = new UserSearchListAdapter(getContext(), users);

        // hide loadingScreen and userList initially
        //loadingScreen.setVisibility(View.GONE);
       // userList.setVisibility(View.GONE);

        // search firebase for name entered in searchUserEditText using searchUsers method in FirebaseConnect
        FirebaseConnect.OnUserSearchListener listener = new FirebaseConnect.OnUserSearchListener() {
            @Override
            public void onUserSearchComplete(List<BasicPlayerProfile> users) {
                // hide loadingScreen and show userList
                //loadingScreen.setVisibility(View.GONE);
                userList.setVisibility(View.VISIBLE);

            }

            @Override
            public void onUserSearchFailure(Exception e) {
            }
        };

        searchUserEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = searchUserEditText.getText().toString();
                fb.searchUsers(username, listener);
                Log.d("lucas", username);
                final ArrayAdapter<BasicPlayerProfile> arrayAdapter = new ArrayAdapter<BasicPlayerProfile>(getContext(), android.R.layout.simple_list_item_1, users);
                userList.setAdapter(arrayAdapter);
                userAdapter.notifyDataSetChanged();
            }
        });

        // capture the text entered in the searchUserEditText and search firebase for that name
        String username = searchUserEditText.getText().toString();
        fb.searchUsers(username, listener);
        Log.d("lucas", username);

        final ArrayAdapter<BasicPlayerProfile> arrayAdapter = new ArrayAdapter<BasicPlayerProfile>(getContext(), android.R.layout.simple_list_item_1, users);
        userList.setAdapter(arrayAdapter);


        return rootView;
        //return inflater.inflate(R.layout.fragment_search, container, false);







    }
}
