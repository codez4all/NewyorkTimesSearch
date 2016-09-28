package com.example.sheetal.newyorktimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.sheetal.newyorktimessearch.models.Article;
import com.example.sheetal.newyorktimessearch.R;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity  {
    @BindView(R.id.webView) WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));

        //title
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
        {
            actionBar.setTitle(article.getHeadLine());
        }

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return  true;
            }
        });
        webView.loadUrl(article.getWebUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article,menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        //pass in the URL currently being used by WebView
        shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        miShare.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
