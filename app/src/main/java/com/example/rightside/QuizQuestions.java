package com.example.rightside;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.dailyQuizScore;
import static com.example.rightside.Manager.db;
import static com.example.rightside.Manager.goToPage;
import static com.example.rightside.Manager.quizIntroPage;
import static com.example.rightside.Manager.stack;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizQuestions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizQuestions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View submitButton;
    private PopupWindow popupWindow;
    private List<Questions> questionsList;

    public QuizQuestions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizQuestions.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizQuestions newInstance(String param1, String param2) {
        QuizQuestions fragment = new QuizQuestions();
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
        return inflater.inflate(R.layout.fragment_quiz_questions, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionsList = new ArrayList<>();

        LinearLayout questionsLayout = view.findViewById(R.id.QuestionsLayout);
        Button submitButton = view.findViewById(R.id.SubmitQuizButton);
        RadioGroup optionGroup = view.findViewById(R.id.Options);
        RadioButton option1 = view.findViewById(R.id.Option1);
        RadioButton option2 = view.findViewById(R.id.Option2);
        RadioButton option3 = view.findViewById(R.id.Option3);
        RadioButton option4 = view.findViewById(R.id.Option4);



        db.getCollection("Quiz Sets").document("1").collection("Questions").get()
                .addOnSuccessListener(documents -> {
                    questionsList.clear();
                    for (QueryDocumentSnapshot document : documents) {
                        List<String> options = (List<String>) document.get("answer_choices");
                        questionsList.add(new Questions(document.getString("question"), options, document.getLong("correct_option_index").intValue()));
                    }
                    for (int i = 0; i < questionsList.size(); i++) {
                        addQuestionCard(questionsLayout, questionsList, i, submitButton);
                    }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswers(v, questionsLayout);
            }
        });
    }

    private void checkAnswers(View v,LinearLayout questionsLayout) {
        Manager.dailyQuizScore = 0;
        for (int i = 0; i < 5; i++) {
            //gets cardview
            View childView = questionsLayout.getChildAt(i);
            if (!(childView instanceof CardView)) {
                Log.e("Error", "Not a CardView: " + childView.getClass().getSimpleName());
                return;
            }

            CardView cardView = (CardView) childView;
            //get the linear layout & radio group
            LinearLayout linearLayout = cardView.findViewById(R.id.QuestionCardLayout);
            RadioGroup radioGroup = linearLayout.findViewById(R.id.Options);

            if (radioGroup == null) {
                Log.e("Error", "Radio group not found in card view: " + cardView.getClass().getSimpleName());
                return;
            }
            int selectedOptionId = radioGroup.getCheckedRadioButtonId();
            if (selectedOptionId == -1){
                Toast.makeText(requireContext(), "Answer all questions first.", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadioButton = radioGroup.findViewById(selectedOptionId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            String correctAnswer = questionsList.get(i).getOptions().get(questionsList.get(i).getCorrectAnswerIndex());
            if (selectedAnswer.equals(correctAnswer)){
                dailyQuizScore++;
            }
        }

        if(currentUser.dailyQuizScore<=dailyQuizScore){
            currentUser.dailyQuizScore = dailyQuizScore;

            db.getCollection("Users").document(Integer.toString(currentUser.userId)).update("daily_quiz_score",dailyQuizScore);
        }

        showPopup(v, dailyQuizScore);
    }

    private void showPopup(View v, int dailyQuizScore) {
        // Inflate the popup_layout.xml
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout_quiz, null);
        TextView popupScore = popupView.findViewById(R.id.popupScore);

        popupScore.setText("You answered "+dailyQuizScore+"/5 questions correctly");

        // Create the PopupWindow
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        // Set up the close button
        Button closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // Show the PopupWindow
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        WindowManager.LayoutParams params = requireActivity().getWindow().getAttributes();
        params.alpha = 0.5f; // Reduce opacity
        requireActivity().getWindow().setAttributes(params);

        popupWindow.setOnDismissListener(() -> {
            params.alpha = 1f; // Restore opacity
            requireActivity().getWindow().setAttributes(params);

            for (int i = 0; i < 2; i++) {
                stack.removeFirst();
            }
            goToPage(quizIntroPage, getParentFragmentManager());
        });
    }

    private void addQuestionCard(LinearLayout questionsLayout, List<Questions> questionsList, int index, Button submitButton) {
        View cardView = LayoutInflater.from(questionsLayout.getContext()).inflate(R.layout.card_quiz_question, questionsLayout, false);

        TextView questionText = cardView.findViewById(R.id.QuestionText);
        RadioButton option1 = cardView.findViewById(R.id.Option1);
        RadioButton option2 = cardView.findViewById(R.id.Option2);
        RadioButton option3 = cardView.findViewById(R.id.Option3);
        RadioButton option4 = cardView.findViewById(R.id.Option4);

        questionText.setText(index+1 + ". " + questionsList.get(index).getQuestionText());
        option1.setText(questionsList.get(index).getOptions().get(0));
        option2.setText(questionsList.get(index).getOptions().get(1));
        option3.setText(questionsList.get(index).getOptions().get(2));
        option4.setText(questionsList.get(index).getOptions().get(3));

        int layoutIndex = questionsLayout.indexOfChild(submitButton);

        questionsLayout.addView(cardView,layoutIndex);

    }
}