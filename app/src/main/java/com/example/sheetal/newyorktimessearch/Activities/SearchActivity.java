package com.example.sheetal.newyorktimessearch.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.sheetal.newyorktimessearch.Adapters.ArticleArrayAdaptor;
import com.example.sheetal.newyorktimessearch.EndlessScrollListener;
import com.example.sheetal.newyorktimessearch.Models.Article;
import com.example.sheetal.newyorktimessearch.Models.SettingData;
import com.example.sheetal.newyorktimessearch.R;
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

    @BindView(R.id.etQuery) EditText etQuery;
    @BindView(R.id.gvResults) GridView gvResults;
    @BindView(R.id.btnSearch) Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdaptor adaptor;
    private final int REQUEST_CODE = 200;
    SettingData filterData;
    String[] array = new String[]{"newest","oldest"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

       /* Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        articles = new ArrayList<>();
        adaptor = new ArticleArrayAdaptor(this,articles);
        gvResults.setAdapter(adaptor);

        //item click listener on gridview
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(SearchActivity.this,ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", Parcels.wrap(article));
                startActivity(i);
            }
        });


        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_search, menu);
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

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter


        final String query= etQuery.getText().toString();
        //Toast.makeText(SearchActivity.this, "Searching for "+query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params= new RequestParams();
        params.put("api-key","dd76f7cbcac0488486de34b0c7ca5155");
        params.put("page",offset);
        params.put("q", query);

        //apply more filters params
        if(filterData != null)
        {
            params.put("begin_date",filterData.getBeginDate().replace("/",""));
            params.put("sort",array[filterData.getSortOrderPosition()]);

            String new_desk_val =   filterData.isCheckedArts()
                    + filterData.isCheckedFashionStyle()
                    + filterData.isCheckedSports();

            params.put("news_desk",new_desk_val);
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONArray articleResults = null;

                try {
                    articleResults = response.getJSONObject("response").getJSONArray("docs");
                    adaptor.addAll(Article.fromJSONArray(articleResults));
                    Log.d("Debug", articles.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    ;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("Debug", "Api request failed");
            }
        });

    }

    public void onArticleSearch(View view) {

        boolean isInternetAvailable  =isNetworkAvailable();
        boolean isOnline = isOnline();

        if(isInternetAvailable && isOnline ) {
            final String query = etQuery.getText().toString();
            //Toast.makeText(SearchActivity.this, "Searching for "+query, Toast.LENGTH_SHORT).show();
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

            RequestParams params = new RequestParams();
            params.put("api-key", "dd76f7cbcac0488486de34b0c7ca5155");
            params.put("page", 0);
            params.put("q", query);

            //apply more filters params
            if(filterData != null)
            {
                params.put("begin_date",filterData.getBeginDate().replace("/",""));
                params.put("sort",array[filterData.getSortOrderPosition()]);

                String new_desk_val =   filterData.isCheckedArts()
                                        + filterData.isCheckedFashionStyle()
                                        + filterData.isCheckedSports();

                params.put("news_desk",new_desk_val);
            }

            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    JSONArray articleResults = null;

                    try {
                        articleResults = response.getJSONObject("response").getJSONArray("docs");
                        adaptor.addAll(Article.fromJSONArray(articleResults));
                        Log.d("Debug", articles.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ;
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
