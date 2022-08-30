package com.example.openshelves;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.openshelves.Fragments.myBooks.BooksByStatusFragment;
import com.example.openshelves.accountDetails.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class MyBooksFragment extends Fragment {

    private ImageButton btnLogout;
    private ImageButton btnAdd;
    private TextView tvNoBooksCR, tvNoBooksAR, tvNoBooksWR, tvSeeAllCR, tvSeeAllAR, tvSeeAllWR, tvProgressScreen;
    private DatabaseReference reference;
    private ArrayList<BookFB> booksList;
    private ArrayList<BookFB> booksCurrentlyReading;
    private ArrayList<BookFB> booksAlreadyRead;
    private ArrayList<BookFB> booksWantToRead;
    ConstraintLayout constraintLayout;
    ProgressBar progressScreen;

    //arraylist-uri pentru imagini
    private ArrayList<String> bookCoversCR;
    private ArrayList<String> bookCoversWR;
    private ArrayList<String> bookCoversAR;

    private RecyclerView RVCurrentlyReading, RVWantToRead, RVAlreadyRead;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    public MyBooksFragment() {
        // Required empty public constructor
    }


    public static MyBooksFragment newInstance(String param1, String param2) {
        MyBooksFragment fragment = new MyBooksFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_books, container, false);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAdd = view.findViewById(R.id.btnAdd);
        tvNoBooksCR=view.findViewById(R.id.tvNoBooksCR);
        tvNoBooksWR=view.findViewById(R.id.tvNoBooksWR);
        tvNoBooksAR=view.findViewById(R.id.tvNoBooksAR);

        tvSeeAllCR=view.findViewById(R.id.tvSeeAllCR);
        tvSeeAllAR=view.findViewById(R.id.tvSeeAllAR);
        tvSeeAllWR=view.findViewById(R.id.tvSeeAllWR);
        constraintLayout=view.findViewById(R.id.constraint2);
        progressScreen=view.findViewById(R.id.progressScreen);
        tvProgressScreen=view.findViewById(R.id.tvProgressScreen);


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

        tvSeeAllCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("statusKey",1); //CR trimite statusul 1
                BooksByStatusFragment fragment=new BooksByStatusFragment();
                fragment.setArguments(bundle);
                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerMyBooks,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tvSeeAllAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("statusKey",2); //AR trimite statusul 2
                BooksByStatusFragment fragment=new BooksByStatusFragment();
                fragment.setArguments(bundle);
                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerMyBooks,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tvSeeAllWR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("statusKey",3); //WR trimite statusul 3
                BooksByStatusFragment fragment=new BooksByStatusFragment();
                fragment.setArguments(bundle);

                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerMyBooks,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        RVCurrentlyReading=view.findViewById(R.id.RVCurrentlyReading);
        RVCurrentlyReading.setHasFixedSize(true);
        RVCurrentlyReading.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        RVWantToRead=view.findViewById(R.id.RVWantToRead);
        RVWantToRead.setHasFixedSize(true);
        RVWantToRead.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        RVAlreadyRead=view.findViewById(R.id.RVAlreadyRead);
        RVAlreadyRead.setHasFixedSize(true);
        RVAlreadyRead.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        firebaseAuth = FirebaseAuth.getInstance();

        booksList = new ArrayList<>();
        booksCurrentlyReading = new ArrayList<>();
        booksAlreadyRead = new ArrayList<>();
        booksWantToRead = new ArrayList<>();

        //imagini
        bookCoversCR = new ArrayList<>();
        bookCoversAR = new ArrayList<>();
        bookCoversWR = new ArrayList<>();

        checkUser();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment fragment = new SearchFragment();
                //incepe tranzitia
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerMyBooks, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String UIDCurrentUser = firebaseUser.getUid();

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

                booksCurrentlyReading = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getStatus().equalsIgnoreCase("currently reading")).collect(Collectors.toList());
                booksAlreadyRead = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getStatus().equalsIgnoreCase("already read")).collect(Collectors.toList());
                booksWantToRead = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getStatus().equalsIgnoreCase("want to read")).collect(Collectors.toList());

                bookCoversCR.clear();
                for(BookFB book:booksCurrentlyReading){
                    bookCoversCR.add(book.getCoverLink());
                }

                bookCoversAR.clear();
                for(BookFB book:booksAlreadyRead){
                    bookCoversAR.add(book.getCoverLink());
                }

                bookCoversWR.clear();
                for(BookFB book:booksWantToRead){
                    bookCoversWR.add(book.getCoverLink());
                }

                MyBooksAdapter myBooksAdapter1=new MyBooksAdapter(bookCoversCR,getContext());
                MyBooksAdapter myBooksAdapter2=new MyBooksAdapter(bookCoversWR,getContext());
                MyBooksAdapter myBooksAdapter3=new MyBooksAdapter(bookCoversAR,getContext());

                if(booksCurrentlyReading.size()==1){
                    tvNoBooksCR.setText(booksCurrentlyReading.size()+" book");
                }
                else if(booksCurrentlyReading==null || booksCurrentlyReading.size()==0){
                    tvNoBooksCR.setText("No books added");
                }
                else{
                    tvNoBooksCR.setText(booksCurrentlyReading.size()+" books");
                }

                if(booksWantToRead.size()==1){
                    tvNoBooksWR.setText(booksWantToRead.size()+" book");
                }
                else if(booksWantToRead==null || booksWantToRead.size()==0){
                    tvNoBooksWR.setText("No books added");
                }
                else{
                    tvNoBooksWR.setText(booksWantToRead.size()+" books");
                }

                if(booksAlreadyRead.size()==1){
                    tvNoBooksAR.setText(booksAlreadyRead.size()+" book");
                }
                else if(booksAlreadyRead==null || booksAlreadyRead.size()==0){
                    tvNoBooksAR.setText("No books added");
                }
                else{
                    tvNoBooksAR.setText(booksAlreadyRead.size()+" books");
                }

                RVCurrentlyReading.setAdapter(myBooksAdapter1);
                RVAlreadyRead.setAdapter(myBooksAdapter3);
                RVWantToRead.setAdapter(myBooksAdapter2);


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

    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            //not logged in, go to login screen
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}