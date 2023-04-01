package com.example.qrgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.qrgo.listeners.OnUserSearchListener;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.utilities.BasicUserArrayAdapter;
import com.example.qrgo.utilities.SearchArrayAdapter;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchUserEditText;
    private LinearLayout loadingScreen;
    private ListView userList;
    private ArrayList<BasicPlayerProfile> dataList;
    private SearchArrayAdapter searchArrayAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.search_user_fragment, container, false);

        // connect objects to UI counterparts
        searchUserEditText = rootView.findViewById(R.id.search_user_edit_text);
        searchUserEditText.requestFocus();
        loadingScreen = rootView.findViewById(R.id.loading_screen);
        userList = rootView.findViewById(R.id.user_list);
        loadingScreen.setVisibility(View.GONE);
        dataList = new ArrayList<>();

        // set up the user list adapter
        searchArrayAdapter = new SearchArrayAdapter(getContext(), dataList);
        userList.setAdapter(searchArrayAdapter);
        FirebaseConnect fb = new FirebaseConnect();

        OnUserSearchListener listener = new OnUserSearchListener() {
            @Override
            public void onUserSearchComplete(List<BasicPlayerProfile> users) {
                // Do something with the search results
                for (BasicPlayerProfile user : users) {
                    dataList.add(user);
                    searchArrayAdapter.notifyDataSetChanged();
                }

                loadingScreen.setVisibility(View.GONE);
            }
            @Override
            public void onUserSearchFailure(Exception e) {
                // Handle the search failure
                e.printStackTrace();
            }
        };

        // button to navigate back to previous fragment
        FloatingActionButton cross = rootView.findViewById(R.id.back_button);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GO back
                getActivity().onBackPressed();
            }
        });
        searchUserEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // empty on purpose, not needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // on text changed, update the view
                dataList.clear();
                searchArrayAdapter.notifyDataSetChanged();
                userList.setVisibility(View.VISIBLE);
                loadingScreen.setVisibility(View.GONE);

            }
            @Override
            // after the text is changed, search for the user
            public void afterTextChanged(Editable s) {
                String searchQuery = s.toString();
                if (searchQuery.length() > 0) {
                    loadingScreen.setVisibility(View.VISIBLE);
                    fb.getUserManager().searchUsers(searchQuery, listener);
                } else {
                    // clear the dataList and hide the userList and loadingScreen
                    dataList.clear();
                    searchArrayAdapter.notifyDataSetChanged();
                    userList.setVisibility(View.VISIBLE);
                    loadingScreen.setVisibility(View.GONE);


                }
            }
        });
        return rootView;
    }
}
