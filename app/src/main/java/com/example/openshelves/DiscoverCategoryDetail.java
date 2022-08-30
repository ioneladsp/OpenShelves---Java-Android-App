package com.example.openshelves;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class DiscoverCategoryDetail extends Fragment {

    TextView tvTitluCategorie, tvDescriereCategorie;
    ImageButton btnBack, btnSort;
    String searchFragment="Discover";
    ListView listView;

    public DiscoverCategoryDetail() {
        // Required empty public constructor
    }

    public static DiscoverCategoryDetail newInstance(int param1) {
        DiscoverCategoryDetail fragment = new DiscoverCategoryDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchFragment = getArguments().getString("searchFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_discover_category_detail, container, false);
        tvTitluCategorie=view.findViewById(R.id.tvTitluCategorie);
        tvDescriereCategorie=view.findViewById(R.id.tvDescriereCategorie);


        btnBack=view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchFragment!=null && searchFragment.equalsIgnoreCase("Search")){
                    SearchFragment fragment=new SearchFragment();
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerdiscover,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else{
                    DiscoverFragment fragment=new DiscoverFragment();
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerdiscover,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        listView=view.findViewById(R.id.lvCategory);
        //ArrayList cu datele cu care vreau sa populez
        ArrayList<BookCategoryLV> arrayList=new ArrayList<>();
        ArrayList<BookCategoryLV> arrayList1=new ArrayList<>();
        ArrayList<BookCategoryLV> arrayList2=new ArrayList<>();
        ArrayList<BookCategoryLV> arrayList3=new ArrayList<>();

        int pos=getArguments().getInt("titlu");
        if(pos==0){
            tvTitluCategorie.setText("Classics");
            tvDescriereCategorie.setText(getString(R.string.classicsCategory));
            arrayList.add(new BookCategoryLV(R.drawable.classics1,"The Great Gatsby","by F. Scott Fitzgerald", 1925,"Reserving judgements is a matter of infinite hope."));
            arrayList.add(new BookCategoryLV(R.drawable.classics2,"Pride and Prejudice","by Jane Austen",1813,"To be fond of dancing was a certain step towards falling in love."));
            arrayList.add(new BookCategoryLV(R.drawable.classics3,"To Kill a Mockingbird","Harper Lee",1960,"The one thing that doesn't abide by majority rule is a person's conscience."));
            arrayList.add(new BookCategoryLV(R.drawable.classics4,"1984","George Orwell",1949,"Who controls the past controls the future. Who controls the present controls the past."));
            arrayList.add(new BookCategoryLV(R.drawable.classics53,"Jane Eyre","Charlotte Bronte",1847,"Life appears to me too short to be spent in nursing animosity or registering wrongs."));
            BookCategoryLVAdapter bookCategoryLVAdapter=new BookCategoryLVAdapter(getContext(), R.layout.lv_row,arrayList);
            listView.setAdapter(bookCategoryLVAdapter);

        }
        if(pos==1){
            tvTitluCategorie.setText("Romance");
            tvDescriereCategorie.setText(getString(R.string.romanceCategory));
            arrayList1.add(new BookCategoryLV(R.drawable.romance1,"Everything, everything","Nicola Yoon",2015,"It's a hard concept to hold on to the idea that there was a time before us. A time before time."));
            arrayList1.add(new BookCategoryLV(R.drawable.romance5,"The Song of Achilles","Madeline Miller",2011,"And perhaps it is the greater grief, after all, to be left on earth when another is gone."));
            arrayList1.add(new BookCategoryLV(R.drawable.romance2,"Fangirl","Rainbow Rowell",2013,"In new situations, all the trickiest rules are the ones nobody bothers to explain to you, and the ones you can't Google."));
            arrayList1.add(new BookCategoryLV(R.drawable.romance3,"The Flatshire","Beth O'Leary",2019,"Being nice is a good thing. You can be strong and nice. You don't have to be one or the other."));
            arrayList1.add(new BookCategoryLV(R.drawable.romance4,"The Spanish Love Deception","Elena Armas",2021,"Because it was all you were willing to give me. And I'd rather have you hating me than not have you at all."));

            BookCategoryLVAdapter bookCategoryLVAdapter=new BookCategoryLVAdapter(getContext(), R.layout.lv_row,arrayList1);
            listView.setAdapter(bookCategoryLVAdapter);
        }
        if(pos==2){
            arrayList.clear();
            tvTitluCategorie.setText("Nonfiction");
            tvDescriereCategorie.setText(getString(R.string.nonfictionCategory));
            arrayList2.add(new BookCategoryLV(R.drawable.nonfiction5,"The Glass Castle","Jeanette Walls",2005,"Life is a drama full of tragedy and comedy. You should learn to enjoy the comic episodes a little more."));
            arrayList2.add(new BookCategoryLV(R.drawable.nonfiction1,"Sapiens: A Brief History of Humankind","Yuval Noah Harari",2011,"Money is the most universal and most efficient system of mutual trust ever devised."));
            arrayList2.add(new BookCategoryLV(R.drawable.nonfiction2,"Educated","Tara Westover",2018,"An education is not so much about making a living as making a person."));
            arrayList2.add(new BookCategoryLV(R.drawable.nonfiction3,"When Breath Becomes Air","Paul Kalanithi",2016,"Human knowledge is never contained in one person. It grows from the relationships we create and still it is never complete."));
            arrayList2.add(new BookCategoryLV(R.drawable.nonfiction4,"A Brief History of Time","Stephen Hawking",1988,"If time travel is possible, where are the tourists from the future?"));

            BookCategoryLVAdapter bookCategoryLVAdapter=new BookCategoryLVAdapter(getContext(), R.layout.lv_row,arrayList2);
            listView.setAdapter(bookCategoryLVAdapter);
        }
        if(pos==3){
            arrayList.clear();
            tvTitluCategorie.setText("Fantasy");
            tvDescriereCategorie.setText(getString(R.string.fantasyCategory));
            arrayList3.add(new BookCategoryLV(R.drawable.fantasy1,"Chamber of Secrets","J.K. Rowling",2002,"Hearing voices no one else can hear isn't a good sign, even in the wizarding world."));
            arrayList3.add(new BookCategoryLV(R.drawable.fantasy2,"The Hobbit","J.R.R Tolkien",1937,"May the wind under your wings bear you where the sun sails and the moon walks."));
            arrayList3.add(new BookCategoryLV(R.drawable.fantasy3,"Stardust","Neil Gaiman",1997,"Anyone who believes what a cat tells him deserves all he gets."));
            arrayList3.add(new BookCategoryLV(R.drawable.fantasy4,"A Wrinkle in Time","Madeleine L'Engle",1962,"The only way to cope with something deadly serious is to try to treat it a little lightly."));
            arrayList3.add(new BookCategoryLV(R.drawable.fantasy5,"Circe","Madeline Miller",2018,"I would say, some people are like constellations that only touch the earth for a season."));
            BookCategoryLVAdapter bookCategoryLVAdapter=new BookCategoryLVAdapter(getContext(), R.layout.lv_row,arrayList3);
            listView.setAdapter(bookCategoryLVAdapter);
        }

        btnSort=view.findViewById(R.id.btnSort);
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=tvTitluCategorie.getText().toString();
                if(category.equalsIgnoreCase("classics")){
                    sortList(arrayList);
                    BookCategoryLVAdapter bookCategoryLVAdapter=new BookCategoryLVAdapter(getContext(), R.layout.lv_row,arrayList);
                    listView.setAdapter(bookCategoryLVAdapter);
                }
                if(category.equalsIgnoreCase("romance")){
                    sortList(arrayList1);
                    BookCategoryLVAdapter bookCategoryLVAdapter=new BookCategoryLVAdapter(getContext(), R.layout.lv_row,arrayList1);
                    listView.setAdapter(bookCategoryLVAdapter);
                }
                if(category.equalsIgnoreCase("nonfiction")){
                    sortList(arrayList2);
                    BookCategoryLVAdapter bookCategoryLVAdapter=new BookCategoryLVAdapter(getContext(), R.layout.lv_row,arrayList2);
                    listView.setAdapter(bookCategoryLVAdapter);
                }
                if(category.equalsIgnoreCase("fantasy")){
                    sortList(arrayList3);
                    BookCategoryLVAdapter bookCategoryLVAdapter=new BookCategoryLVAdapter(getContext(), R.layout.lv_row,arrayList3);
                    listView.setAdapter(bookCategoryLVAdapter);
                }
            }
        });


        return view;
    }

    private void sortList(ArrayList<BookCategoryLV> list) {
        Collections.sort(list, new Comparator<BookCategoryLV>() {
            public int compare(BookCategoryLV ideaVal1, BookCategoryLV ideaVal2) {
                Long idea1 = new Long(ideaVal1.getYear());
                Long idea2 = new Long(ideaVal2.getYear());
                return idea1.compareTo(idea2);
            }
        });
    }

}