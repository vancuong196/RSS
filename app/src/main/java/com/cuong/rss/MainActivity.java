package com.cuong.rss;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.cuong.rss.FeedParsing.parseFeed;

public class MainActivity extends AppCompatActivity
  {
      private RSS mCurentRss;
      private RecyclerView mFeedLv;
      private SwipeRefreshLayout mSwipeLayout;

      private ArrayList<Feed> mFeedList;
    ListView mLvRss;
    RssListViewApdapter rssAdapter;
    ArrayList<RSS> datas;
    private static DataBaseAcess sDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sDatabase = new DataBaseAcess(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        init();
        initMainContent();
    }
    void newRssFeed(RSS rss){
        this.mCurentRss =rss;
        new FetchFeedTask().execute((Void) null);

    }
    void initMainContent(){

        mFeedLv = findViewById(R.id.lv_feeds);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        if (sDatabase.getCount()==0) {
            return;
        }else {
            mCurentRss =sDatabase.getRss(1);

                new FetchFeedTask().execute((Void) null);
            }

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });
    }
    void init(){
        mLvRss = findViewById(R.id.lv_myrss);

        datas = sDatabase.getAllRss();
        rssAdapter = new RssListViewApdapter(this,datas);
        mLvRss.setAdapter(rssAdapter);
        mLvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newRssFeed(rssAdapter.getItem(position));
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addCallBack(View v){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Intent myIntent = new Intent(MainActivity.this,AddActivity.class);
        myIntent.setAction("add");
        startActivityForResult(myIntent,1);

    }
    public void editCallBack(View v){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Intent myIntent = new Intent(MainActivity.this,RssListActivity.class);
        startActivityForResult(myIntent,2);

    }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          rssAdapter.clear();
          rssAdapter.addAll(sDatabase.getAllRss());
          sDatabase.close();
          rssAdapter.notifyDataSetChanged();
          super.onActivityResult(requestCode, resultCode, data);
      }


      private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

          private String urlLink;

          @Override
          protected void onPreExecute() {
              mSwipeLayout.setRefreshing(true);
              urlLink = mCurentRss.getmLink();
          }

          @Override
          protected Boolean doInBackground(Void... voids) {
              if (TextUtils.isEmpty(urlLink))
                  return false;

              try {
                  if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                      urlLink = "http://" + urlLink;

                  URL url = new URL(urlLink);
                  InputStream inputStream = url.openConnection().getInputStream();

                  mFeedList = parseFeed(inputStream);
                  return true;
              } catch (IOException e) {
                  Log.e("ioe", "Error", e);
              } catch (XmlPullParserException e) {
                  Log.e("null", "Error", e);
              }
              return false;
          }

          @Override
          protected void onPostExecute(Boolean success) {
              mSwipeLayout.setRefreshing(false);

              if (success) {

                  // Fill RecyclerView
                  LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                  mFeedLv.setLayoutManager(mLayoutManager);
                  mFeedLv.setAdapter(new FeedListAdapter(mFeedList));
              } else {
                  Toast.makeText(MainActivity.this,
                          "Invalid Rss feed url",
                          Toast.LENGTH_LONG).show();
              }
          }
      }
  }
