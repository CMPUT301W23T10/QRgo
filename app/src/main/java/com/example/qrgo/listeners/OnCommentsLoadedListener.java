package com.example.qrgo.listeners;

import com.example.qrgo.models.Comment;

import java.util.List;

public interface OnCommentsLoadedListener {
    void onCommentsLoaded(List<Comment> comments);

    void onCommentsLoadFailure(Exception e);
}
