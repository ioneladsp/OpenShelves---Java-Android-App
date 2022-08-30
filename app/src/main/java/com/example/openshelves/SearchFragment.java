package com.example.openshelves;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.openshelves.api.Books;
import com.example.openshelves.api.Item;
import com.example.openshelves.recycler.SearchResultsRecyclerviewAdapter;
import com.example.openshelves.request.api.RequestService;
import com.example.openshelves.request.api.RetrofitClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements RecyclerViewInterfaceSearch{

    EditText searchQueryET;
    ImageView img1, img2,img3,img4;
    TextView tvDescriere;
    RecyclerView searchResultsRV;
    ProgressBar progressBar, progressBar1;
    SearchResultsRecyclerviewAdapter searchAdapter;
    LinearLayoutManager layoutManager;
    String search_keyword="";
    String orderBy="relevance";
    RequestService requestService;
    Call<Books> searchResultsCall;
    int page=1;
    List<Item> carti=new ArrayList<>();


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view= inflater.inflate(R.layout.activity_search, container, false);
        img1=view.findViewById(R.id.imgV1);
        img2=view.findViewById(R.id.imgV2);
        img3=view.findViewById(R.id.imgV3);
        img4=view.findViewById(R.id.imgV4);
        img1.setClipToOutline(true);
        img2.setClipToOutline(true);
        img3.setClipToOutline(true);
        img4.setClipToOutline(true);
        tvDescriere=view.findViewById(R.id.tvDescriere);

        searchQueryET = view.findViewById(R.id.searchQueryET);
        searchResultsRV = view.findViewById(R.id.searchResultsRV);
        searchResultsRV.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        progressBar = view.findViewById(R.id.progressBar);
        progressBar1 = view.findViewById(R.id.progressBar1);

        requestService = RetrofitClass.getNewBooksAPIInstance();


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverCategoryDetail fragment=new DiscoverCategoryDetail();
                final Bundle bundle=new Bundle();
                bundle.putInt("titlu",0);
                bundle.putString("searchFragment","Search");
                fragment.setArguments(bundle);
                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.searchBooksContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverCategoryDetail fragment=new DiscoverCategoryDetail();
                final Bundle bundle=new Bundle();
                bundle.putInt("titlu",1);
                bundle.putString("searchFragment","Search");
                fragment.setArguments(bundle);
                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.searchBooksContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverCategoryDetail fragment=new DiscoverCategoryDetail();
                final Bundle bundle=new Bundle();
                bundle.putInt("titlu",2);
                bundle.putString("searchFragment","Search");
                fragment.setArguments(bundle);
                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.searchBooksContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverCategoryDetail fragment=new DiscoverCategoryDetail();
                final Bundle bundle=new Bundle();
                bundle.putInt("titlu",3);
                bundle.putString("searchFragment","Search");
                fragment.setArguments(bundle);
                //incepe tranzitia
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.searchBooksContainer,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        searchQueryET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    search_keyword = searchQueryET.getText().toString().trim();
                    if (!searchQueryET.getText().toString().trim().isEmpty()) {
                        loadRelevantItems(page);
                        //imagini
                        img1.setVisibility(View.GONE);
                        img2.setVisibility(View.GONE);
                        img3.setVisibility(View.GONE);
                        img4.setVisibility(View.GONE);
                        tvDescriere.setVisibility(View.GONE);
                        searchResultsRV.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }else {
                        searchResultsRV.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        //imagini
                        img1.setVisibility(View.VISIBLE);
                        img2.setVisibility(View.VISIBLE);
                        img3.setVisibility(View.VISIBLE);
                        img4.setVisibility(View.VISIBLE);
                        tvDescriere.setVisibility(View.VISIBLE);

                    }
                return false;
            }
        });

        searchQueryET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search_keyword = searchQueryET.getText().toString().trim();
                if (s.length() >0 && (!searchQueryET.getText().toString().trim().isEmpty())){
                    loadRelevantItems(page);
                } else {
                    //imagini
                    img1.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.VISIBLE);
                    img4.setVisibility(View.VISIBLE);
                    tvDescriere.setVisibility(View.VISIBLE);
                    searchResultsRV.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
        return view;
    }


    private void setUpSearchResultslist(List<Item> itemList) {
        searchAdapter = new SearchResultsRecyclerviewAdapter(getContext(),itemList,this);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        searchResultsRV.setLayoutManager(layoutManager);
        searchResultsRV.setAdapter(searchAdapter);
    }

    void loadRelevantItems(int page) {
        searchResultsRV.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        callSearchResults("relevance",page);
    }

    private void callSearchResults(String orderBy, int index) {
        String finalQuery = search_keyword.replace(" ","+");
        searchResultsCall = requestService.getSearchResults(finalQuery,index,orderBy,40);

        searchResultsCall.enqueue(new Callback<Books>() {
            @Override
            public void onResponse(Call<Books> call, Response<Books> response) {
                if ((response.isSuccessful() || response.body() != null) && response.body().getItems()!=null) {
                    img1.setVisibility(View.GONE);
                    img2.setVisibility(View.GONE);
                    img3.setVisibility(View.GONE);
                    img4.setVisibility(View.GONE);
                    tvDescriere.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    searchResultsRV.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    progressBar1.setVisibility(View.GONE);

                    for (int i=0; i<response.body().getItems().size(); i++) {
                        setUpSearchResultslist(response.body().getItems());
                        carti=response.body().getItems();
                    }
                }

                if (response.code()!=200) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Books> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                tvDescriere.setVisibility(View.GONE);
            }
        });
    }




    @Override
    public void onItemClick(String idBook) {
        BookDetailsFragment fragment=new BookDetailsFragment();
        final Bundle bundle=new Bundle();
        bundle.putString("volume_id",idBook);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.searchBooksContainer,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}