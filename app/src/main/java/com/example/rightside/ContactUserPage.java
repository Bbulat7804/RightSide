package com.example.rightside;

import static com.example.rightside.Manager.*;
import static com.google.firebase.firestore.DocumentChange.Type.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactUserPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUserPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout chatContainer;
    EditText chatInput;
    int chatIndex = 0;
    boolean first;
    ListenerRegistration registration;
    ArrayList<DocumentSnapshot> textList= new ArrayList();
    public ContactUserPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_contact_user.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUserPage newInstance(String param1, String param2) {
        ContactUserPage fragment = new ContactUserPage();
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
        return inflater.inflate(R.layout.fragment_contact_user, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button requestButton = view.findViewById(R.id.buttonRequest);
        ImageButton sendTextButton = view.findViewById(R.id.SendTextButton);
        chatInput = view.findViewById(R.id.ChatInput);
        chatContainer = view.findViewById(R.id.ChatLinearLayout);

        sendTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatInput.getText().toString().trim().equals(""))
                    return;
                uploadMessage(chatInput.getText().toString().trim());
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSiblingPage(viewRequestAdminPage,getParentFragmentManager());
            }
        });
    }

    private void sendText(String text) {
        if(text.equals(""))
            return;
        View chat = LayoutInflater.from(getActivity()).inflate(R.layout.layout_chat_bubble_send,chatContainer,false);

        TextView chatText = chat.findViewById(R.id.ChatText);
        chatText.setText(text);
        chatInput.setText("");

        chatContainer.addView(chat);
    }

    private void receiveText(String text) {
        if(text.equals(""))
            return;
        View chat = LayoutInflater.from(getActivity()).inflate(R.layout.layout_chat_bubble_receive,chatContainer,false);

        TextView chatText = chat.findViewById(R.id.ChatText);
        chatText.setText(text);

        chatContainer.addView(chat);
    }


    @Override
    public void onResume() {
        super.onResume();
        first = true;
        textList.clear();
        chatContainer.removeAllViews();
        chatIndex = 0;
        registration = db.getCollection("ChatRoom" + ViewRequestAdminPage.requestId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            DocumentSnapshot document = documentChange.getDocument();
                            if(first){
                                textList.add(document);
                            }
                            else {
                                if(Integer.parseInt(document.getId())<=chatIndex)
                                    continue;
                                chatIndex++;
                                if (document.getString("sender").equals(ADMIN)) {
                                    sendText(document.getString("text"));
                                } else {
                                    receiveText(document.getString("text"));
                                }
                            }
                            break;
                        case MODIFIED:
                            // Document was modified
                            break;
                        case REMOVED:
                            // Document was removed
                            break;
                    }
                }
                if(first){
                    int n = textList.size();

                    // Traverse through all elements in the ArrayList
                    for (int i = 0; i < n - 1; i++) {
                        // Last i elements are already in place
                        for (int j = 0; j < n - 1 - i; j++) {
                            // Compare adjacent elements and swap if needed
                            if (Integer.parseInt(textList.get(j).getId()) > Integer.parseInt(textList.get(j + 1).getId())) {
                                DocumentSnapshot temp = textList.get(j);
                                textList.set(j, textList.get(j + 1));
                                textList.set(j + 1, temp);
                            }
                        }
                    }
                    for(int i=0 ; i<textList.size() ; i++){
                        chatIndex++;
                        if (textList.get(i).getString("sender").equals(ADMIN)) {
                            System.out.println("sampai");
                            sendText(textList.get(i).getString("text"));
                        } else {
                            receiveText(textList.get(i).getString("text"));
                        }
                    }
                    first = false;
                }
            }
        });
    }

    public void uploadMessage(String text){
        HashMap <String,Object> data = new HashMap<>();
        data.put("sender",ADMIN);
        data.put("text", text);
        db.addDocument("ChatRoom" + ViewRequestAdminPage.requestId, data,Integer.toString(chatIndex+1));
    }

    @Override
    public void onStop() {
        super.onStop();
        if(registration!=null)
        registration.remove();
    }
}