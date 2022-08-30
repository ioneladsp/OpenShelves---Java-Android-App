package com.example.openshelves.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.openshelves.R;
import com.example.openshelves.StatisticsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuizFragment extends Fragment implements View.OnClickListener {

    ImageButton btnBack;
    TextView tvQuestionNo, tvQuestion;
    Button btnAnswer1, btnAnswer2,btnAnswer3, btnAnswer4;
    Button btnCancel, btnNext;
    Context context;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    Integer score=0;
    Integer totalQuestion=QuestionAnswer.question.length;
    Integer currentQuestionIndex=0;
    String selectedAnswer="";
    String passStatus="None";


    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_quiz, container, false);
        btnBack=view.findViewById(R.id.btnBack);
        btnAnswer1=view.findViewById(R.id.btnAnswer1);
        btnAnswer2=view.findViewById(R.id.btnAnswer2);
        btnAnswer3=view.findViewById(R.id.btnAnswer3);
        btnAnswer4=view.findViewById(R.id.btnAnswer4);
        btnCancel=view.findViewById(R.id.btnCancel);
        btnNext=view.findViewById(R.id.btnNext);
        tvQuestionNo=view.findViewById(R.id.tvQuestionNo);
        tvQuestion=view.findViewById(R.id.tvQuestion);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsFragment fragment = new StatisticsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerQuiz, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnAnswer1.setOnClickListener(this);
        btnAnswer2.setOnClickListener(this);
        btnAnswer3.setOnClickListener(this);
        btnAnswer4.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        context=getContext();


        loadNextQuestion(context);

        return view;

    }

    private void loadNextQuestion(Context context) {
        if(currentQuestionIndex==totalQuestion){
            finishQuiz(context);
            return;
        }
        Integer currentQO=currentQuestionIndex+1;
        tvQuestionNo.setText("Question "+currentQO+"/"+totalQuestion);
        tvQuestion.setText(QuestionAnswer.question[currentQuestionIndex]);
        btnAnswer1.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
        btnAnswer2.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
        btnAnswer3.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
        btnAnswer4.setText(QuestionAnswer.choices[currentQuestionIndex][3]);

    }

    private void finishQuiz(Context context) {
        if(score>=totalQuestion*0.8){
            passStatus="Bibliophile";
        }
        if(score>=totalQuestion*0.5 && score<totalQuestion*0.8){
            passStatus="Intermediate";
        }
        if(score<totalQuestion*0.5){
            passStatus="Beginner";
        }

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String UIDCurrentUser = firebaseUser.getUid();

        //referinta la obiectul utilizator exact cu id-ul logat
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser).child("reader");
        reference.setValue(passStatus);

        if(passStatus.equalsIgnoreCase("Bibliophile")){
            new AlertDialog.Builder(context).setTitle(passStatus)
                    .setMessage("Your score is "+score+" out of "+totalQuestion+". Seems like you are an avid reader and you pay attention to details, being an enthuasiastic for classics.")
                    .setPositiveButton("Retake",(dialogInterface,i)->restartQuiz())
                    .setNegativeButton("Cancel",(dialogInterface2,i2)->finish())
                    .show();
        }
        if(passStatus.equalsIgnoreCase("Intermediate")){
            new AlertDialog.Builder(context).setTitle(passStatus)
                    .setMessage("Your score is "+score+" out of "+totalQuestion+". Seems like you are an intermediate reader with pretty good knowledge of must-read books. You need just a little more focus and drive to become a bibliophile. Check the Discover Screen to boost your inspiration.")
                    .setPositiveButton("Retake",(dialogInterface,i)->restartQuiz())
                    .setNegativeButton("Cancel",(dialogInterface2,i2)->finish())
                    .show();
        }

        if(passStatus.equalsIgnoreCase("Beginner")){
            new AlertDialog.Builder(context).setTitle(passStatus)
                    .setMessage("Your score is "+score+" out of "+totalQuestion+". Seems like you are a beginner so you're in the perfect place. OpenShelves will help you on your journey, inspiring you with thousands of titles and tracking all your progress.")
                    .setPositiveButton("Retake",(dialogInterface,i)->restartQuiz())
                    .setNegativeButton("Cancel",(dialogInterface2,i2)->finish())
                    .show();
        }

    }

    private void finish() {
        StatisticsFragment fragment = new StatisticsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerQuiz, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void restartQuiz() {
        score=0;
        currentQuestionIndex=0;
        loadNextQuestion(context);
    }

    @Override
    public void onClick(View v) {
        btnAnswer1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        btnAnswer2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        btnAnswer3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        btnAnswer4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        Button clickedButton=(Button) v;
        if(clickedButton.getId()==R.id.btnNext){
            currentQuestionIndex++;
            loadNextQuestion(context);
        }
        if(clickedButton.getId()==R.id.btnCancel){
            finish();
        }
        if(clickedButton.getId()!=R.id.btnNext && clickedButton.getId()!=R.id.btnCancel){
            //choices button clicked
            selectedAnswer=clickedButton.getText().toString();
            if(selectedAnswer.equalsIgnoreCase(QuestionAnswer.correctAnswers[currentQuestionIndex])){
                score++;
                clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct, 0, 0, 0);
            }
            else{
                clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong, 0, 0, 0);
                //color the right one with green
                if(btnAnswer1.getText().toString().equalsIgnoreCase(QuestionAnswer.correctAnswers[currentQuestionIndex])){
                    btnAnswer1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct, 0, 0, 0);
                }
                if(btnAnswer2.getText().toString().equalsIgnoreCase(QuestionAnswer.correctAnswers[currentQuestionIndex])){
                    btnAnswer2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct, 0, 0, 0);
                }
                if(btnAnswer3.getText().toString().equalsIgnoreCase(QuestionAnswer.correctAnswers[currentQuestionIndex])){
                    btnAnswer3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct, 0, 0, 0);
                }
                if(btnAnswer4.getText().toString().equalsIgnoreCase(QuestionAnswer.correctAnswers[currentQuestionIndex])){
                    btnAnswer4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct, 0, 0, 0);
                }
            }
        }

    }
}