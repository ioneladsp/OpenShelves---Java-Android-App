package com.example.openshelves;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openshelves.helper.Constant;

import java.util.List;

public class BooksCurrentlyReadingRV extends RecyclerView.Adapter<BooksCurrentlyReadingRV.ViewHolder> {
    private Context context;
    private List<BookFB> items;
    private String volumeId="";

    private final RecyclerViewInterfaceSearch recyclerViewInterface;

    public BooksCurrentlyReadingRV(Context context, List<BookFB> items, RecyclerViewInterfaceSearch bookInterface) {
        this.context = context;
        this.items = items;
        this.recyclerViewInterface=bookInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_book_card_mybooks_cr, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view,recyclerViewInterface);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 volumeId = items.get(viewHolder.getAdapterPosition()).getId();
                 recyclerViewInterface.onItemClick(volumeId);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookFB item = items.get(position);
        if(item.getCoverLink().equalsIgnoreCase(Constant.N0_IMAGE_PLACEHOLDER)){
            Glide.with(context).load(item.getCoverLink())
                    .into(holder.imageView);
        }
        else{
            Glide.with(context).load(item.getCoverLink()).placeholder(R.color.white).into(holder.imageView);
        }
        holder.titleTV.setText(item.getTitle());
        holder.authorTV.setText("by "+item.getAuthor());
        int nrPagesProgress= item.getCurrentNoPages()*100/item.getTotalNoPages();
        holder.pBar.setProgress(nrPagesProgress);
        holder.pbTV.setText(nrPagesProgress+"%");

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public BookFB getItem(int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTV, authorTV, pbTV;
        ProgressBar pBar;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterfaceSearch bookInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTV = itemView.findViewById(R.id.titleTV);
            authorTV = itemView.findViewById(R.id.authorTV);
            pbTV=itemView.findViewById(R.id.pbTV);
            pBar=itemView.findViewById(R.id.pBar);
        }

    }

}
