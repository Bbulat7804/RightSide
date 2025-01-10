package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnonymousSupportGroupPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnonymousSupportGroupPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout suggestionGroupContainer;
    LinearLayout joinedGroupContainer;

    public AnonymousSupportGroupPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_anonymous_support_group.
     */
    // TODO: Rename and change types and number of parameters
    public static AnonymousSupportGroupPage newInstance(String param1, String param2) {
        AnonymousSupportGroupPage fragment = new AnonymousSupportGroupPage();
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
        return inflater.inflate(R.layout.fragment_anonymous_support_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        suggestionGroupContainer = view.findViewById(R.id.SuggestionSupportGroupCardContainer);
        joinedGroupContainer = view.findViewById(R.id.SupportGroupsJoinedContainer);

    }

    public void addSuggestionGroupCard (SupportGroup group){
        View suggestionCard = LayoutInflater.from(getActivity()).inflate(R.layout.card_suggestion_support_group, suggestionGroupContainer, false);

        TextView grpName = suggestionCard.findViewById(R.id.GroupName);
        ImageView grpIcon = suggestionCard.findViewById(R.id.GroupIcon);

        grpName.setText(group.name);
        db.loadImageFromStorage(suggestionCard.getContext(), group.iconUrl,grpIcon);
        suggestionGroupContainer.addView(suggestionCard);
        grpName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.participantId.add(currentUser.userId);
                db.getCollection("Support Groups").document(Integer.toString(group.id)).update("participants_id", FieldValue.arrayUnion(Integer.toString(currentUser.userId))).addOnFailureListener(e -> {
                    System.out.println("failed");
                });
                addCards();
            }
        });
    }
    public void addJoinedGroupCard (SupportGroup group) {
        View groupCard = LayoutInflater.from(getActivity()).inflate(R.layout.card_support_group, joinedGroupContainer, false);

        TextView grpName = groupCard.findViewById(R.id.JoinedGroupName);
        ImageView grpIcon = groupCard.findViewById(R.id.GroupJoinedIcon);
        TextView grpDescription = groupCard.findViewById(R.id.JoinedGroupDescription);
        grpName.setText(group.name);
        grpDescription.setText(group.description);
        db.loadImageFromStorage(groupCard.getContext(), group.iconUrl,grpIcon);
        groupCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupChatPage.chatId = group.id;
                goToPage(groupChatPage,getParentFragmentManager());
                currentUser.supportGroupNo++;
                HashMap<String,Object> data = new HashMap<>();
                data.put("support_group_no", Integer.toString(currentUser.supportGroupNo));
                db.updateDocument("Users", Integer.toString(currentUser.userId), data);
            }
        });
//        db.loadImageFromStorage(groupCard.getContext(), group.image_url,grpIcon);
        joinedGroupContainer.addView(groupCard);
    }

    public void addCards(){
        joinedGroupContainer.removeAllViews();
        suggestionGroupContainer.removeAllViews();
        for (int i=0; i<supportGroups.size(); i++) {
            if(supportGroups.get(i).participantId.contains(currentUser.userId))
                addJoinedGroupCard(supportGroups.get(i));
            else
                addSuggestionGroupCard(supportGroups.get(i));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addCards();
    }
}