package com.example.qrgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.qrgo.models.Comment;
import com.example.qrgo.utilities.BasicCommentArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrListviewFragment} factory method to
 * create an instance of this fragment.
 */
public class CommentListviewFragment extends Fragment {
    private ArrayList<Comment> commentList;
    ListView listView;
    public CommentListviewFragment() {
        // Required empty public constructor
    }
    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }


        View rootView = inflater.inflate(R.layout.fragment_comment_listview, container, false);
        listView = rootView.findViewById(R.id.fragment_comments_listview);
        FloatingActionButton closeButton = rootView.findViewById(R.id.back_button);
        BasicCommentArrayAdapter commentAdapter = new BasicCommentArrayAdapter(getActivity(), R.layout.comment_items, commentList);
        listView.setAdapter(commentAdapter);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootView;
    }
}