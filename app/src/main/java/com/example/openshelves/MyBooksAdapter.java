package com.example.openshelves;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openshelves.helper.Constant;

import java.util.ArrayList;


public class MyBooksAdapter extends RecyclerView.Adapter<MyBooksAdapter.MyBooksViewHolder> {
    private ArrayList<String> bookCoverList;
    private Context context;

    public MyBooksAdapter(ArrayList<String> bookCoverList, Context context){
        this.context=context;
        if(bookCoverList!=null){
            this.bookCoverList=bookCoverList;
        }
        else{
            this.bookCoverList=new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public MyBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookcovers_item,parent,false);
        return new MyBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBooksViewHolder holder, int position) {
        if(bookCoverList.get(position).equalsIgnoreCase(Constant.N0_IMAGE_PLACEHOLDER)){
            Glide.with(context).load(bookCoverList.get(position))
                    .into(holder.mimageView);
        }
        else{
            Glide.with(context).load(bookCoverList.get(position)).placeholder(R.color.white).into(holder.mimageView);
        }
    }

    @Override
    public int getItemCount() {
        if(this.bookCoverList!=null)
        {
            return this.bookCoverList.size();
        }
       else{
           return 0;
        }

    }

    public class MyBooksViewHolder extends RecyclerView.ViewHolder{
        private ImageView mimageView;
        public MyBooksViewHolder(@NonNull View itemView) {
            super(itemView);
            mimageView=itemView.findViewById(R.id.iv_child_item);
        }
    }

}
