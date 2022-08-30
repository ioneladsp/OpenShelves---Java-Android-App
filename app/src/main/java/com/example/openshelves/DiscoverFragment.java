package com.example.openshelves;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class DiscoverFragment extends Fragment implements RecyclerViewInterface {

    RecyclerView recyclerView;
    ArrayList<ParentModelClass> parentModelClassArrayList;
    ArrayList<ChildModelClass> classics;
    ArrayList<ChildModelClass> romance;
    ArrayList<ChildModelClass> nonfiction;
    ArrayList<ChildModelClass> fantasy;
    ParentAdapter parentAdapter;
    SearchView searchView;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
        View view= inflater.inflate(R.layout.fragment_discover, container, false);
        recyclerView=view.findViewById(R.id.rv_parent);
        recyclerView.setNestedScrollingEnabled(false);
        classics=new ArrayList<>();
        romance=new ArrayList<>();
        nonfiction=new ArrayList<>();
        fantasy=new ArrayList<>();
        parentModelClassArrayList=new ArrayList<>();

        classics.add(new ChildModelClass(R.drawable.classics1));
        classics.add(new ChildModelClass(R.drawable.classics2));
        classics.add(new ChildModelClass(R.drawable.classics3));
        classics.add(new ChildModelClass(R.drawable.classics4));
        classics.add(new ChildModelClass(R.drawable.classics53));

        parentModelClassArrayList.add(new ParentModelClass("Classics",classics));

        romance.add(new ChildModelClass(R.drawable.romance1));
        romance.add(new ChildModelClass(R.drawable.romance5));
        romance.add(new ChildModelClass(R.drawable.romance2));
        romance.add(new ChildModelClass(R.drawable.romance4));
        romance.add(new ChildModelClass(R.drawable.romance3));

        parentModelClassArrayList.add(new ParentModelClass("Romance",romance));


        nonfiction.add(new ChildModelClass(R.drawable.nonfiction5));
        nonfiction.add(new ChildModelClass(R.drawable.nonfiction1));
        nonfiction.add(new ChildModelClass(R.drawable.nonfiction2));
        nonfiction.add(new ChildModelClass(R.drawable.nonfiction3));
        nonfiction.add(new ChildModelClass(R.drawable.nonfiction4));

        parentModelClassArrayList.add(new ParentModelClass("Nonfiction", nonfiction));

        fantasy.add(new ChildModelClass(R.drawable.fantasy1));
        fantasy.add(new ChildModelClass(R.drawable.fantasy2));
        fantasy.add(new ChildModelClass(R.drawable.fantasy3));
        fantasy.add(new ChildModelClass(R.drawable.fantasy4));
        fantasy.add(new ChildModelClass(R.drawable.fantasy5));

        parentModelClassArrayList.add(new ParentModelClass("Fantasy", fantasy));

        parentAdapter=new ParentAdapter(parentModelClassArrayList,getContext(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();

        searchView=view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        return view;
    }

    private void filterList(String text) {
        ArrayList<ParentModelClass> parentModelClassArrayListFiltered=new ArrayList<>();
        for(ParentModelClass parentModelClass:parentModelClassArrayList){
            if(parentModelClass.title.equalsIgnoreCase(text)){
                parentModelClassArrayListFiltered.add(parentModelClass);
            }
        }
        if(parentModelClassArrayListFiltered.isEmpty()){
            parentAdapter.setFilteredList(parentModelClassArrayList);
        }else{
            parentAdapter.setFilteredList(parentModelClassArrayListFiltered);
        }
    }

    @Override
    public void onItemClick(int position) {
        //the code you want to get executed when the user clicks on the recyclerview
        DiscoverCategoryDetail fragment=new DiscoverCategoryDetail();
        final Bundle bundle=new Bundle();
        bundle.putInt("titlu",position);
        fragment.setArguments(bundle);
        //incepe tranzitia
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerdiscover,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}