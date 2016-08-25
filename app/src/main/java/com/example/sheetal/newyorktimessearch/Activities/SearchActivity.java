package com.example.sheetal.newyorktimessearch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.sheetal.newyorktimessearch.Article;
import com.example.sheetal.newyorktimessearch.ArticleArrayAdaptor;
import com.example.sheetal.newyorktimessearch.EndlessScrollListener;
import com.example.sheetal.newyorktimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdaptor adaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

       /* Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView)findViewById(R.id.gvResults);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        articles = new ArrayList<>();
        adaptor = new ArticleArrayAdaptor(this,articles);
        gvResults.setAdapter(adaptor);

        //item click listener on gridview
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(SearchActivity.this,ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", article);
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
       getMenuInflater().inflate(R.menu.menu_search,menu);
        return  true;
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

        client.get(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONArray articleResults = null;

                try {
                    articleResults = response.getJSONObject("response").getJSONArray("docs");
                    adaptor.addAll(Article.fromJSONArray(articleResults));
                    Log.d("Debug",articles.toString());

                }catch (JSONException e)
                {
                    e.printStackTrace();;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("Debug","Api request failed");
            }
        });

    }

    public void onArticleSearch(View view) {
        final String query= etQuery.getText().toString();
        //Toast.makeText(SearchActivity.this, "Searching for "+query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params= new RequestParams();
        params.put("api-key","dd76f7cbcac0488486de34b0c7ca5155");
        params.put("page",0);
        params.put("q", query);

        client.get(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONArray articleResults = null;

                try {
                    articleResults = response.getJSONObject("response").getJSONArray("docs");
                    adaptor.addAll(Article.fromJSONArray(articleResults));
                    Log.d("Debug",articles.toString());

                }catch (JSONException e)
                {
                    e.printStackTrace();;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("Debug","Api request failed");
            }
        });


    }
}
