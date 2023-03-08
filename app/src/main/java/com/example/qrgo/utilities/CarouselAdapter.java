package com.example.qrgo.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.qrgo.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CarouselAdapter extends PagerAdapter {

    private List<CustomCarouselItem> carouselItems;
    private LayoutInflater layoutInflater;

    public CarouselAdapter(Context context, List<CustomCarouselItem> carouselItems) {
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
        View view = layoutInflater.inflate(R.layout.caraousel_card, container, false);

        CustomCarouselItem carouselItem = carouselItems.get(position);

        ImageView qrCodeImage = view.findViewById(R.id.qr_code_image);
        TextView qrCodeRank = view.findViewById(R.id.qr_code_rank);
        TextView qrCodeName = view.findViewById(R.id.qr_code_name);
        TextView qrCodePoints = view.findViewById(R.id.qr_code_points);

        // Set rounded square image using Picasso and RoundedSquareTransform
        Picasso.get()
                .load(carouselItem.getQrCodeImage())
                .transform(new RoundedSquareTransform(500))
                .into(qrCodeImage);
        qrCodeRank.setText(carouselItem.getQrCodeRank());
        qrCodeName.setText(carouselItem.getQrCodeName());
        qrCodePoints.setText(carouselItem.getQrCodePoints());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
