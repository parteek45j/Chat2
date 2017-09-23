package com.android.parteek.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Suraj on 7/31/2017.
 */

public class CustomAdapter extends ArrayAdapter<ChatMessage>{
    TextView textView,textView1;
    ArrayList<ChatMessage> list=new ArrayList<>();
    Context context;
    @Override
    public void add(@Nullable ChatMessage object) {
        list.add(object);
        super.add(object);
    }

    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context=context;
    }

    public int getCount(){
        return this.list.size();
    }

    public ChatMessage getItem(int index){
        return this.list.get(index);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage chatMessage=getItem(position);
        View view=convertView;
        LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(chatMessage.left){
            view=inflater.inflate(R.layout.left,parent,false);
        }else {
            view=inflater.inflate(R.layout.right,parent,false);
        }
        textView=(TextView)view.findViewById(R.id.msg);
        textView1=(TextView)view.findViewById(R.id.msg1);
        textView.setText(chatMessage.message);
        textView1.setText(chatMessage.time);
        return view;
    }
}
