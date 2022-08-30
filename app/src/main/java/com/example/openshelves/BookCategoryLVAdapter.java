package com.example.openshelves;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookCategoryLVAdapter extends ArrayAdapter<BookCategoryLV> {
    private Context context;
    private int mResource;
    public BookCategoryLVAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BookCategoryLV> objects) {
        super(context, resource,objects);
        this.context=context;
        this.mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        convertView=layoutInflater.inflate(mResource,parent,false);

        ImageView imageView= convertView.findViewById(R.id.ivBookcover);
        TextView tvTitle=convertView.findViewById(R.id.tvTitle);
        TextView tvAuthor=convertView.findViewById(R.id.tvAuthor);
        TextView tvYear=convertView.findViewById(R.id.tvYear);
        TextView tvQuote=convertView.findViewById(R.id.tvQuote);

        imageView.setImageResource(getItem(position).getImage());
        tvTitle.setText(getItem(position).getTitle());
        tvAuthor.setText(getItem(position).getAuthor());
        tvYear.setText(getItem(position).getYear().toString());
        tvQuote.setText(getItem(position).getQuote());

        return convertView;
    }
}
