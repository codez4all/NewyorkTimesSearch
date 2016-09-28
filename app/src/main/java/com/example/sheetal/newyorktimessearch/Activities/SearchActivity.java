package com.example.sheetal.newyorktimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sheetal.newyorktimessearch.R;
import com.example.sheetal.newyorktimessearch.adapters.ArticleRecycleAdapter;
import com.example.sheetal.newyorktimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.example.sheetal.newyorktimessearch.models.Article;
import com.example.sheetal.newyorktimessearch.models.SettingData;
import com.example.sheetal.newyorktimessearch.utils.ItemClickSupport;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

   // @BindView(R.id.gvResults) GridView gvResults;
    @BindView(R.id.rvResults) RecyclerView rvResults;
    ArrayList<Article> articles;
   // ArticleArrayAdaptor adaptor;
    ArticleRecycleAdapter recycleAdapter;
    private final int REQUEST_CODE = 200;
    SettingData filterData;
    String[] array = new String[]{"newest","oldest"};
    SearchView searchView;
    String searchText= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

       /* Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        articles = new ArrayList<>();
        //adaptor = new ArticleArrayAdaptor(this,articles);
        recycleAdapter = new ArticleRecycleAdapter(this,articles);
        rvResults.setAdapter(recycleAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(staggeredGridLayoutManager);


        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(SearchActivity.this,ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", Parcels.wrap(article));
                startActivity(i);
            }

        });


        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                onArticleSearch(searchText, page);
                Log.d("DEBUG", "Search Page:" + page);
                Log.d("DEBUG", "Search Query:" + searchText);
                //Log.d("DEBUG","Search view:"+ searchView.getQuery());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchText = query;

                //clear previous search
                articles.clear();
                recycleAdapter.notifyDataSetChanged();

                onArticleSearch(query,0);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.action_setting:
                Intent i = new Intent(SearchActivity.this,SettingActivity.class);
                //startActivity(i);
                startActivityForResult(i,REQUEST_CODE);

            default:
                   return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            filterData = SettingData.getInstance();
        }

    }


    public void onArticleSearch(String query, int offset) {

        boolean isInternetAvailable  =isNetworkAvailable();
        boolean isOnline = isOnline();

        if(isInternetAvailable && isOnline ) {
            //final String query = etQuery.getText().toString();
            //Toast.makeText(SearchActivity.this, "Searching for "+query, Toast.LENGTH_SHORT).show();
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

            RequestParams params = new RequestParams();
            params.put("api-key", "dd76f7cbcac0488486de34b0c7ca5155");
            params.put("page", offset);
            params.put("q", query);

            //apply more filters params
            if(filterData != null)
            {
                params.put("begin_date", filterData.getBeginDate().replace("/", ""));
                params.put("sort", array[filterData.getSortOrderPosition()]);

                StringBuffer news_desk = new StringBuffer();
                news_desk.append("news_desk:" + filterData.isCheckedArts());
                news_desk.append(" or news_desk:" + filterData.isCheckedFashionStyle());
                news_desk.append(" or news_desk:" + filterData.isCheckedSports());

                params.put("fq",news_desk.toString());

            }

            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    Log.d("DEBUG",this.getRequestURI().toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    JSONArray articleResults = null;

                    try {

                        int curSize = recycleAdapter.getItemCount();
                        articleResults = response.getJSONObject("response").getJSONArray("docs");
                        articles.addAll(Article.fromJSONArray(articleResults));
                        recycleAdapter.notifyItemRangeInserted(curSize,articles.size());

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    Log.d("Debug", "Api request failed");
                }
            });
        }
        else
        {
            Toast.makeText(this,"Network is not available",Toast.LENGTH_SHORT).show();

        }

    }

    //Check if network available
    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

    }

    //Check if device is actually connected to internet
    //pinging the Google DNS servers to check for the expected exit value
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
