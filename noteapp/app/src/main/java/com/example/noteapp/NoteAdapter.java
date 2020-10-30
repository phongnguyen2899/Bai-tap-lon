package com.example.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Note> list;
    public NoteAdapter(Context context,ArrayList<Note> list){
        inflater=LayoutInflater.from(context);
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView =inflater.inflate(R.layout.item_listview,null);
        TextView txttitle=convertView.findViewById(R.id.tv_title);
        TextView txtcontent=convertView.findViewById(R.id.tv_content);
        TextView txtcreatedate=convertView.findViewById(R.id.tv_createdate);

        txttitle.setText(list.get(position).getTitle());
        txtcontent.setText(list.get(position).getContent());
        txtcreatedate.setText(list.get(position).getCreatedate().toString());
        return convertView;
    }
}
