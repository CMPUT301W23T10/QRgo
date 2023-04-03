package com.example.qrgo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.firebase.QRCodeFirebaseManager;
import com.example.qrgo.listeners.OnPlayerListLoadedListener;
import com.example.qrgo.listeners.QRCodeListener;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.QRCode;
import com.example.qrgo.utilities.BasicUserArrayAdapter;
import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.QRCodeArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardFragment extends Fragment {
    // If the user presses the back button, go back to the home activity
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button press in your Fragment
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    private String comeFromString = "";
    private ArrayList<String> qrCodeList;

    public void setComeFrom(String comeFromString) {
        this.comeFromString = comeFromString;
    }

    public void setQrCodeList(ArrayList<String> qrCodeList) {
        this.qrCodeList = qrCodeList;
    }

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    FloatingActionButton back;
    FloatingActionButton highScoreQR;
    FloatingActionButton mostScannedQR;
    FloatingActionButton totalHighScore;
    FloatingActionButton highScoreLocation;
    TextView users_subtitle;
    ListView all_users_listview;
    FirebaseConnect fb = new FirebaseConnect();


    private void toggleFabs(FloatingActionButton activeFab) {
        FloatingActionButton[] fabs = {highScoreQR, mostScannedQR, totalHighScore, highScoreLocation};
        for (FloatingActionButton fab : fabs) {
            if (fab == activeFab) {

                // Toggle the active fab
                fab.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF28262C")));
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                users_subtitle.setText(fab.getContentDescription());
                // If the active fab is highScoreQR, load the high score list
                if (activeFab == highScoreQR) {
                    fb.getPlayerProfileManager().getPlayersSortedByHighestScore(
                            new OnPlayerListLoadedListener() {
                                @Override
                                public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                                    // convert the list to an array
                                    ArrayList<BasicPlayerProfile> playerArrayList = new ArrayList<>(playerList);
                                    BasicUserArrayAdapter userAdapter = new BasicUserArrayAdapter(requireActivity(), playerArrayList, "highScore");
                                    all_users_listview.setAdapter(userAdapter);
                                }

                                @Override
                                public void onPlayerListLoadFailure(Exception e) {
                                    Toast.makeText(requireActivity(), "Failed to load players", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                } else if (activeFab == totalHighScore) {
                    fb.getPlayerProfileManager().getPlayersSortedByTotalScore(
                            new OnPlayerListLoadedListener() {
                                @Override
                                public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                                    // convert the list to an array
                                    ArrayList<BasicPlayerProfile> playerArrayList = new ArrayList<>(playerList);
                                    BasicUserArrayAdapter userAdapter = new BasicUserArrayAdapter(requireActivity(), playerArrayList, "totalScore");
                                    all_users_listview.setAdapter(userAdapter);
                                }

                                @Override
                                public void onPlayerListLoadFailure(Exception e) {
                                    Toast.makeText(requireActivity(), "Failed to load players", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                } else if (activeFab == mostScannedQR) {
                    fb.getPlayerProfileManager().getPlayersSortedByTotalScans(
                            new OnPlayerListLoadedListener() {
                                @Override
                                public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                                    // convert the list to an array
                                    ArrayList<BasicPlayerProfile> playerArrayList = new ArrayList<>(playerList);
                                    BasicUserArrayAdapter userAdapter = new BasicUserArrayAdapter(requireActivity(), playerArrayList, "totalScans");
                                    all_users_listview.setAdapter(userAdapter);
                                }

                                @Override
                                public void onPlayerListLoadFailure(Exception e) {
                                    Toast.makeText(requireActivity(), "Failed to load players", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                } else if (activeFab == highScoreLocation) {
                    if (comeFromString.equals("HomeActivity")) {
                        Intent intent = new Intent(getActivity(), GeoLocationActivity.class);
                        intent.putExtra("LeaderBoard", "leaderboard");
                        startActivity(intent);
                    } else {
                        // populate listview
                        QRCodeFirebaseManager qrCodeFirebaseManager = new QRCodeFirebaseManager();
                        //Loop through qrCodeList and get the QRCode objects and add them to the list
                        ArrayList<QRCode> qrCodeArrayList = new ArrayList<>();
                        final int[] numRetrieved = {0};

                        for (String qrCode : qrCodeList) {
                            qrCodeFirebaseManager.getQRCode(qrCode, new QRCodeListener() {
                                @Override
                                public void onQRCodeRetrieved(QRCode qrCode) {
                                    // add the qrCode to the list
                                    qrCodeArrayList.add(qrCode);
                                    numRetrieved[0]++;
                                    if (numRetrieved[0] == qrCodeList.size()) {
                                        // all QR codes have been retrieved, sort the list
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            qrCodeArrayList.sort(new Comparator<QRCode>() {
                                                @Override
                                                public int compare(QRCode qrCode1, QRCode qrCode2) {
                                                    if (qrCode1.getQrCodePoints() > qrCode2.getQrCodePoints()) {
                                                        return -1;
                                                    } else if (qrCode1.getQrCodePoints() < qrCode2.getQrCodePoints()) {
                                                        return 1;
                                                    } else {
                                                        return 0;
                                                    }
                                                }
                                            });
                                        }
                                        QRCodeArrayAdapter userAdapter = new QRCodeArrayAdapter(requireActivity(), qrCodeArrayList);
                                        all_users_listview.setAdapter(userAdapter);

                                    }

                                }

                                @Override
                                public void onQRCodeNotFound() {
                                    Toast.makeText(requireActivity(), "QR Code not found", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onQRCodeRetrievalFailure(Exception e) {
                                    Toast.makeText(requireActivity(), "Failed to retrieve QR Code", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // once the above code is done, log the list
                        }


                    }
                }
            } else {
                // Untoggle other fabs
                fab.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF28262C")));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }
        View rootView = inflater.inflate(R.layout.fragment_leaderboard_highscore, container, false);

        back = rootView.findViewById(R.id.back_button);
        highScoreQR = rootView.findViewById(R.id.high_score_button);
        users_subtitle = rootView.findViewById(R.id.users_subtitle);
        mostScannedQR = rootView.findViewById(R.id.most_scanned_button);
        totalHighScore = rootView.findViewById(R.id.total_score_button);
        highScoreLocation = rootView.findViewById(R.id.location_score_button);
        all_users_listview = rootView.findViewById(R.id.all_users_listview);

        // Set highScoreQR as the default active FAB
        if (comeFromString.equals("HomeActivity")) {
            toggleFabs(highScoreQR);
        } else {
            toggleFabs(highScoreLocation);
        }


        highScoreQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabs(highScoreQR);
            }
        });

        mostScannedQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabs(mostScannedQR);
            }
        });

        totalHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabs(totalHighScore);
            }
        });

        highScoreLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabs(highScoreLocation);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the topmost fragment from the back stack
                getActivity().getSupportFragmentManager().popBackStack();
                // Create a new intent to navigate to the HomeActivity
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                // Clear the activity stack so that becomes the new root activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start the HomeActivity and finish the current activity
                startActivity(intent);
                getActivity().finish();
            }
        });


        return rootView;
    }
}