package com.example.qrgo.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.qrgo.R;
import com.example.qrgo.models.BasicQRCode;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CarouselAdapter extends PagerAdapter {

    private List<BasicQRCode> carouselItems;
    private LayoutInflater layoutInflater;

    public CarouselAdapter(Context context, List<BasicQRCode> carouselItems) {
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

        BasicQRCode carouselItem = carouselItems.get(position);

        ImageView qrCodeImage = view.findViewById(R.id.qr_code_image);
        TextView qrCodeRank = view.findViewById(R.id.qr_code_rank);
        TextView qrCodeName = view.findViewById(R.id.qr_code_name);
        TextView qrCodePoints = view.findViewById(R.id.qr_code_points);

        // Set rounded square image using Picasso and RoundedSquareTransform
        Picasso.get()
                // CHANGE THIS TO THE ACTUAL IMAGE URL
                .load(R.drawable.demo_qr_image)
                .transform(new RoundedSquareTransform(500))
                .into(qrCodeImage);
        String positionString = Integer.toString(position + 1);
        qrCodeRank.setText("#"+positionString);
        // Truncate the qrname if it is too long
        if (carouselItem.getHumanReadableQR().length() > 6) {
            String truncatedName = carouselItem.getHumanReadableQR().substring(0, 6) + "...";
            qrCodeName.setText(truncatedName);
        } else {
            qrCodeName.setText(carouselItem.getHumanReadableQR());
        }
        String pointsString = Integer.toString(carouselItem.getQrCodePoints());
        qrCodePoints.setText(pointsString);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
