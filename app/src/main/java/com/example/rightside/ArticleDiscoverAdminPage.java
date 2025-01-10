package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLOutput;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleDiscoverAdminPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleDiscoverAdminPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseConnection db = new DatabaseConnection();

    public ArticleDiscoverAdminPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_article_discover_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleDiscoverAdminPage newInstance(String param1, String param2) {
        ArticleDiscoverAdminPage fragment = new ArticleDiscoverAdminPage();
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
        return inflater.inflate(R.layout.fragment_article_discover_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton uploadButton = view.findViewById(R.id.UploadButton);
        LinearLayout articleContainer = view.findViewById(R.id.ArticleContainer);
        Button[] buttons = new Button[4];

        buttons[0] = view.findViewById(R.id.All);
        buttons[1] = view.findViewById(R.id.Guides);
        buttons[2] = view.findViewById(R.id.Rights);
        buttons[3] = view.findViewById(R.id.Discrimination);
        int green = ContextCompat.getColor(getContext(),R.color.green);
        int white = ContextCompat.getColor(getContext(),R.color.white);
        int black = Color.BLACK;

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleContainer.removeAllViews();
                for(int i=0 ; i<4 ; i++){
                    buttons[i].setBackgroundColor(white);
                    buttons[i].setTextColor(black);
                }
                buttons[0].setBackgroundColor(green);
                buttons[0].setTextColor(white);
                for(int i=0 ; i<articles.size() ; i++){
                    addArticleCard(articleContainer,articles.get(i));
                }
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleContainer.removeAllViews();
                for(int i=0 ; i<4 ; i++){
                    buttons[i].setBackgroundColor(white);
                    buttons[i].setTextColor(black);
                }
                buttons[1].setBackgroundColor(green);
                buttons[1].setTextColor(white);
                for(int i=0 ; i<articles.size() ; i++){
                    if(articles.get(i).type.equals("Guides"))
                        addArticleCard(articleContainer,articles.get(i));
                }
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleContainer.removeAllViews();
                for(int i=0 ; i<4 ; i++){
                    buttons[i].setBackgroundColor(white);
                    buttons[i].setTextColor(black);
                }
                buttons[2].setBackgroundColor(green);
                buttons[2].setTextColor(white);
                for(int i=0 ; i<articles.size() ; i++){
                    if(articles.get(i).type.equals("Your Rights"))
                        addArticleCard(articleContainer,articles.get(i));
                }
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleContainer.removeAllViews();
                for(int i=0 ; i<4 ; i++){
                    buttons[i].setBackgroundColor(white);
                    buttons[i].setTextColor(black);
                }
                buttons[3].setBackgroundColor(green);
                buttons[3].setTextColor(white);
                for(int i=0 ; i<articles.size() ; i++){
                    if(articles.get(i).type.equals("Discrimination"))
                        addArticleCard(articleContainer,articles.get(i));
                }
            }
        });

        articleContainer.removeAllViews();
        for(int i=0 ; i<articles.size() ; i++){
            addArticleCard(articleContainer,articles.get(i));
        }

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(uploadArticlePage,getParentFragmentManager());
            }
        });
    }
    public void addArticleCard(LinearLayout container, Article article){
        View card = LayoutInflater.from(getActivity()).inflate(R.layout.card_article_discover_page,container,false);
        TextView articleCaption = card.findViewById(R.id.articleCaption);
        TextView articleAuthorDate = card.findViewById(R.id.authorDates);
        ImageView articleImage = card.findViewById(R.id.ArticleImage);

        articleCaption.setText(article.caption);
        articleAuthorDate.setText(article.author + " | " + article.date);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(article.url));
                startActivity(intent);
            }
        });
        db.loadImageFromStorage(card.getContext(), article.imageURL, articleImage);
        container.addView(card);
    }

    @Override
    public void onResume() {
        super.onResume();
        db.deleteImageFromFirebase("ArticlesIcon/" + (latestArticleIndex+1) + ".jpg");
    }
}