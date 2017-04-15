package com.terry.librarysearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class BookDetailActivity extends AppCompatActivity {

    private String TAG = "TAG";
    String bookCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_book_detail);

        Intent intent = getIntent();
        bookCode = intent.getStringExtra("bookLinkUrl");

        new GetHtmlText().execute();
    }

    private class GetHtmlText extends AsyncTask<Void, Void, String> {
        Document doc;
        Elements text, textImgUrl, hrefs, circstats, countText, author;

        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> statusList = new ArrayList<>();
        ArrayList<String> linkList = new ArrayList<>();
        ArrayList<String> imageUrlList = new ArrayList<>();
        ArrayList<String> authorList = new ArrayList<>();

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url = "http://oasis.ssu.ac.kr/search/" + bookCode;

                doc = Jsoup.connect(url).get();
                text = doc.select("tdoby td");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            Log.d(TAG, "onPostExecute: " + text.toString());
        }
    }
}
