package com.example.sheetal.newyorktimessearch.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import java.util.ArrayList;


@Parcel
public class Article
{

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    String webUrl;
    String headLine;
    String thumbNail;

    public Article()
    {

    }

    public  Article(JSONObject object) throws JSONException {
        this.webUrl= object.getString("web_url");
        this.headLine= object.getJSONObject("headline").getString("main");

        JSONArray multimedia = object.getJSONArray("multimedia");

        if(multimedia.length() >0) {
          JSONObject  multimediaJson = multimedia.getJSONObject(0);

           this.thumbNail = "http://www.nytimes.com/"+ multimediaJson.getString("url");
        }
        else
        {
            this.thumbNail = "";
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array)
    {
        ArrayList<Article> result = new ArrayList<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                result.add(new Article(array.getJSONObject(i)));
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return  result;
    }
}
