package com.example.openshelves;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    List<ParentModelClass> parentModelClassList;
    Context context;

    public ParentAdapter(List<ParentModelClass> parentModelClassList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.parentModelClassList = parentModelClassList;
        this.context = context;
        this.recyclerViewInterface=recyclerViewInterface;
    }

    public void setFilteredList(List<ParentModelClass> filteredList){
        this.parentModelClassList=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.parent_rv_layout,null,false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     holder.tv_parent_title.setText(parentModelClassList.get(position).title);

     ChildAdapter childAdapter;
     childAdapter=new ChildAdapter(parentModelClassList.get(position).childModelClassList,context);
     holder.rv_child.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
     holder.rv_child.setAdapter(childAdapter);
     childAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return parentModelClassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rv_child;
        TextView tv_parent_title;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            rv_child=itemView.findViewById(R.id.rv_child);
            tv_parent_title=itemView.findViewById(R.id.tv_parent_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){
                        //grab the position from the adapter
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}

