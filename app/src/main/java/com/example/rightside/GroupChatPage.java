package com.example.rightside;

import static com.example.rightside.Manager.USER;
import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.supportGroups;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupChatPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChatPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static int chatId;
    ImageView groupIcon;
    TextView groupName;
    boolean first;
    ArrayList<DocumentSnapshot> textList= new ArrayList();
    LinearLayout chatContainer;
    ListenerRegistration registration;
    long currentId = 0;
    EditText chatInput;
    ScrollView chatScroll;
    DatabaseConnection db = new DatabaseConnection();

    public GroupChatPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_group_chat.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupChatPage newInstance(String param1, String param2) {
        GroupChatPage fragment = new GroupChatPage();
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
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupIcon = view.findViewById(R.id.GroupIcon);
        groupName = view.findViewById(R.id.GroupName);
        ImageButton sendTextButton = view.findViewById(R.id.SendTextButton);
        chatInput = view.findViewById(R.id.ChatInput);
        chatContainer = view.findViewById(R.id.ChatLinearLayout);
        chatScroll = view.findViewById(R.id.ChatContainer);
        chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
        sendTextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(chatInput.getText().toString().trim().equals(""))
                    return;
                uploadMessage(chatInput.getText().toString().trim());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SupportGroup group = null;
        for(int i=0 ; i<supportGroups.size() ; i++){
            if(supportGroups.get(i).id == chatId){
                group = supportGroups.get(i);
            }
        }
        if(group==null){
            return;
        }
        groupName.setText(group.name);
        db.loadImageFromStorage(getContext(),group.iconUrl,groupIcon);
        first = true;
        textList.clear();
        chatContainer.removeAllViews();
        currentId = 0;
        registration = db.getCollection("GroupChats").document("ChatRoom" + chatId).collection("Chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                if(Long.parseLong(document.getId())<=currentId)
                                    continue;
                                currentId = Long.parseLong(document.getId());
                                if (document.getString("sender").equals(Integer.toString(currentUser.userId))) {
                                    sendText(document.getString("text"),document.getString("sender"));
                                } else {
                                    receiveText(document.getString("text"),document.getString("sender"));
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
                            if (Long.parseLong(textList.get(j).getId()) > Long.parseLong(textList.get(j + 1).getId())) {
                                DocumentSnapshot temp = textList.get(j);
                                textList.set(j, textList.get(j + 1));
                                textList.set(j + 1, temp);
                            }
                        }
                    }
                    for(int i=0 ; i<textList.size() ; i++){
                        currentId = currentId < Long.parseLong(textList.get(i).getId()) ? Long.parseLong(textList.get(i).getId()) : currentId;
                        if (textList.get(i).getString("sender").equals(Integer.toString(currentUser.userId))) {
                            sendText(textList.get(i).getString("text"),textList.get(i).getString("sender"));
                        } else {
                            receiveText(textList.get(i).getString("text"),textList.get(i).getString("sender"));
                        }
                    }
                    first = false;
                }
            }
        });
    }
    private void sendText(String text, String id) {
        View chat = LayoutInflater.from(getActivity()).inflate(R.layout.layout_chat_bubble_send,chatContainer,false);
        TextView chatText = chat.findViewById(R.id.ChatText);
        chatText.setText(text);
        TextView senderName = chat.findViewById(R.id.senderName);
        senderName.setText("user_" + id);
        chatInput.setText("");
        chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
        chatContainer.addView(chat);
    }

    private void receiveText(String text, String id) {
        if(text.equals(""))
            return;
        View chat = LayoutInflater.from(getActivity()).inflate(R.layout.layout_chat_bubble_receive,chatContainer,false);

        TextView chatText = chat.findViewById(R.id.ChatText);
        chatText.setText(text);
        TextView senderName = chat.findViewById(R.id.senderName);
        senderName.setText("user_" + id);
        chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
        chatContainer.addView(chat);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void uploadMessage(String text){
        HashMap<String,Object> data = new HashMap<>();
        data.put("sender",currentUser.userId + "");
        data.put("text", text);
        db.getCollection("GroupChats").document("ChatRoom" + chatId).collection("Chats").document(getTimeStamp()).set(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeStamp(){
        DateTimeFormatter timeStampFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        LocalDateTime now = LocalDateTime.now();
        return now.format(timeStampFormat);
    }
}
