package com.example.openshelves.Fragments.myBooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.openshelves.R;
import com.example.openshelves.StatisticsList;
import com.example.openshelves.helper.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class BookDetailsAR extends Fragment {

    Button btnAR;
    String genre;
    String selectedShelf = "Already Read";
    ImageView imgBookCover;
    RatingBar ratingBar;
    TextView tvTitle, tvEditura, tvAutori, tvDescription, tvGenre, tvPages, tvTime, tvRatingBar, tvProgressScreen;
    ImageButton btnBack;
    String volumeId = "";
    Integer fragmentStatistici = 0;
    FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private DatabaseReference reference1, reference2, reference3, reference4;
    ProgressBar progressScreen;
    LinearLayout linearLayout;

    public BookDetailsAR() {
        // Required empty public constructor
    }

    public static BookDetailsAR newInstance(String param1, String param2) {
        BookDetailsAR fragment = new BookDetailsAR();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            volumeId = getArguments().getString("volume_id");
            fragmentStatistici = getArguments().getInt("fragmentStatistici");
            displayBookItem(volumeId, getContext());
        }
    }

    private void displayBookItem(String volumeId, Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String UIDCurrentUser = firebaseUser.getUid();

        //referinta la obiectul utilizator exact cu id-ul logat
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser).child("books");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(volumeId).getValue() == null) return;
                String title = dataSnapshot.child(volumeId).child("title").getValue().toString();
                String bookCover = dataSnapshot.child(volumeId).child("coverLink").getValue().toString();
                String publisher = dataSnapshot.child(volumeId).child("publisher").getValue().toString();
                String author = dataSnapshot.child(volumeId).child("author").getValue().toString();
                genre = dataSnapshot.child(volumeId).child("genre").getValue().toString();
                String totalNoPages = dataSnapshot.child(volumeId).child("totalNoPages").getValue().toString();
                String time = dataSnapshot.child(volumeId).child("time").getValue().toString();
                Integer rating = Integer.parseInt(dataSnapshot.child(volumeId).child("noStars").getValue().toString());
                String description = dataSnapshot.child(volumeId).child("description").getValue().toString();
                String description2 = Html.fromHtml(description).toString();
                tvTitle.setText(title);
                tvEditura.setText(publisher);
                tvAutori.setText("by " + author);
                tvGenre.setText(genre);
                tvPages.setText(totalNoPages + " pages");
                tvTime.setText(time);
                ratingBar.setRating(rating);

                tvDescription.setText(description2);

                if (bookCover.equalsIgnoreCase(Constant.N0_IMAGE_PLACEHOLDER)) {
                    Glide.with(context).load(bookCover)
                            .into(imgBookCover);
                } else {
                    Glide.with(context).load(bookCover).placeholder(R.color.white).into(imgBookCover);
                }

                progressScreen.setVisibility(View.GONE);
                tvProgressScreen.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_details_a_r, container, false);

        btnAR = view.findViewById(R.id.btnAddBook);
        btnBack = view.findViewById(R.id.btnBack);
        imgBookCover = view.findViewById(R.id.bookCover);
        tvTitle = view.findViewById(R.id.titleBook);
        tvEditura = view.findViewById(R.id.editura);
        tvAutori = view.findViewById(R.id.author);
        tvGenre = view.findViewById(R.id.tvGenre);
        tvPages = view.findViewById(R.id.tvPages);
        tvTime = view.findViewById(R.id.tvTime);
        tvRatingBar = view.findViewById(R.id.tvRating);
        ratingBar = view.findViewById(R.id.ratingBar);
        tvDescription = view.findViewById(R.id.bookDescription);
        tvProgressScreen=view.findViewById(R.id.tvProgressScreen);
        linearLayout=view.findViewById(R.id.linearLayout);
        progressScreen=view.findViewById(R.id.progressScreen);

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


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String UIDCurrentUser = firebaseUser.getUid();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                reference1 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("noStars");
                reference1.setValue(ratingBar.getRating());
            }
        });

        btnAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fragmentStatistici == 1) {
                    Bundle bundle = new Bundle();
                    if (genre.trim().equalsIgnoreCase("Fiction")) {
                        bundle.putInt("statusKey", 1);
                    }
                    if (genre.trim().equalsIgnoreCase("Science")) {
                        bundle.putInt("statusKey", 2);
                    }
                    if (genre.trim().equalsIgnoreCase("Biography")) {
                        bundle.putInt("statusKey", 3);
                    }
                    if (!genre.trim().equalsIgnoreCase("Fiction") && !genre.trim().equalsIgnoreCase("Science") && !genre.trim().equalsIgnoreCase("Biography")) {
                        bundle.putInt("statusKey", 4);
                    }
                    StatisticsList fragment = new StatisticsList();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerDetailsAR, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("statusKey", 2); //AR trimite statusul 2
                    BooksByStatusFragment fragment = new BooksByStatusFragment();
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerDetailsAR, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        return view;
    }

    private void showOptionsDialog() {
        String[] bookshelves = {"Currently Reading", "Want to Read"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Bookshelf");

        builder.setSingleChoiceItems(bookshelves, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedShelf = bookshelves[which];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //schimbare status
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String UIDCurrentUser = firebaseUser.getUid();
                reference2 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("status");
                reference3 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("dateStarted");
                reference4 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("currentNoPages");

                //schimbare fragment
                if (selectedShelf.equalsIgnoreCase("Currently Reading")) {
                    reference2.setValue("Currently Reading");
                    Date date = new Date();
                    String stringDate = DateFormat.getDateInstance().format(date);
                    reference3.setValue(stringDate);
                    reference4.setValue(0);
                    Bundle bundle = new Bundle();
                    bundle.putString("volume_id", volumeId);
                    BookDetailsCR fragment = new BookDetailsCR();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerDetailsAR, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                if (selectedShelf.equalsIgnoreCase("Want to Read")) {
                    reference2.setValue("Want to Read");
                    Date date = new Date();
                    String stringDate = DateFormat.getDateInstance().format(date);
                    reference3.setValue(stringDate);
                    reference4.setValue(0);
                    Bundle bundle = new Bundle();
                    bundle.putString("volume_id", volumeId);
                    BookDetailsWR fragment = new BookDetailsWR();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerDetailsAR, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                dialog.dismiss();

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
}