package com.example.openshelves;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.openshelves.quiz.QuizFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class StatisticsFragment extends Fragment {
    private TextView tvNoBooksFiction, tvNoBooksScience, tvNoBooksBiography, tvNoBooksOther, tvNoBooksTotal, tvName, tvProgressScreen, tvReader;
    private ProgressBar pbar1, pbar2, pbar3, pbar4;
    ImageButton btnBack;
    ImageView imgFiction, imgScience, imgBiography, imgOther, imgQuiz;
    Integer booksFiction=0, booksScience=0, booksBiography=0, booksOther=0;
    private DatabaseReference reference, reference1;
    private ArrayList<BookFB> booksList;
    private FirebaseAuth firebaseAuth;
    ConstraintLayout constraintLayout;
    ProgressBar progressScreen;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        View view= inflater.inflate(R.layout.fragment_statistics, container, false);
        tvNoBooksFiction=view.findViewById(R.id.tvNoBooksFiction);
        tvNoBooksScience=view.findViewById(R.id.tvNoBooksScience);
        tvNoBooksBiography=view.findViewById(R.id.tvNoBooksBiography);
        tvNoBooksOther=view.findViewById(R.id.tvNoBooksOther);
        tvNoBooksTotal=view.findViewById(R.id.tvNoBooksTotal);
        tvReader=view.findViewById(R.id.tvReader);
        tvName=view.findViewById(R.id.tvNumeUtilizator);
        pbar1=view.findViewById(R.id.pBar);
        pbar2=view.findViewById(R.id.pBar2);
        pbar3=view.findViewById(R.id.pBar3);
        pbar4=view.findViewById(R.id.pBar4);
        constraintLayout=view.findViewById(R.id.constraint2);
        progressScreen=view.findViewById(R.id.progressScreen);
        tvProgressScreen=view.findViewById(R.id.tvProgressScreen);
        imgFiction=view.findViewById(R.id.imgFiction);
        imgScience=view.findViewById(R.id.imgScience);
        imgBiography=view.findViewById(R.id.imgBiography);
        imgOther=view.findViewById(R.id.imgOther);
        imgQuiz=view.findViewById(R.id.imgQuiz);

        imgQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizFragment fragment = new QuizFragment();
                //incepe tranzitia
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerStatistics, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Integer noQuote=(int)(Math.random() * ((4 - 0) + 1));
        switch (noQuote){
            case 1:
                tvProgressScreen.setText(getString(R.string.quoteP1));
                break;
            case 2:
                tvProgressScreen.setText(getString(R.string.quoteP2));
                break;
            case 3:
                tvProgressScreen.setText(getString(R.string.quoteP3));
                break;
            case 4:
                tvProgressScreen.setText(getString(R.string.quoteP4));
                break;
            default:
                tvProgressScreen.setText(getString(R.string.quoteP0));
        }

        imgFiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("statusKey",1);
                StatisticsList fragment=new StatisticsList();
                fragment.setArguments(bundle);

                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerStatistics,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imgScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("statusKey",2);
                StatisticsList fragment=new StatisticsList();
                fragment.setArguments(bundle);

                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerStatistics,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imgBiography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("statusKey",3);
                StatisticsList fragment=new StatisticsList();
                fragment.setArguments(bundle);

                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerStatistics,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imgOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("statusKey",4);
                StatisticsList fragment=new StatisticsList();
                fragment.setArguments(bundle);

                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerStatistics,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        btnBack=view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerStatistics, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        booksList = new ArrayList<>();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String UIDCurrentUser = firebaseUser.getUid();

        //referinta la obiectul utilizator exact cu id-ul logat
        reference1= FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullname= dataSnapshot.child("fullname").getValue().toString();
                tvName.setText(fullname.substring(0,1).toUpperCase()+fullname.substring(1));
                String readerType=dataSnapshot.child("reader").getValue().toString();
                if(readerType!=null && !readerType.isEmpty() && !readerType.equalsIgnoreCase("")){
                    tvReader.setVisibility(View.VISIBLE);
                    tvReader.setText(readerType);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser).child("books");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) return;

                Map<String, Object> books = (Map<String, Object>) dataSnapshot.getValue();

                //iterate through each user, ignoring their UID
                for (Map.Entry<String, Object> entry : books.entrySet()) {
                    Map singleBook = (Map) entry.getValue();
                    booksList.add(new BookFB(singleBook.get("title").toString(), singleBook.get("author").toString(), singleBook.get("publisher").toString(),
                            singleBook.get("time").toString(),singleBook.get("genre").toString(),singleBook.get("status").toString(), singleBook.get("coverLink").toString(),
                            singleBook.get("description").toString(), Integer.parseInt(singleBook.get("totalNoPages").toString()) ,
                            Integer.parseInt(singleBook.get("currentNoPages").toString()), singleBook.get("dateFinished").toString(),singleBook.get("dateStarted").toString(),
                            Integer.parseInt(singleBook.get("noStars").toString()),singleBook.get("id").toString()));
                }

                //filtrare nr carti dupa gen
                for(BookFB book:booksList){
                    if(book.getGenre().trim().equalsIgnoreCase("Fiction")){
                        booksFiction++;
                    }
                    if(book.getGenre().trim().equalsIgnoreCase("Biography")){
                        booksBiography++;
                    }
                    if(book.getGenre().trim().equalsIgnoreCase("Science")){
                        booksScience++;
                    }
                    if(!book.getGenre().trim().equalsIgnoreCase("Science") && !book.getGenre().trim().equalsIgnoreCase("Biography") && !book.getGenre().trim().equalsIgnoreCase("Fiction")){
                        booksOther++;
                    }
                }
                tvNoBooksTotal.setText(booksList.size()+" books added");
                tvNoBooksFiction.setText(booksFiction+" books");
                tvNoBooksBiography.setText(booksBiography+" books");
                tvNoBooksScience.setText(booksScience+" books");
                tvNoBooksOther.setText(booksOther+" books");

                Integer progress1=booksFiction*100/booksList.size();
                Integer progress2=booksScience*100/booksList.size();
                Integer progress3=booksBiography*100/booksList.size();
                Integer progress4=booksOther*100/booksList.size();
                pbar1.setProgress(progress1);
                pbar2.setProgress(progress2);
                pbar3.setProgress(progress3);
                pbar4.setProgress(progress4);

                progressScreen.setVisibility(View.GONE);
                tvProgressScreen.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);


                reference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}