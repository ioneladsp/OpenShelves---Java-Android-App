package com.example.openshelves.recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openshelves.R;
import com.example.openshelves.RecyclerViewInterfaceSearch;
import com.example.openshelves.api.Item;
import com.example.openshelves.helper.Constant;
import com.example.openshelves.vendor.NumberFormatter;

import java.util.List;

public class SearchResultsRecyclerviewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerviewAdapter.ViewHolder> {
    private Context context;
    private List<Item> items;
    private String volumeId="";

    private final RecyclerViewInterfaceSearch recyclerViewInterface;

    public SearchResultsRecyclerviewAdapter(Context context, List<Item> items, RecyclerViewInterfaceSearch bookInterface) {
        this.context = context;
        this.items = items;
        this.recyclerViewInterface=bookInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_book_card_horizontal, parent, false);
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
        Item item = items.get(position);
        holder.titleTV.setText(item.getVolumeInfo().getTitle());
        holder.RatingRB.setVisibility(View.VISIBLE);

        try{
            holder.publisherTV.setText(item.getVolumeInfo().getPublisher());
        }catch (Exception e) {
            holder.publisherTV.setText(R.string.dash);
        }

        try {
            Glide.with(context).load(item.getVolumeInfo().getImageLinks().getSmallThumbnail()).centerCrop().into(holder.imageView);
        }catch (Exception e) {
            Glide.with(context).load(Constant.N0_IMAGE_PLACEHOLDER)
                    .centerCrop().into(holder.imageView);
        }
        try {
            if (item.getVolumeInfo().getRatingsCount()==1) {
                holder.ratingsTV.setText(item.getVolumeInfo().getAverageRating()+" avg rating ??? "+ NumberFormatter.format(item.getVolumeInfo().getRatingsCount())+" rating");
            }else {
                holder.ratingsTV.setText(item.getVolumeInfo().getAverageRating()+" avg rating ??? "+NumberFormatter.format(item.getVolumeInfo().getRatingsCount())+" ratings");
            }
        }catch (Exception e) {
            holder.ratingsTV.setText(R.string.no_reviews);
        }

        try {
            switch (item.getVolumeInfo().getAuthors().size()) {
                case 1:
                    holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0));
                    break;
                case 2:
                    holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0)+", "+item.getVolumeInfo().getAuthors().get(1));
                    break;
                case 3:
                    holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0)+", "+item.getVolumeInfo().getAuthors().get(1)+", "+item.getVolumeInfo().getAuthors().get(2));
                    break;
                case 4:
                    holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0)+", "+
                            item.getVolumeInfo().getAuthors().get(1)+", "+
                            item.getVolumeInfo().getAuthors().get(2)+", "+item.getVolumeInfo().getAuthors().get(3));
                    break;
                case 5:
                    holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0)+", "+item.getVolumeInfo().getAuthors().get(1)+", "+item.getVolumeInfo().getAuthors().get(2)+
                            ", "+item.getVolumeInfo().getAuthors().get(3)+", "+item.getVolumeInfo().getAuthors().get(4));
                    break;
                default:
                    holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0));
            }
        }catch (Exception e) {
            holder.authorTV.setText(R.string.dash);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView publisherTV, titleTV, authorTV, ratingsTV, noRatingTV;
        RatingBar RatingRB;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterfaceSearch bookInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            publisherTV = itemView.findViewById(R.id.publisherTV);
            titleTV = itemView.findViewById(R.id.titleTV);
            authorTV = itemView.findViewById(R.id.authorTV);
            ratingsTV = itemView.findViewById(R.id.ratingsTV);
            RatingRB = itemView.findViewById(R.id.RatingRB);
            noRatingTV = itemView.findViewById(R.id.noRatingTV);
        }

    }

}
