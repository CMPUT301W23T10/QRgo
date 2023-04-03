package com.example.qrgo.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.example.qrgo.GeoLocationActivity;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.QrProfileActivity;
import com.example.qrgo.R;
import com.example.qrgo.listeners.OnUserDeleteFromQRCodeListener;
import com.example.qrgo.models.BasicQRCode;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * An adapter class for displaying a list of QR codes in a carousel format using a ViewPager.
 *
 * This adapter extends PagerAdapter to handle the swipe functionality.
 */
public class CarouselAdapter extends PagerAdapter {

    private List<BasicQRCode> carouselItems;
    private LayoutInflater layoutInflater;

    /**
     * Constructs a new CarouselAdapter object.
     *
     * @param context       The context of the current state of the application.
     * @param carouselItems The list of BasicQRCode objects to be displayed in the carousel.
     */
    public CarouselAdapter(Context context, List<BasicQRCode> carouselItems) {
        this.carouselItems = carouselItems;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * Returns the number of QR codes in the carousel.
     *
     * @return The number of QR codes in the carousel.
     */
    @Override
    public int getCount() {
        return carouselItems.size();
    }

    /**
     * Determines if a given view is associated with a given object.
     *
     * @param view   The view to be compared with the object.
     * @param object The object to be compared with the view.
     * @return True if the view is associated with the object, false otherwise.
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * Creates a new view for a QR code in the carousel.
     *
     * @param container The parent view where the new view will be attached.
     * @param position  The position of the QR code in the carousel.
     * @return The new view for the QR code.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.caraousel_card, container, false);
        BasicQRCode carouselItem = carouselItems.get(position);


        ImageView locationButton = view.findViewById(R.id.qr_code_location);
        ImageView deleteButton = view.findViewById(R.id.qr_code_delete);
        String qrCodeId = carouselItem.getQRString();
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "");

        if (qrCodeId.equals("NaN")) {
            // Do nothing

        } else {
            locationButton.setOnClickListener(v -> {
                Intent intent1 = new Intent(v.getContext(), GeoLocationActivity.class);
                intent1.putExtra("qrCode", qrCodeId);
                v.getContext().startActivity(intent1);
            });
            deleteButton.setOnClickListener(v -> {
                FirebaseConnect firebaseConnect = new FirebaseConnect();
                firebaseConnect.getQRCodeManager().deleteUserFromQRCode(
                    qrCodeId,
                    user,
                    new OnUserDeleteFromQRCodeListener() {
                        @Override
                        public void onUserDeleteFromQRCode(boolean success) {
                            if (success) {
                                // Refresh the activity
                                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                                v.getContext().startActivity(intent);
                            } else {
                                Toast.makeText(v.getContext(), "Error deleting user from QR code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
            });
        }



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), QrProfileActivity.class);
                if (carouselItem.getQRString() != "NaN") {
                    intent.putExtra("qr_code", carouselItem.getQRString());
                    intent.putExtra("comeFrom", "scanned");
                    view.getContext().startActivity(intent);
                }
            }
        });

        ImageView qrCodeImage = view.findViewById(R.id.qr_code_image);
        TextView qrCodeRank = view.findViewById(R.id.qr_code_rank);
        TextView qrCodeName = view.findViewById(R.id.qr_code_name);
        TextView qrCodePoints = view.findViewById(R.id.qr_code_points);
        LinearLayout caraouselImageContainer = view.findViewById(R.id.caraousel_image_container);

        // Set rounded square image using Picasso and RoundedSquareTransform
        Picasso.get()
                // CHANGE THIS TO THE ACTUAL IMAGE URL
                .load(R.drawable.demo_qr_image)
                .transform(new RoundedSquareTransform(500))
                .into(qrCodeImage);
        String positionString = Integer.toString(position + 1);
        qrCodeRank.setText("#" + positionString);
        // Truncate the qrname if it is too long
        if (carouselItem.getHumanReadableQR().length() > 6) {
            String truncatedName = carouselItem.getHumanReadableQR().substring(0, 6) + "...";
            if (carouselItem.getHumanReadableQR().contains("(C)")) {
                caraouselImageContainer.setBackgroundResource(R.drawable.common_rounded_corner);
            }
            else if (carouselItem.getHumanReadableQR().contains("(R)")) {
                caraouselImageContainer.setBackgroundResource(R.drawable.rare_rounded_corner);

            } else if (carouselItem.getHumanReadableQR().contains("(E)")) {
                caraouselImageContainer.setBackgroundResource(R.drawable.epic_rounded_corner);

            } else if (carouselItem.getHumanReadableQR().contains("(L)")) {
                caraouselImageContainer.setBackgroundResource(R.drawable.legendary_rounded_corner);
            }
            else {
                caraouselImageContainer.setBackgroundResource(R.drawable.home_card_rounded_corners);
            }
            qrCodeName.setText(truncatedName);

        } else {
            qrCodeName.setText(carouselItem.getHumanReadableQR());
            if (carouselItem.getHumanReadableQR().contains("(C)")) {
                caraouselImageContainer.setBackgroundResource(R.drawable.common_rounded_corner);
            }
            else if (carouselItem.getHumanReadableQR().contains("(R)")) {
                caraouselImageContainer.setBackgroundResource(R.drawable.rare_rounded_corner);

            } else if (carouselItem.getHumanReadableQR().contains("(E)")) {
                caraouselImageContainer.setBackgroundResource(R.drawable.epic_rounded_corner);

            } else if (carouselItem.getHumanReadableQR().contains("(L)")) {
                caraouselImageContainer.setBackgroundResource(R.drawable.legendary_rounded_corner);
            }
            else {
                caraouselImageContainer.setBackgroundResource(R.drawable.home_card_rounded_corners);
            }
        }
        String pointsString = Integer.toString(carouselItem.getQrCodePoints());
        qrCodePoints.setText(pointsString);

        container.addView(view);

        return view;
    }

    /**
     * This method is called when the item instantiated by {@link #instantiateItem(ViewGroup, int)}
     * is no longer needed and should be removed from the container. In this case, it removes the
     * view associated with the specified position.
     *
     * @param container The containing {@link ViewGroup} from which the item will be removed.
     * @param position  The position of the item within the adapter's data set that is being removed.
     * @param object    The {@link Object} representing the view to be removed.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
