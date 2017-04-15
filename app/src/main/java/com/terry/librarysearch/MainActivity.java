package com.terry.librarysearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    public TextView mTextView;
    String content;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<String> linkList = new ArrayList<>();
    ArrayList<String> imageUrlList = new ArrayList<>();
    ArrayList<String> authorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        content = intent.getStringExtra("content");

        new GetHtmlText().execute();
    }

    private class GetHtmlText extends AsyncTask<Void, Void, String> {
        Document doc;
        Elements text, textImgUrl, hrefs, circstats, countText, author;
        Element e;
        int page = 1;

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url = "http://oasis.ssu.ac.kr/search/Search.Result.ax?sid=1&mf=true&q=ALL%3A" + content + "&eq=&qt=&qf=" + content + "&f=&br=&cl=1+2+60+3+4+5+6+9+10+50+51+54+33+34+55+11+12+13+14+15+16+17+18+21+22+53+52+57+58&gr=1+2+3+4+8+7+5+9+14+15&rl=&page=" + page + "&pageSize=&h=&cr=&py=&subj=&facet=&nd=&tabID=&formaction=true&item=ALL&value=" + content;
                //String url ="http://oasis.ssu.ac.kr/search/Search.Result.ax?sid=1&item=ALL&mf=true&value=" + content + "&x=0&y=0";

                url = "http://oasis.ssu.ac.kr/search/DetailView.ax?sid=1&cid=387718";

                doc = Jsoup.connect(url).get();

                text = doc.select("tbody td");

//                text = doc.select("span.title a");
//                hrefs = doc.select("span.title a[href]");
//                textImgUrl = doc.select("dd.thumb img");
//                circstats = doc.select("span.circstat");
//                countText = doc.select("h3.stitle");
//                author = doc.select("div.search_result_brief_contents");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            String resultText = "";
//            for(Element e : countText) {
//                StringTokenizer st = new StringTokenizer(e.text().toString().replaceAll("\\D+"," "));
//                resultText += "전체 검색 결과 개수 : " + st.nextToken() + "\n";
//                resultText += "현재 페이지 결과 개수 : " + st.nextToken() + "\n";
//            }
//
//            resultText += "\n\n----\n\n";
//
//            for(Element e : circstats) {
//                Log.d("TAG", "doInBackground: e : " + e);
//                statusList.add(e.text().toString());
//            }
//
//            resultText += "\n\n----\n\n";
//
//            for(Element e : text) {
//                Log.d("TAG", "doInBackground: e : " + e);
//                titleList.add(e.text().toString());
//            }
//
//            for(Element e : author) {
//                // .과, 파싱 필요
//                authorList.add(e.text().toString());
//            }
//
//            for(Element e : hrefs) {
//                Log.d("TAG", "doInBackground: e : " + e);
//                linkList.add("http://oasis.ssu.ac.kr/search/" + e.attr("href"));
//            }
//
//            for(Element e : textImgUrl) {
//                Log.d("TAG", "doInBackground: e : " + e);
//                imageUrlList.add(e.absUrl("src").toString());
//            }
//
//            for(int i = 0; i < titleList.size(); ++i) {
//                resultText += titleList.get(i) + "\n";
//                resultText += statusList.get(i) + "\n";
//                resultText += imageUrlList.get(i) + "\n";
//                resultText += linkList.get(i) + "\n\n";
//                resultText += authorList.get(i) + "\n\n";
//            }

            for(Element e : text) {
                resultText += e.text().toString();
                resultText += '\n';
            }
            mTextView.setText(resultText);
        }
    }
}
