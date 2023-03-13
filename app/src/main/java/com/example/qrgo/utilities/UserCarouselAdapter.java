package com.example.qrgo.utilities;

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

import com.example.qrgo.HomeActivity;
import com.example.qrgo.PlayerActivity;
import com.example.qrgo.R;
import com.example.qrgo.models.BasicPlayerProfile;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserCarouselAdapter extends PagerAdapter {

    private List<BasicPlayerProfile> carouselItems;
    private LayoutInflater layoutInflater;

    public UserCarouselAdapter(Context context, List<BasicPlayerProfile> carouselItems) {
        this.carouselItems = carouselItems;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return carouselItems.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.users_carousel_item, container, false);

        // Get the users that will be displayed together
        int startIndex = position;
        int endIndex = Math.min(startIndex + 3, carouselItems.size());
        List<BasicPlayerProfile> users = carouselItems.subList(startIndex, endIndex);

        // Set up each user within the layout
        for (int i = 0; i < users.size(); i++) {
            BasicPlayerProfile user = users.get(i);
            View userView = layoutInflater.inflate(R.layout.users_card, container, false);

            userView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                    // Put the username in the intent
                    intent.putExtra("username", user.getUsername());
                    view.getContext().startActivity(intent);
                }

            });
            ImageView userImage = userView.findViewById(R.id.player_image);
            TextView userName = userView.findViewById(R.id.player_name);
            TextView userScore = userView.findViewById(R.id.player_score);
            TextView collectedQrCodes = userView.findViewById(R.id.player_collected);

            // Set rounded square image using Picasso and RoundedSquareTransform
            Picasso.get()
                    .load(R.drawable.demo_picture)
                    .transform(new RoundedSquareTransform(1000))
                    .into(userImage);
            String name = user.getFirstName();
            if (name.length() > 4) {
                name = name.substring(0, 4) + "...";
            }
            userName.setText(name);
            String scoreString = Integer.toString(user.getTotalScore());
            userScore.setText(scoreString);
            collectedQrCodes.setText("High score:"+user.getHighestScore());

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
                userContainer.addView(userView);
            }
        }

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}