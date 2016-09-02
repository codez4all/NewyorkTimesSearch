package com.example.sheetal.newyorktimessearch.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sheetal.newyorktimessearch.Models.Article;
import com.example.sheetal.newyorktimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sheetal on 7/28/16.
 */
public class ArticleArrayAdaptor extends ArrayAdapter<Article> {

    static class ViewHolder
    {
       @BindView(R.id.ivImage) ImageView imageView;
       @BindView(R.id.tvTitle) TextView tvTitle;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this,view);
        }
    }

    public  ArticleArrayAdaptor(Context context, List<Article> articles)
    {
        super(context, R.layout.support_simple_spinner_dropdown_item,articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

      ViewHolder holder;

      Article item =this.getItem(position);

        if(convertView != null)
        {
            holder= (ViewHolder) convertView.getTag();
        }
        else
        {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            convertView = inflater.inflate(R.layout.item_article_result,parent,false);

            holder =new ViewHolder(convertView);
            convertView.setTag(holder);
        }



        holder.imageView.setImageResource(0);
        holder.tvTitle.setText(item.getHeadLine());

        String thumbNail = item.getThumbNail();
        if(!TextUtils.isEmpty(thumbNail))
        {
            Picasso.with(getContext()).load(thumbNail).into(holder.imageView);
        }

        return  convertView;
    }
}
