package com.example.openshelves;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.openshelves.api.Item;
import com.example.openshelves.api.VolumeInfo;
import com.example.openshelves.helper.Constant;
import com.example.openshelves.request.api.RequestService;
import com.example.openshelves.request.api.RetrofitClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookDetailsFragment extends Fragment {
    ImageView bookCover;
    TextView tvTitle, tvEditura, tvAutori, tvDescription, tvGenre, tvPages, tvTime, tvProgressScreen;
    ProgressBar progressScreen;
    LinearLayout linearLayout;
    Call<Item> singleItemCall;
    RequestService requestService;
    Integer totalNoPages;
    Integer timeToRead;
    String category;
    String volumeId = "";
    Button btnAddBook;
    ImageButton btnBack;

    String selectedShelf = "";
    FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    //obiect de tip Book pentru care sa apelez ct in metoda din api
    VolumeInfo volume;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance() {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestService = RetrofitClass.getAPIInstance();
        if (getArguments() != null) {
            volumeId = getArguments().getString("volume_id");
            displayBookItem(volumeId);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        bookCover = view.findViewById(R.id.bookCover);
        tvTitle = view.findViewById(R.id.titleBook);
        tvEditura = view.findViewById(R.id.editura);
        tvAutori = view.findViewById(R.id.author);
        tvGenre = view.findViewById(R.id.tvGenre);
        tvPages = view.findViewById(R.id.tvPages);
        tvTime = view.findViewById(R.id.tvTime);
        tvDescription = view.findViewById(R.id.bookDescription);
        btnAddBook = view.findViewById(R.id.btnAddBook);
        btnBack = view.findViewById(R.id.btnBack);
        linearLayout=view.findViewById(R.id.linearlayout);
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



        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment fragment = new SearchFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerDetails, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    void showOptionsDialog() {
        String[] bookshelves = {"Currently Reading", "Already Read", "Want to Read"};
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
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String UIDCurrentUser = firebaseUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", volume.getTitle());
                if (volume.getAuthors() == null || volume.getAuthors().size() == 0) {
                    hashMap.put("author", "No author");
                } else hashMap.put("author", volume.getAuthors().get(0));
                if (volume.getPublisher() == null || volume.getPublisher().isEmpty()) {
                    hashMap.put("publisher", "No publisher");
                } else hashMap.put("publisher", volume.getPublisher());
                hashMap.put("time", timeToRead + " min");
                hashMap.put("genre", category);
                hashMap.put("status", selectedShelf);
                if (volume.getImageLinks().getThumbnail() == null || volume.getImageLinks().getThumbnail().isEmpty()) {
                    hashMap.put("coverLink", Constant.N0_IMAGE_PLACEHOLDER);
                } else {
                    hashMap.put("coverLink", volume.getImageLinks().getThumbnail());
                }
                if (volume.getDescription() == null || volume.getDescription().isEmpty()) {
                    hashMap.put("description", "No description available");
                } else {
                    hashMap.put("description", volume.getDescription());
                }
                if (volume.getPageCount() == null || volume.getPageCount() < 1) {
                    hashMap.put("totalNoPages", totalNoPages);
                } else {
                    hashMap.put("totalNoPages", volume.getPageCount());
                }
                hashMap.put("currentNoPages", 0);
                Date date = new Date();
                String stringDate = DateFormat.getDateInstance().format(date);
                hashMap.put("dateFinished", stringDate);
                hashMap.put("dateStarted", stringDate);
                hashMap.put("noStars", 0);
                hashMap.put("id", volumeId);
                reference.child(volumeId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //data failed adding to db
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                btnAddBook.setText(selectedShelf);
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

    void displayBookItem(String id) {
        singleItemCall = requestService.getBookItem(id);
        singleItemCall.enqueue(new Callback<Item>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                volume = response.body().getVolumeInfo();
                if (response.isSuccessful()) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String UIDCurrentUser = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UIDCurrentUser).child("books");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(id).getValue() == null) return;
                            String status = dataSnapshot.child(id).child("status").getValue().toString();
                            btnAddBook.setText(status);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    tvTitle.setText(volume.getTitle());
                    tvEditura.setText(volume.getPublisher());
                    if (volume.getCategories() != null && volume.getCategories().size() >= 0) {
                        String categories = volume.getCategories().get(0);
                        String[] categ = categories.split("/");
                        String[] cat = categ[0].split("&");
                        category = cat[0];
                        tvGenre.setText(cat[0]);
                    } else {
                        category = "-";
                        tvGenre.setText("-");
                    }
                    if (volume.getPageCount() != null && volume.getPageCount() > 0) {
                        totalNoPages = volume.getPageCount();
                        tvPages.setText(volume.getPageCount() + " pages");
                    } else {
                        totalNoPages = new Random().nextInt(250) + 20; // [0, 220] + 20 => [20,240]
                        tvPages.setText(totalNoPages + " pages");
                    }
                    timeToRead = Math.toIntExact(Math.round(totalNoPages * 1.7));
                    tvTime.setText(timeToRead + " min");
                    if (volume.getDescription() != null && !volume.getDescription().isEmpty()) {
                        tvDescription.setText(Html.fromHtml(volume.getDescription(), Html.FROM_HTML_MODE_COMPACT));

                    } else {
                        tvDescription.setVisibility(View.GONE);
                    }
                    tvAutori.setText(volume.getTitle());
                    try {
                        switch (volume.getAuthors().size()) {
                            case 1:
                                tvAutori.setText("by " + volume.getAuthors().get(0));
                                break;
                            case 2:
                                tvAutori.setText("by " + volume.getAuthors().get(0) + ", " + volume.getAuthors().get(1));
                                break;
                            case 3:
                                tvAutori.setText("by " + volume.getAuthors().get(0) + ", " + volume.getAuthors().get(1) + ", " + volume.getAuthors().get(2));
                                break;
                            case 4:
                                tvAutori.setText("by " + volume.getAuthors().get(0) + ", " +
                                        volume.getAuthors().get(1) + ", " +
                                        volume.getAuthors().get(2) + ", " + volume.getAuthors().get(3));
                                break;
                            case 5:
                                tvAutori.setText("by " + volume.getAuthors().get(0) + ", " + volume.getAuthors().get(1) + ", " + volume.getAuthors().get(2) +
                                        ", " + volume.getAuthors().get(3) + ", " + volume.getAuthors().get(4));
                                break;
                            default:
                                tvAutori.setText("by " + volume.getAuthors().get(0));
                        }
                    } catch (Exception e) {
                        tvAutori.setText(R.string.dash);
                    }

                    try {
                        Glide.with(getContext()).load(volume.getImageLinks().getThumbnail()).placeholder(R.color.white).into(bookCover);
                    } catch (Exception e) {
                        Glide.with(getContext()).load(Constant.N0_IMAGE_PLACEHOLDER)
                                .into(bookCover);
                    }
                    progressScreen.setVisibility(View.GONE);
                    tvProgressScreen.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });
    }

}


