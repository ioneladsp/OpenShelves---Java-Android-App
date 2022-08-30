package com.example.openshelves.Fragments.myBooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.openshelves.BookFB;
import com.example.openshelves.BooksByStatusRV;
import com.example.openshelves.BooksCurrentlyReadingRV;
import com.example.openshelves.accountDetails.LoginActivity;
import com.example.openshelves.MyBooksFragment;
import com.example.openshelves.R;
import com.example.openshelves.RecyclerViewInterfaceSearch;
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

public class BooksByStatusFragment extends Fragment implements RecyclerViewInterfaceSearch {
    private ImageButton btnBack, btnSort;
    private TextView tvSortedBy, tvStatus;
    private int counter = 0;
    private RecyclerView booksByStatusRV;

    private int a = 0;

    private BooksByStatusRV booksByStatusAdapter;
    private BooksCurrentlyReadingRV booksCRByStatusAdapter;

    LinearLayoutManager layoutManager;

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    private ArrayList<BookFB> booksList;
    private ArrayList<BookFB> booksCurrentlyReading;
    private ArrayList<BookFB> booksAlreadyRead;
    private ArrayList<BookFB> booksWantToRead;

    int pos;


    public BooksByStatusFragment() {
        // Required empty public constructor
    }


    public static BooksByStatusFragment newInstance(String param1, String param2) {
        BooksByStatusFragment fragment = new BooksByStatusFragment();
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
        View view = inflater.inflate(R.layout.fragment_books_by_status, container, false);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvSortedBy = view.findViewById(R.id.tvSortedBy);
        btnBack = view.findViewById(R.id.btnBack);
        btnSort = view.findViewById(R.id.btnSort);
        pos = getArguments().getInt("statusKey");
        if (pos == 1) {
            tvStatus.setText("Currently Reading");
            tvSortedBy.setText("PROGRESS");
        }
        if (pos == 2) {
            tvStatus.setText("Already Read");
        }

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if (counter % 2 == 1 && pos == 1) {
                    Collections.sort(booksCurrentlyReading, ((o1, o2) -> (o1.getCurrentNoPages() * 100 / o1.getTotalNoPages()) - (o2.getCurrentNoPages() * 100 / o2.getTotalNoPages())));
                    setUpBooksCRResults(booksCurrentlyReading);

                } else if (counter % 2 == 0 && pos == 1) {
                    Collections.sort(booksCurrentlyReading, ((o1, o2) -> (o2.getCurrentNoPages() * 100 / o2.getTotalNoPages()) - (o1.getCurrentNoPages() * 100 / o1.getTotalNoPages())));
                    setUpBooksCRResults(booksCurrentlyReading);
                }
                if (counter % 2 == 1 && pos == 2) {
                    Collections.sort(booksAlreadyRead, ((o1, o2) -> o2.getTotalNoPages() - o1.getTotalNoPages()));
                    setUpBooksByStatusResults(booksAlreadyRead);
                } else if (counter % 2 == 0 && pos == 2) {
                    Collections.sort(booksAlreadyRead, ((o1, o2) -> o1.getTotalNoPages() - o2.getTotalNoPages()));
                    setUpBooksByStatusResults(booksAlreadyRead);
                }
                if (counter % 2 == 1 && pos == 3) {
                    Collections.sort(booksWantToRead, ((o1, o2) -> o2.getTotalNoPages() - o1.getTotalNoPages()));
                    setUpBooksByStatusResults(booksWantToRead);
                } else if (counter % 2 == 0 && pos == 3) {
                    Collections.sort(booksWantToRead, ((o1, o2) -> o1.getTotalNoPages() - o2.getTotalNoPages()));
                    setUpBooksByStatusResults(booksWantToRead);
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBooksFragment fragment = new MyBooksFragment();
                //incepe tranzitia
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.booksByStatusContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        booksByStatusRV = view.findViewById(R.id.booksByStatusRV);
        booksByStatusRV.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        firebaseAuth = FirebaseAuth.getInstance();

        booksList = new ArrayList<>();
        booksCurrentlyReading = new ArrayList<>();
        booksAlreadyRead = new ArrayList<>();
        booksWantToRead = new ArrayList<>();

        checkUser();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String UIDCurrentUser = firebaseUser.getUid();

        //referinta la obiectul utilizator exact cu id-ul logat
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

                booksCurrentlyReading = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getStatus().equalsIgnoreCase("currently reading")).collect(Collectors.toList());
                booksAlreadyRead = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getStatus().equalsIgnoreCase("already read")).collect(Collectors.toList());
                booksWantToRead = (ArrayList<BookFB>) booksList.stream().filter(b -> b.getStatus().equalsIgnoreCase("want to read")).collect(Collectors.toList());

                //preluare date pentru a stii cu ce carti sa populez rv (din ce categorie-status)
                int pos = getArguments().getInt("statusKey");
                if (pos == 1) {

                    Collections.sort(booksCurrentlyReading, ((o1, o2) -> (o2.getCurrentNoPages() * 100 / o2.getTotalNoPages()) - (o1.getCurrentNoPages() * 100 / o1.getTotalNoPages())));
                    setUpBooksCRResults(booksCurrentlyReading);
                }
                if (pos == 2) {
                    //populare recyclerview
                    Collections.sort(booksAlreadyRead, Comparator.comparingInt(BookFB::getTotalNoPages));
                    setUpBooksByStatusResults(booksAlreadyRead);
                }
                if (pos == 3) {
                    //populare recyclerview
                    Collections.sort(booksWantToRead, Comparator.comparingInt(BookFB::getTotalNoPages));
                    setUpBooksByStatusResults(booksWantToRead);
                }

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            // Get RecyclerView item from the ViewHolder
                            View itemView = viewHolder.itemView;
                            Paint p = new Paint();
                            if (dX > 0) {
                                /* Set color for positive displacement */
                                p.setARGB(255, 255, 0, 0);
                                // Draw Rect with varying right side, equal to displacement dX
                                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                        (float) itemView.getBottom(), p);
                            } else {
                                /* Set color for negative displacement */
                                p.setARGB(255, 205, 91, 69);
                                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                        (float) itemView.getRight(), (float) itemView.getBottom(), p);
                            }

                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        }
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Warning");
                        builder.setMessage("Removing this book from this shelf will also remove any data you have associated with it.");
                        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (pos == 3) {
                                    BookFB bookdeleted = booksByStatusAdapter.getItem(position);
                                    booksByStatusAdapter.deleteItem(position);
                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().
                                            getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(bookdeleted.getId());
                                    reference1.removeValue();
                                }

                                if (pos == 2) {
                                    BookFB bookdeleted = booksByStatusAdapter.getItem(position);
                                    booksByStatusAdapter.deleteItem(position);
                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(bookdeleted.getId());
                                    reference1.removeValue();
                                }

                                if (pos == 1) {
                                    BookFB bookdeleted = booksCRByStatusAdapter.getItem(position);
                                    booksCRByStatusAdapter.deleteItem(position);
                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users" + "/" + UIDCurrentUser + "/" + "books").child(bookdeleted.getId());
                                    reference1.removeValue();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (pos == 1) {
                                    setUpBooksCRResults(booksCurrentlyReading);
                                }

                                if (pos == 2) {
                                    setUpBooksByStatusResults(booksAlreadyRead);
                                }
                                if (pos == 3) {
                                    setUpBooksByStatusResults(booksWantToRead);
                                }
                            }
                        });
                        builder.show();

                    }
                }).attachToRecyclerView(booksByStatusRV);

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

    private void setUpBooksByStatusResults(List<BookFB> itemList) {
        booksByStatusAdapter = new BooksByStatusRV(getContext(), itemList, this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        booksByStatusRV.setLayoutManager(layoutManager);
        booksByStatusRV.setAdapter(booksByStatusAdapter);
    }

    private void setUpBooksCRResults(List<BookFB> itemList) {
        booksCRByStatusAdapter = new BooksCurrentlyReadingRV(getContext(), itemList, this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        booksByStatusRV.setLayoutManager(layoutManager);
        booksByStatusRV.setAdapter(booksCRByStatusAdapter);
    }


    @Override
    public void onItemClick(String pos) {
        //pentru want to read
        if (this.pos == 3) {
            BookDetailsWR fragment = new BookDetailsWR();
            final Bundle bundle = new Bundle();
            bundle.putString("volume_id", pos);
            fragment.setArguments(bundle);
            //incepe tranzitia
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.booksByStatusContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //pentru already read
        if (this.pos == 2) {
            BookDetailsAR fragment = new BookDetailsAR();
            final Bundle bundle = new Bundle();
            bundle.putString("volume_id", pos);
            fragment.setArguments(bundle);
            //incepe tranzitia
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.booksByStatusContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //pentru currently reading
        if (this.pos == 1) {
            BookDetailsCR fragment = new BookDetailsCR();
            final Bundle bundle = new Bundle();
            bundle.putString("volume_id", pos);
            fragment.setArguments(bundle);
            //incepe tranzitia
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.booksByStatusContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}