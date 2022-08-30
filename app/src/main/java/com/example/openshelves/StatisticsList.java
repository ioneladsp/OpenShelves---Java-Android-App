package com.example.openshelves;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.openshelves.Fragments.myBooks.BookDetailsAR;
import com.example.openshelves.Fragments.myBooks.BookDetailsCR;
import com.example.openshelves.Fragments.myBooks.BookDetailsWR;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsList extends Fragment implements RecyclerViewInterfaceSearch{

    private ImageButton btnBack, btnSort;
    private String UIDCurrentUser, status="";
    private TextView tvSortedBy, tvStatus;
    private int counter = 0;
    private RecyclerView booksByStatusRV;

    private int a = 0;

    private BooksByStatusRV booksByStatusAdapter;


    LinearLayoutManager layoutManager;

    private DatabaseReference reference,reference1;
    private FirebaseAuth firebaseAuth;

    private ArrayList<BookFB> booksList;
    private ArrayList<BookFB> booksFiction;
    private ArrayList<BookFB> booksScience;
    private ArrayList<BookFB> booksBiography;
    private ArrayList<BookFB> booksOther;

    int pos;


    public StatisticsList() {
        // Required empty public constructor
    }

    public static StatisticsList newInstance(String param1, String param2) {
        StatisticsList fragment = new StatisticsList();
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
        View view = inflater.inflate(R.layout.fragment_statistics_list, container, false);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvSortedBy = view.findViewById(R.id.tvSortedBy);
        btnBack = view.findViewById(R.id.btnBack);
        btnSort = view.findViewById(R.id.btnSort);
        pos = getArguments().getInt("statusKey");
        if (pos == 1) {
            tvStatus.setText("Fiction");
        }
        if (pos == 2) {
            tvStatus.setText("Science");
        }
        if(pos==3){
            tvStatus.setText("Biography");
        }
        if(pos==4){
            tvStatus.setText("Other");
        }


        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if (counter % 2 == 1 && pos == 1) {
                    Collections.sort(booksFiction,  ((o1, o2) -> o2.getTotalNoPages() - o1.getTotalNoPages()));
                    setUpBooksByStatusResults(booksFiction);

                } else if (counter % 2 == 0 && pos == 1) {
                    Collections.sort(booksFiction,  ((o1, o2) -> o1.getTotalNoPages() - o2.getTotalNoPages()));
                    setUpBooksByStatusResults(booksFiction);
                }
                if (counter % 2 == 1 && pos == 2) {
                    Collections.sort(booksScience, ((o1, o2) -> o2.getTotalNoPages() - o1.getTotalNoPages()));
                    setUpBooksByStatusResults(booksScience);
                } else if (counter % 2 == 0 && pos == 2) {
                    Collections.sort(booksScience, ((o1, o2) -> o1.getTotalNoPages() - o2.getTotalNoPages()));
                    setUpBooksByStatusResults(booksScience);
                }
                if (counter % 2 == 1 && pos == 3) {
                    Collections.sort(booksBiography, ((o1, o2) -> o2.getTotalNoPages() - o1.getTotalNoPages()));
                    setUpBooksByStatusResults(booksBiography);
                } else if (counter % 2 == 0 && pos == 3) {
                    Collections.sort(booksBiography, ((o1, o2) -> o1.getTotalNoPages() - o2.getTotalNoPages()));
                    setUpBooksByStatusResults(booksBiography);
                }
                if (counter % 2 == 1 && pos == 4) {
                    Collections.sort(booksOther, ((o1, o2) -> o2.getTotalNoPages() - o1.getTotalNoPages()));
                    setUpBooksByStatusResults(booksOther);
                } else if (counter % 2 == 0 && pos == 4) {
                    Collections.sort(booksOther, ((o1, o2) -> o1.getTotalNoPages() - o2.getTotalNoPages()));
                    setUpBooksByStatusResults(booksOther);
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsFragment fragment = new StatisticsFragment();
                //incepe tranzitia
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.statisticsList, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        booksByStatusRV = view.findViewById(R.id.booksByStatusRV);
        booksByStatusRV.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        firebaseAuth = FirebaseAuth.getInstance();

        booksList = new ArrayList<>();
        booksFiction = new ArrayList<>();
        booksScience = new ArrayList<>();
        booksBiography = new ArrayList<>();
        booksOther = new ArrayList<>();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UIDCurrentUser = firebaseUser.getUid();

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
                    booksList.add(new BookFB(singleBook.get("title").toString(), singleBook.get("author").toString(), singleBook.get("publisher").toString(), singleBook.get("time").toString(), singleBook.get("genre").toString(), singleBook.get("status").toString(), singleBook.get("coverLink").toString(),
                            singleBook.get("description").toString(), Integer.parseInt(singleBook.get("totalNoPages").toString()), Integer.parseInt(singleBook.get("currentNoPages").toString()),
                            singleBook.get("dateFinished").toString(), singleBook.get("dateStarted").toString(), Integer.parseInt(singleBook.get("noStars").toString()), singleBook.get("id").toString()));
                }

                booksFiction = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getGenre().trim().equalsIgnoreCase("Fiction")).collect(Collectors.toList());
                booksScience = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getGenre().trim().equalsIgnoreCase("Science")).collect(Collectors.toList());
                booksBiography = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getGenre().trim().equalsIgnoreCase("Biography")).collect(Collectors.toList());
                booksOther = (ArrayList<BookFB>) booksList.stream().filter(b -> !b.getGenre().trim().equalsIgnoreCase("Fiction") && !b.getGenre().trim().equalsIgnoreCase("Science") && !b.getGenre().trim().equalsIgnoreCase("Biography")).collect(Collectors.toList());

                //preluare date pentru a stii cu ce carti sa populez rv (din ce categorie-status)
                int pos = getArguments().getInt("statusKey");
                if (pos == 1) {
                    Collections.sort(booksFiction, Comparator.comparingInt(BookFB::getTotalNoPages));
                    setUpBooksByStatusResults(booksFiction);
                }
                if (pos == 2) {
                    //populare recyclerview
                    Collections.sort(booksScience, Comparator.comparingInt(BookFB::getTotalNoPages));
                    setUpBooksByStatusResults(booksScience);
                }
                if (pos == 3) {
                    //populare recyclerview
                    Collections.sort(booksBiography, Comparator.comparingInt(BookFB::getTotalNoPages));
                    setUpBooksByStatusResults(booksBiography);
                }
                if (pos == 4) {
                    //populare recyclerview
                    Collections.sort(booksOther, Comparator.comparingInt(BookFB::getTotalNoPages));
                    setUpBooksByStatusResults(booksOther);
                }

                reference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return view;
    }

    private void setUpBooksByStatusResults(List<BookFB> itemList) {
        booksByStatusAdapter = new BooksByStatusRV(getContext(), itemList, this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        booksByStatusRV.setLayoutManager(layoutManager);
        booksByStatusRV.setAdapter(booksByStatusAdapter);
    }

    @Override
    public void onItemClick(String pos) {
        //trebuie sa stiu ce fragment sa deschid
        //interoghez firebase in functie de id si verific ce status are
        reference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser).child("books").child(pos.trim());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("status").getValue().toString();
                reference1.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if(status.equalsIgnoreCase("Currently Reading")){
            BookDetailsCR fragment = new BookDetailsCR();
            final Bundle bundle = new Bundle();
            bundle.putString("volume_id", pos);
            bundle.putInt("fragmentStatistici",1);
            fragment.setArguments(bundle);
            //incepe tranzitia
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.statisticsList, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        if(status.equalsIgnoreCase("Want to Read")){
            BookDetailsWR fragment = new BookDetailsWR();
            final Bundle bundle = new Bundle();
            bundle.putString("volume_id", pos);
            bundle.putInt("fragmentStatistici",1);
            fragment.setArguments(bundle);
            //incepe tranzitia
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.statisticsList, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        if(status.equalsIgnoreCase("Already Read")){
            BookDetailsAR fragment = new BookDetailsAR();
            final Bundle bundle = new Bundle();
            bundle.putString("volume_id", pos);
            bundle.putInt("fragmentStatistici",1);
            fragment.setArguments(bundle);
            //incepe tranzitia
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.statisticsList, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


    }
}