package com.android.parteek.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Help extends AppCompatActivity {
    ArrayAdapter arrayAdapter;
    ArrayList<String> arrayList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        listView=(ListView)findViewById(R.id.list_item);
        arrayList=new ArrayList<>();
        arrayList.add("Hello,how are You");
        arrayList.add("Whats Today date");
        arrayList.add("Can you look for birds");
        arrayList.add("Whats your name");
        arrayList.add("who is your Invnetor");
        arrayList.add("Hello baby");
        arrayList.add("Turn on falsh");
        arrayList.add("Turn off flash");
        arrayList.add("Google funny images");
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            overridePendingTransition(R.anim.side_in_left, R.anim.side_out_right);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.side_in_left, R.anim.side_out_right);
    }
}
