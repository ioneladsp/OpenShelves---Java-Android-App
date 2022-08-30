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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class BookDetailsCR extends Fragment {

    Button btnCR, btnUpdateProgress;
    ImageView imgBookCover;
    String genre;
    Integer fragmentStatistici=0;
    TextView tvTitle, tvEditura, tvAutori, tvDescription, tvGenre, tvPages, tvTime, tvDateStarted, tvProgress, tvProgressPages, tvProgressScreen;
    ProgressBar progressBar;
    Context context;
    ImageButton btnBack;
    String volumeId = "";
    String selectedShelf="Currently Reading";
    String totalNoPages;
    FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private DatabaseReference reference1;
    private DatabaseReference reference2, reference3,reference4;
    ProgressBar progressScreen;
    LinearLayout linearLayout;

    public BookDetailsCR() {
        // Required empty public constructor
    }

    public static BookDetailsCR newInstance(String param1, String param2) {
        BookDetailsCR fragment = new BookDetailsCR();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            volumeId = getArguments().getString("volume_id");
            fragmentStatistici=getArguments().getInt("fragmentStatistici");
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
                totalNoPages = dataSnapshot.child(volumeId).child("totalNoPages").getValue().toString();
                String time = dataSnapshot.child(volumeId).child("time").getValue().toString();
                String dateStarted = dataSnapshot.child(volumeId).child("dateStarted").getValue().toString();
                Integer currentPage=Integer.parseInt(dataSnapshot.child(volumeId).child("currentNoPages").getValue().toString());
                Integer nrPagesProgress = Integer.parseInt(dataSnapshot.child(volumeId).child("currentNoPages").getValue().toString()) * 100 / Integer.parseInt(dataSnapshot.child(volumeId).child("totalNoPages").getValue().toString());
                String description = dataSnapshot.child(volumeId).child("description").getValue().toString();
                String description2 = Html.fromHtml(description).toString();
                tvTitle.setText(title);
                tvEditura.setText(publisher);
                tvAutori.setText("by " + author);
                tvGenre.setText(genre);
                tvPages.setText(totalNoPages + " pages");
                tvTime.setText(time);
                tvDateStarted.setText("You started reading this book on: " + dateStarted);
                if (nrPagesProgress >= Integer.parseInt(totalNoPages)) {
                    tvProgress.setText("You are 100% done");
                } else {
                    tvProgress.setText("You are " + nrPagesProgress + "% done");
                }
                if (nrPagesProgress >= Integer.parseInt(totalNoPages)) {
                    progressBar.setProgress(100);
                } else {
                    progressBar.setProgress(nrPagesProgress);
                }
                tvProgressPages.setText(currentPage+"/"+totalNoPages);
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
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_book_details_c_r, container, false);
        btnCR = view.findViewById(R.id.btnAddBook);
        btnBack = view.findViewById(R.id.btnBack);
        imgBookCover = view.findViewById(R.id.bookCover);
        tvTitle = view.findViewById(R.id.titleBook);
        tvEditura = view.findViewById(R.id.editura);
        tvAutori = view.findViewById(R.id.author);
        tvGenre = view.findViewById(R.id.tvGenre);
        tvPages = view.findViewById(R.id.tvPages);
        tvTime = view.findViewById(R.id.tvTime);
        tvDateStarted = view.findViewById(R.id.tvDateStarted);
        tvProgress = view.findViewById(R.id.tvProgress);
        progressBar = view.findViewById(R.id.pBar);
        tvDescription = view.findViewById(R.id.bookDescription);
        btnUpdateProgress = view.findViewById(R.id.btnUpdateProgress);
        tvProgressPages=view.findViewById(R.id.tvProgressPages);
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


        btnCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();

            }
        });

        btnUpdateProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("I'm currently on page: ");
                final EditText input = new EditText(context);
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String UIDCurrentUser = firebaseUser.getUid();


                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer pages = Integer.parseInt(String.valueOf(input.getText()));
                        reference1 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("currentNoPages");
                        reference2 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("status");
                        if (pages >= Integer.parseInt(totalNoPages)) {
                            reference1.setValue(totalNoPages);
                            reference2.setValue("Already Read");

                            Bundle bundle = new Bundle();
                            bundle.putString("volume_id", volumeId);
                            BookDetailsAR fragment = new BookDetailsAR();
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerDetailsCR, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            reference1.setValue(pages);
                        }
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


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fragmentStatistici==1){
                    Bundle bundle = new Bundle();
                    if(genre.trim().equalsIgnoreCase("Fiction")){
                        bundle.putInt("statusKey", 1);
                    }
                    if(genre.trim().equalsIgnoreCase("Science")){
                        bundle.putInt("statusKey", 2);
                    }
                    if(genre.trim().equalsIgnoreCase("Biography")){
                        bundle.putInt("statusKey", 3);
                    }
                    if(!genre.trim().equalsIgnoreCase("Fiction") && !genre.trim().equalsIgnoreCase("Science") && !genre.trim().equalsIgnoreCase("Biography")){
                        bundle.putInt("statusKey", 4);
                    }
                    StatisticsList fragment = new StatisticsList();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerDetailsCR, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putInt("statusKey", 1);
                    BooksByStatusFragment fragment = new BooksByStatusFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerDetailsCR, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        btnCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });


        return view;
    }

    private void showOptionsDialog() {
        String[] bookshelves = {"Want to Read", "Already Read"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Bookshelf");

        builder.setSingleChoiceItems(bookshelves, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedShelf=bookshelves[which];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String UIDCurrentUser = firebaseUser.getUid();
                reference1 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("noStars");
                reference2 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("status");
                reference3 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("dateStarted");
                reference4 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(volumeId).child("currentNoPages");

                if(selectedShelf.equalsIgnoreCase("Want to Read")){
                    reference1.setValue(0);
                    reference2.setValue("Currently Reading");
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
                    fragmentTransaction.replace(R.id.containerDetailsCR, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                if(selectedShelf.equalsIgnoreCase("Already Read")){
                    reference1.setValue(0);
                    reference2.setValue("Already Read");
                    Date date = new Date();
                    String stringDate = DateFormat.getDateInstance().format(date);
                    reference3.setValue(stringDate);
                    reference4.setValue(0);
                    Bundle bundle = new Bundle();
                    bundle.putString("volume_id", volumeId);
                    BookDetailsAR fragment = new BookDetailsAR();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerDetailsCR, fragment);
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