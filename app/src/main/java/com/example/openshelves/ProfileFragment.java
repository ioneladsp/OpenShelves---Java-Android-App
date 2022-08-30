package com.example.openshelves;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;


public class ProfileFragment extends Fragment {
    //firebase auth
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private DatabaseReference reference1;
    private ProgressBar pbar, progressScreen;
    ImageView imgAvatar;
    Integer goalUser=12;
    private Button btnUpdate;
    private TextView tvName,tvName2, tvNrPagini, tvNrCarti, tvProgress,tvBooks, tvProgressScreen;
    ConstraintLayout constraintLayout;
    Integer nrPagini=0, nrCarti=0;
    private ArrayList<BookFB> booksAlreadyRead;
    private ArrayList<BookFB> booksList;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imgAvatar=view.findViewById(R.id.avatarIcon);
        tvName=view.findViewById(R.id.tvNumeUtilizator);
        tvName2=view.findViewById(R.id.label1Tv);
        tvNrPagini=view.findViewById(R.id.tvNrPagini);
        tvNrCarti=view.findViewById(R.id.tvNrCarti);
        tvProgress=view.findViewById(R.id.tvProgress);
        tvBooks=view.findViewById(R.id.tvBooks);
        pbar=view.findViewById(R.id.pBar);
        btnUpdate=view.findViewById(R.id.btnUpdateProgress);
        progressScreen=view.findViewById(R.id.progressScreen);
        tvProgressScreen=view.findViewById(R.id.tvProgressScreen);
        constraintLayout=view.findViewById(R.id.constraint2);

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsFragment fragment = new StatisticsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerProfile, fragment);
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



        booksList = new ArrayList<>();
        booksAlreadyRead = new ArrayList<>();

        //am preluat utilizatorul
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String UIDCurrentUser=firebaseUser.getUid();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Change Your Reading Goal");
                builder.setMessage("Change the number of books in your annual challenge: ");
                final EditText input = new EditText(getContext());
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //salvare in baza de date a progresului
                        Integer goal = Integer.parseInt(String.valueOf(input.getText()));
                        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser).child("goal");
                        reference.setValue(goal);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.show();
            }
        });


        //referinta la obiectul utilizator exact cu id-ul logat
        reference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser).child("books");
        reference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) return;

                Map<String, Object> books = (Map<String, Object>) dataSnapshot.getValue();

                //iterate through each user, ignoring their UID
                for (Map.Entry<String, Object> entry : books.entrySet()) {
                    Map singleBook = (Map) entry.getValue();
                    booksList.add(new BookFB(singleBook.get("title").toString(), singleBook.get("author").toString(), singleBook.get("publisher").toString(), singleBook.get("time").toString(), singleBook.get("genre").toString(), singleBook.get("status").toString(), singleBook.get("coverLink").toString(),
                            singleBook.get("description").toString(), Integer.parseInt(singleBook.get("totalNoPages").toString()), Integer.parseInt(singleBook.get("currentNoPages").toString()),
                            singleBook.get("dateFinished").toString(), singleBook.get("dateStarted").toString(), Integer.parseInt(singleBook.get("noStars").toString()), singleBook.get("id").toString()));
                }

                booksAlreadyRead = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getStatus().equalsIgnoreCase("already read")).collect(Collectors.toList());

                for(BookFB book:booksAlreadyRead){
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter=new SimpleDateFormat("MMM dd, yyyy");
                    try {
                        Date date=formatter.parse(book.getDateFinished());
                        Date date2=formatter.parse("Jan 01, 2023");
                        assert date != null;
                        if(date.before(date2)){
                            nrPagini+=book.getTotalNoPages();
                            nrCarti+=1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                tvNrCarti.setText(nrCarti+"");
                tvNrPagini.setText(nrPagini+"");

                int progress=nrCarti*100/goalUser;
                pbar.setProgress(progress);
                tvProgress.setText("You are "+progress+"% close to reaching your annual goal");
                tvBooks.setText(nrCarti+"/"+goalUser+"");

                progressScreen.setVisibility(View.GONE);
                tvProgressScreen.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
                reference1.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //referinta la obiectul utilizator exact cu id-ul logat
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullname= dataSnapshot.child("fullname").getValue().toString();
                goalUser=Integer.parseInt(dataSnapshot.child("goal").getValue().toString());
                tvName.setText(fullname.substring(0,1).toUpperCase()+fullname.substring(1));
                //setez progress barul
                Integer progress=nrCarti*100/goalUser;
                pbar.setProgress(progress);
                tvProgress.setText("You are "+progress+"% close to reaching your annual goal");
                tvBooks.setText(nrCarti+"/"+goalUser+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //setez progress barul
        Integer progress=nrCarti*100/goalUser;
        pbar.setProgress(progress);
        tvProgress.setText("You are "+progress+"% close to reaching your annual goal");
        tvBooks.setText(nrCarti+"/"+goalUser+"");

        return view;
    }
}