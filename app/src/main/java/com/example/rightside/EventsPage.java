package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LocalDate currentDate = LocalDate.now();
        Handler handler = new Handler();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        TextView todayDate = view.findViewById(R.id.date);
        LinearLayout eventCardContainer = view.findViewById(R.id.EventCardContainer);
        LinearLayout upcomingEventsContainer = view.findViewById(R.id.upcomingEventsContainer);
        ImageView uploadEventAdminButton = view.findViewById(R.id.uploadEventAdminButton);

        todayDate.setText(dateFormatter.format(new Date()));

        //admin je akan nampak upload button
        if (userType.equals(USER))
            uploadEventAdminButton.setVisibility(View.GONE);

        uploadEventAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(uploadEventPage, getParentFragmentManager());
            }
        });

        //add card kat "Now Calling" ikut date within this month
        for (int i = 0; i < events.size(); i++) {
            Date eventDate = getDateInDateFormat(dateFormatter, events.get(i).date);
            LocalDate localEventDate = eventDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long daysBetween = ChronoUnit.DAYS.between(currentDate, localEventDate);
            if (daysBetween >= 0 && daysBetween <= 30){
                addCardNowCalling(eventCardContainer, events.get(i));
            } else {
                int finalI = i;
                //tambah delay
                handler.postDelayed(() -> {
                    //tambah yang lain kat bawah
                    addCardUpcomingEvents(upcomingEventsContainer, events.get(finalI));
                        },i*500);
            }
        }
    }

    private Date getDateInDateFormat(SimpleDateFormat dateFormatter, String dateString) {
        try {
            Date date = dateFormatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addCardNowCalling(LinearLayout eventCardContainer, Event event) {

        // Create a new card
        View cardView = LayoutInflater.from(eventCardContainer.getContext()).inflate(R.layout.card_events_page, eventCardContainer, false);

        // cari view
        ImageView eventImageView = cardView.findViewById(R.id.EventImage);
        TextView eventTitleTextView = cardView.findViewById(R.id.EventTitle);
        TextView orgNameTextView = cardView.findViewById(R.id.EventOrganizer);
        TextView eventDescriptionTextView = cardView.findViewById(R.id.EventDescription);
        TextView eventDateTextView = cardView.findViewById(R.id.EventDate);

        //tuka view content
        eventTitleTextView.setText(event.title);
        eventDescriptionTextView.setText(event.description);
        orgNameTextView.setText(event.organizer);
        eventDateTextView.setText(event.date);
        db.loadImageFromStorage(getContext(), event.imageURL, eventImageView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(event.url));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Unable to open URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add the card to the HorizontalScrollView
        eventCardContainer.addView(cardView);
        animateCard(cardView);
    }

    private void addCardUpcomingEvents(LinearLayout upcomingEventsContainer, Event event){
        // Create a new card
        View cardView = LayoutInflater.from(upcomingEventsContainer.getContext()).inflate(R.layout.card_upcoming_events, upcomingEventsContainer, false);
        //cari view
        ImageView eventImageView = cardView.findViewById(R.id.UpcomingEventsImage);
        TextView eventTitleTextView = cardView.findViewById(R.id.UpcomingEventsTitle);
        TextView orgNameTextView = cardView.findViewById(R.id.UpcomingEventsOrganizer);
        TextView eventDescriptionTextView = cardView.findViewById(R.id.UpcomingEventsDescription);
        TextView eventDateTextView = cardView.findViewById(R.id.UpcomingEventsDate);
        //tuka view content
        eventTitleTextView.setText(event.title);
        eventDescriptionTextView.setText(event.description);
        orgNameTextView.setText(event.organizer);
        eventDateTextView.setText(event.date);
        db.loadImageFromStorage(getContext(), event.imageURL, eventImageView);
        // Add the card to the HorizontalScrollView
        upcomingEventsContainer.addView(cardView);
        animateCard(cardView);
    }

    private void animateCard(View cardView) {
        TranslateAnimation slideIn = new TranslateAnimation(0, 0, -100, cardView.getY());
        slideIn.setDuration(500);
        cardView.startAnimation(slideIn);
    }

}