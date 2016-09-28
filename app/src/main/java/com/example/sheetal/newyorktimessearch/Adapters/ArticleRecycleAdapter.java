package com.example.sheetal.newyorktimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sheetal.newyorktimessearch.models.Article;
import com.example.sheetal.newyorktimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sheetal.
 */

public class ArticleRecycleAdapter extends RecyclerView.Adapter<ArticleRecycleAdapter.ViewHolder> {

    private List<Article> mArticles;
    private Context mContext;

    public  static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.ivImage) ImageView imageView;
        @BindView(R.id.tvTitle) TextView tvTitle;

        public ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    // Pass in the contact array into the constructor
    public ArticleRecycleAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }


    @Override
    public ArticleRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article_result,parent,false);

        ViewHolder viewHolder = new ViewHolder(articleView);
        return  viewHolder;
    }


    @Override
    public void onBindViewHolder(ArticleRecycleAdapter.ViewHolder holder, int position) {

        Article article = mArticles.get(position);

        ImageView imgView = holder.imageView;
        TextView tvTitle = holder.tvTitle;

        imgView.setImageResource(0);
        tvTitle.setText(article.getHeadLine());

        String thumbNail = article.getThumbNail();
        if(!TextUtils.isEmpty(thumbNail))
        {
            Picasso.with(getContext()).load(thumbNail).into(imgView);
        }

    }


    @Override
    public int getItemCount() {
        return mArticles.size();
    }

}


