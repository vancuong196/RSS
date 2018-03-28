package com.cuong.rss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;




import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


    public class RssListActivity extends AppCompatActivity {
        private ListView mLvRss;
        private RssListViewApdapter mAdapter;
        private DataBaseAcess db;
        private ArrayList<RSS> mRssList;
        private  int posClick = -1;

        @Override
        public void onBackPressed() {
            setResult(RESULT_OK);
            finish();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_rss_list);
            db = new DataBaseAcess(this);
            mRssList = new ArrayList<>();
            getData();
            handle();
        }

        private void getData(){
            mRssList.clear();
            mRssList = db.getAllRss();
            db.close();
        }

        private void handle(){
            mLvRss =  findViewById(R.id.lv_rss);
            mAdapter = new RssListViewApdapter(this,mRssList);
            mLvRss.setAdapter(mAdapter);
            mLvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent myIntent =new Intent(RssListActivity.this,AddActivity.class);
                            myIntent.setAction("detail");
                            myIntent.putExtra("rss",mAdapter.getItem(position));
                            startActivityForResult(myIntent,2);
                }
            });

        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu_option,menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            int id = item.getItemId();
            switch (id){
                case R.id.addContact:
                    Intent intent = new Intent(RssListActivity.this, AddActivity.class);
                    intent.setAction("add");
                    startActivityForResult(intent,1);
                    break;
                case R.id.deleteAll:
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setTitle("Delete all");
                    alertBuilder.setMessage("Are you sure?");
                    alertBuilder.setCancelable(false);
                    alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteAll();
                        }
                    });
                    alertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
        private void deleteAll(){
            db.deleteAll();
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            db.close();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode== 1 && resultCode == RESULT_OK) {
                mAdapter.clear();
                mAdapter.addAll(db.getAllRss());
                mAdapter.notifyDataSetChanged();
            }
            if (requestCode == 2 && resultCode == 1){
                mAdapter.clear();
                mAdapter.addAll(db.getAllRss());
                mAdapter.notifyDataSetChanged();
            }
        }


    }
