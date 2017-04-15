package com.terry.librarysearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class BookDetailActivity extends AppCompatActivity {

    private String TAG = "TAG";
    private ImageView bookImage;
    private TextView tvAuthor;
    private String bookCode;
    private String bookImageUrl;
    private String bookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_book_detail);

        bookImage = (ImageView) findViewById(R.id.book_image);
        tvAuthor = (TextView) findViewById(R.id.tv_booktitle);

        Intent intent = getIntent();
        bookCode = intent.getStringExtra("bookLinkUrl");
        bookImageUrl = intent.getStringExtra("bookImageUrl");
        bookTitle = intent.getStringExtra("bookTitle");

        Glide.with(this).load(bookImageUrl).into(bookImage);
        tvAuthor.setText(bookTitle);

        new GetHtmlText().execute();
    }

    private class GetHtmlText extends AsyncTask<Void, Void, String> {
        Document doc;
        Elements text;

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url = bookCode;

                doc = Jsoup.connect(url).get();
                text = doc.select("tbody td");

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
