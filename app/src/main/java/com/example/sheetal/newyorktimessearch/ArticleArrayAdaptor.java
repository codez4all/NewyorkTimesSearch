package com.example.sheetal.newyorktimessearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sheetal on 7/28/16.
 */
public class ArticleArrayAdaptor extends ArrayAdapter<Article> {

    public  ArticleArrayAdaptor(Context context, List<Article> articles)
    {
        super(context,R.layout.support_simple_spinner_dropdown_item,articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

      Article item =this.getItem(position);

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            convertView = inflater.inflate(R.layout.item_article_result,parent,false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        imageView.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(item.getHeadLine());

        String thumbNail = item.getThumbNail();
        if(!TextUtils.isEmpty(thumbNail))
        {
            Picasso.with(getContext()).load(thumbNail).into(imageView);
        }

        return  convertView;
    }
}
