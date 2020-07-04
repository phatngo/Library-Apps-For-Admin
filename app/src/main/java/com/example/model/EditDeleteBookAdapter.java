package com.example.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.libraryadmin.R;

import java.util.List;

public class EditDeleteBookAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    List<Book> objects;
    public EditDeleteBookAdapter(@NonNull Activity context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View row=layoutInflater.inflate(this.resource,null);

        TextView txtISBN_lvBookItem=row.findViewById(R.id.txtISBN_lvBookItem);
        TextView txtbookName_lvBookItem=row.findViewById(R.id.txtbookName_lvBookItem);
        TextView txtAuthor_lvBookItem=row.findViewById(R.id.txtAuthor_lvBookItem);
        TextView txtPublishedYear_lvBookItem=row.findViewById(R.id.txtPublishedYear_lvBookItem);
        TextView txtQuantity_lvBookItem=row.findViewById(R.id.txtQuantity_lvBookItem);
        TextView txtGenre_lvBookItem=row.findViewById(R.id.txtGenre_lvBookItem);

        Book book=this.objects.get(position);
        txtISBN_lvBookItem.setText(book.getISBN());
        txtbookName_lvBookItem.setText(book.getBookName());
        txtAuthor_lvBookItem.setText(book.getAuthor());
        txtPublishedYear_lvBookItem.setText(String.valueOf(book.getPublishedYear()));
        txtQuantity_lvBookItem.setText(String.valueOf(book.getQuantity()));
        txtGenre_lvBookItem.setText(book.getGenre());

        return row;
    }
}
