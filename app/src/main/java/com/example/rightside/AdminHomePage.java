package com.example.rightside;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminHomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_admin_home.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomePage newInstance(String param1, String param2) {
        AdminHomePage fragment = new AdminHomePage();
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
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout eventImageContainer = view.findViewById(R.id.EventImageContainer);
        LinearLayout articleImageContainer = view.findViewById(R.id.ArticleImageContainer);
        for(int i=0; i<10 ; i++){
            addImage(eventImageContainer,R.drawable.sample_event_image);
        }
        for(int i=0; i<10 ; i++){
            addImage(articleImageContainer,R.drawable.sample_article_image);
        }
    }

    public void addImage(LinearLayout container, int imageResource){
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(imageResource);

        // Set layout parameters for proper sizing
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1000, LinearLayout.LayoutParams.MATCH_PARENT);

        params.setMargins(20, 0, 0, 10); // Optional: Add spacing between images
        imageView.setLayoutParams(params);

        container.addView(imageView);
    }
}