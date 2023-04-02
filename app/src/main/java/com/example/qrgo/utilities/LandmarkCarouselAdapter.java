


package com.example.qrgo.utilities;

        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.viewpager.widget.PagerAdapter;

        import com.bumptech.glide.Glide;
        import com.example.qrgo.HomeActivity;
        import com.example.qrgo.PlayerActivity;
        import com.example.qrgo.R;
        import com.example.qrgo.models.BasicPlayerProfile;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;
        import java.util.List;

/**
 * This class provides an adapter to display a carousel of user profiles.
 */
public class LandmarkCarouselAdapter extends PagerAdapter {

    private ArrayList<String> carouselItems;
    private LayoutInflater layoutInflater;
    /**
     * Constructor for the UserCarouselAdapter.
     *
     * @param context       The context of the activity or fragment using the adapter.
     * @param carouselItems A list of BasicPlayerProfile objects to display in the carousel.
     */
    public LandmarkCarouselAdapter(Context context, ArrayList<String> carouselItems) {
        this.carouselItems = carouselItems;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * Returns the number of items in the carousel.
     *
     * @return The number of items in the carousel.
     */
    @Override
    public int getCount() {
        return carouselItems.size();
    }

    /**
     * Determines if a page view is associated with a given object.
     *
     * @param view   The page view to check for association.
     * @param object The object to check for association.
     * @return True if the view is associated with the object, false otherwise.
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    /**
     * Creates the page view for the given position in the carousel.
     *
     * @param container The view container for the page view.
     * @param position  The position of the page view in the carousel.
     * @return The created page view.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.users_carousel_item, container, false);

        // Get the users that will be displayed together
        int startIndex = position;
        int endIndex = Math.min(startIndex + 3, carouselItems.size());
        ArrayList<String> items = new ArrayList<>(carouselItems.subList(startIndex, endIndex));

        // Set up each user within the layout
        for (int i = 0; i < items.size(); i++) {
            String link = items.get(i);
            View imageView = layoutInflater.inflate(R.layout.landmark_card, container, false);
            // Load the image using Glide and apply the transformation
           Picasso.get().load(link)
                   .transform(new RoundedSquareTransform(50))
                   .into((ImageView) imageView.findViewById(R.id.landmark_image));

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImageDialog(link);
                }
            });
            ImageView Image = imageView.findViewById(R.id.landmark_image);

            // Add the user view to the layout
            ViewGroup userContainer;
            switch (i) {
                case 0:
                    userContainer = view.findViewById(R.id.user_1_container);
                    break;
                case 1:
                    userContainer = view.findViewById(R.id.user_2_container);
                    break;
                case 2:
                    userContainer = view.findViewById(R.id.user_3_container);
                    break;
                default:
                    userContainer = null;
                    break;
            }
            if (userContainer != null) {
                userContainer.addView(imageView);
            }
        }

        container.addView(view);

        return view;
    }
    private void showImageDialog(String imageUrl) {
        Dialog dialog = new Dialog(layoutInflater.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image);
        ImageView imageView = dialog.findViewById(R.id.dialog_image_view);
        FloatingActionButton closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Glide.with(imageView).load(imageUrl).into(imageView);
        dialog.show();
    }

    /**
     * This method is called when the adapter is about to destroy the item previously instantiated by
     * the instantiateItem() method. It is responsible for removing the view of the item from the container.
     *
     * @param container the parent view of the item
     * @param position  the position of the item in the adapter
     * @param object    the object representing the item
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
