package com.example.qrgo.utilities;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.qrgo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
/**
 The StaggeredAdapter class is an implementation of the RecyclerView.Adapter that supports the display of images in a staggered grid layout.
 */
public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.ViewHolder> {

    private List<String> dataList;

    private Context context;
    /**

     Constructs a new StaggeredAdapter with the specified list of data and context.
     @param data The list of data to display.
     @param context The context used to inflate the layout.
     */
    public StaggeredAdapter(List<String> data, Context context) {
        dataList = data;
        this.context = context;
    }
    /**

     Called when a new ViewHolder is created.
     @param parent The ViewGroup into which the new View will be added.
     @param viewType The view type of the new View.
     @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_item, parent, false);
        return new ViewHolder(view);
    }
    /**
     Called when a ViewHolder is bound to a new item.
     @param holder The ViewHolder to bind the data to.
     @param position The position of the item in the data list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = dataList.get(position);
        Log.d("dataList", item);
        Glide.with(context)
                .load(item)
                .override(Target.SIZE_ORIGINAL)
                .into(holder.staggeredImages);
        holder.bind(item);
        holder.staggeredImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog(item);
            }
        });
    }
    /**
     Displays a dialog with the selected image.
     @param imageUrl The URL of the image to display.
     */
    private void showImageDialog(String imageUrl) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image);
        ImageView imageView = dialog.findViewById(R.id.dialog_image_view);
        FloatingActionButton closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Glide.with(context)
                .load(imageUrl)
                .into(imageView);
        dialog.show();

    }
    /**

     Returns the total number of items in the data list.
     @return The total number of items in the data list.
     */
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    /**

     The ViewHolder class holds references to the views that make up the item view.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView staggeredImages;
        /**

         Constructs a new ViewHolder with the given View.
         @param itemView The item view to hold.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            staggeredImages = itemView.findViewById(R.id.staggeredImages);
        }
        /**
         Binds the data to the views in the ViewHolder.
         @param item The data item to bind.
         */
        public void bind(String item) {
            // Bind the data to the views in the ViewHolder
        }
    }
}
