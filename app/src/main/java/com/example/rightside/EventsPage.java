package com.example.rightside;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventsPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VolunteerPage.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsPage newInstance(String param1, String param2) {
        EventsPage fragment = new EventsPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseConnection db = new DatabaseConnection();
        LinearLayout eventCardContainer = view.findViewById(R.id.EventCardContainer);
        Button tryTambahButton = view.findViewById(R.id.tryTambahButton);

        tryTambahButton.setOnClickListener(v -> {
            addEventCard(eventCardContainer, db);
        });
    }

    private void addEventCard(LinearLayout cardContainer, DatabaseConnection db) {

        // Create a new card
        LayoutInflater inflater = LayoutInflater.from(cardContainer.getContext());
        View cardView = inflater.inflate(R.layout.card_events_page, cardContainer, false);

        // Find views in the card
        ImageView imageView = cardView.findViewById(R.id.EventImage);
        TextView titleTextView = cardView.findViewById(R.id.EventTitle);
        TextView orgNameTextView = cardView.findViewById(R.id.EventOrganizer);
        TextView descriptionTextView = cardView.findViewById(R.id.EventDescription);

        // ganti text dgn data dari cloudfirebase
        db.getDocument("Volunteering Library","vZaeyFAAJAeV3zYGfcpQ")
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        //amik data from firebase
                        titleTextView.setText(document.getString("title"));
                        orgNameTextView.setText(document.getString("author"));
                        descriptionTextView.setText(document.getString("content"));

                        // Get the context of the card view
                        Context cardContext = cardView.getContext();
                        //dapatkan gambar dari storage
                        db.loadImageFromStorage(cardContext, document.getString("media_bucket_path"), imageView);
                        Toast.makeText(cardContext, "Card Added!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "tak jumpa data", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error getting document", e));

        // Add the card to the HorizontalScrollView
        cardContainer.addView(cardView);
        animateCard(cardView);
    }

    private void animateCard(View cardView) {

        // Create animations
        AlphaAnimation fadeIn = new AlphaAnimation(0f, cardView.getAlpha());
        fadeIn.setDuration(300);

        TranslateAnimation slideIn = new TranslateAnimation(0, 0, -100, cardView.getY());
        slideIn.setDuration(300);

        // Start animations together
        cardView.startAnimation(fadeIn);
        cardView.startAnimation(slideIn);
    }
}
