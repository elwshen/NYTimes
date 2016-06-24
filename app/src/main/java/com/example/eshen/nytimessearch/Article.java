package com.example.eshen.nytimessearch;

import org.parceler.Parcel;

/**
 * Created by eshen on 6/20/16.
 */
@Parcel
public class Article {
    String webUrl;
    String headline;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    String thumbnail;

    public Article() {

    }

    public Article(String webUrl, String headline, String thumbnail) {
        this.webUrl = webUrl;
        this.headline = headline;
        this.thumbnail = thumbnail;
    }

//    public Article (JSONObject jsonObject) {
//        try {
//            this.webUrl = jsonObject.getString("web_url");
//            this.headline = jsonObject.getJSONObject("headline").getString("main");
//
//            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
//
//            if (multimedia.length() > 0) {
//                JSONObject multimediaJson = multimedia.getJSONObject(0);
//                this.thumbnail = "http://www.nytimes.com/" + multimediaJson.getString("url");
//            } else {
//                this.thumbnail = "";
//            }
//        } catch (JSONException e) {
//
//        }
//    }
//
//    public static ArrayList<Article> fromJSONArray (JSONArray array) {
//        ArrayList<Article> results = new ArrayList<>();
//
//        for (int x = 0; x < array.length(); x++) {
//            try {
//                results.add(new Article(array.getJSONObject(x)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return results;
//    }
}
