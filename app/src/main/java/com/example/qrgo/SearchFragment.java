package com.example.qrgo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private ArrayList<BasicPlayerProfile> dataList;
    private UserSearchListAdapter userSearchListAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.search_user_fragment, container, false);

        searchUserEditText = rootView.findViewById(R.id.search_user_edit_text);
        searchUserEditText.requestFocus();
        loadingScreen = rootView.findViewById(R.id.loading_screen);
        userList = rootView.findViewById(R.id.user_list);
        loadingScreen.setVisibility(View.GONE);
        dataList = new ArrayList<>();
        // 3. Display data in listview
        // Both instances of the code are the same up to this point

        FirebaseConnect fb = new FirebaseConnect();

        // search firebase for name entered in searchUserEditText using searchUsers method in FirebaseConnect
        FirebaseConnect.OnUserSearchListener listener = new FirebaseConnect.OnUserSearchListener() {
            @Override
            public void onUserSearchComplete(List<BasicPlayerProfile> users) {
                // Do something with the search results
                for (BasicPlayerProfile user : users) {
                    dataList.add(user);
                }
                userSearchListAdapter = new UserSearchListAdapter(getActivity(), dataList);
                userList.setAdapter(userSearchListAdapter);
                loadingScreen.setVisibility(View.GONE);
            }
            @Override
            public void onUserSearchFailure(Exception e) {
                // Handle the search failure
                e.printStackTrace();
            }
        };

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
                //
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // no application at the moment
            }
            @Override
            public void afterTextChanged(Editable s) {
                String searchQuery = s.toString();
                if (searchQuery.length() > 0) {
                    loadingScreen.setVisibility(View.VISIBLE);
                    fb.searchUsers(searchQuery, listener);
                } else {
                    // clear the dataList and hide the userList and loadingScreen
                    dataList.clear();
                    UserSearchListAdapter adapter = new UserSearchListAdapter(getActivity(), dataList);
                    userList.setAdapter(adapter);
                    userList.setVisibility(View.VISIBLE);
                    loadingScreen.setVisibility(View.GONE);

                    // update ListView with new data
                    userSearchListAdapter.notifyDataSetChanged();
                }
            }
        });
        return rootView;
    }
}
