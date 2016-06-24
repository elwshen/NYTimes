package com.example.eshen.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.eshen.nytimessearch.Article;
import com.example.eshen.nytimessearch.ArticleArrayAdapter;
import com.example.eshen.nytimessearch.EndlessScrollListener;
import com.example.eshen.nytimessearch.R;
import com.example.eshen.nytimessearch.models.Doc;
import com.example.eshen.nytimessearch.models.Multimedium;
import com.example.eshen.nytimessearch.models.NytSearch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    GridView gvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    String searchQuery;
    private final int FILTER_REQUEST_CODE = 0;
    int beginDate = -1;
    String sortOrder = "";
    ArrayList<String> fields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

    }

    public void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);


        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create intent to display article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                //get the article to display
                Article article = articles.get(position);
                //pass in that article to intent
                i.putExtra("article", Parcels.wrap(article));
                i.putExtra("query", searchQuery);
                //launch activity
                startActivity(i);
            }
        });


        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                fetchMoreData(page);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    public void fetchMoreData(int offset) {
        Log.d("DEBUG", String.valueOf(beginDate));

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "7e272e359d554326902a565b5f58e5f3");
        params.put("page", offset);
        params.put("q", searchQuery);

        if (beginDate != -1) {
            params.put("begin_date", beginDate);
        }
        if (sortOrder.equals("newest") || sortOrder.equals("oldest")) {
            params.put("sort", sortOrder);
        }

        if (offset == 0) {
            adapter.clear();
        }

        if (fields.size() > 0) {
            params.put("facet_field", "section_name");
            params.put("facet_filter", "true");
            String fieldsParam = "news_desk:(";
            for (int i = 0; i < fields.size(); i++) {
                 fieldsParam += (fields.get(i) + " ");
            }
            fieldsParam += ")";
            params.put("fq", fieldsParam);
            Log.d("DEBUG", fieldsParam);
        }

        client.get(url, params, new TextHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Gson gson = new GsonBuilder().create();
        // Define Response class to correspond to the JSON response returned
            NytSearch nytsearch = gson.fromJson(responseString, NytSearch.class);
//                    NytSearch nytsearch = gson.getfromjson(response.toString(),NytSearch.class)
        ArrayList<Doc> docs = (ArrayList<Doc>) nytsearch.getResponse().getDocs();
            for (Doc d: docs) {
                String webUrl = d.getWebUrl();
                String headline = d.getHeadline().getMain();
                List<Multimedium> multimedia = d.getMultimedia();
                String thumbnail = "";
                if (multimedia.size() > 0) {
                    thumbnail = "http://www.nytimes.com/" + (multimedia.get(0)).getUrl();
                }
                adapter.add(new Article(webUrl, headline, thumbnail));
            }
        }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
            }
        });


//        client.get(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("DEBUG", response.toString());
//                JSONArray articleJsonResults = null;
//
//                try {
//
//                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
//                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
//                    Log.d("DEBUG", articles.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchQuery = query;
                fetchMoreData(0);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
        else if (id == R.id.action_filter) {
            Intent i = new Intent(SearchActivity.this, FilterActivity.class);
            i.putExtra("fields", fields);
//            i.putExtra("date", );
            i.putExtra("sortOrder", sortOrder);
            startActivityForResult(i, FILTER_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_REQUEST_CODE) {
            int date = data.getExtras().getInt("date", 0);
            sortOrder = data.getExtras().getString("sortOrder");
            fields = data.getExtras().getStringArrayList("fields");
            beginDate = date;
            fetchMoreData(0);
        }
    }

}
