package com.terry.librarysearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.terry.librarysearch.CustomView.CustomFontTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ResultListActivity extends AppCompatActivity {

    private List<ResultItem> resultItemList;
    private RecyclerView mRecyclerView;
    private RelativeLayout mRelativeLayout;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private CustomFontTextView mPlaceHolderTitle;
    private CustomFontTextView mPlaceHolderContent;
    private String content;
    private String TAG = "TAG";
    private ActionBar actionBar;
    private int page = 1;
    private boolean isLoadMore = false;
    private int loadedItemCount = 0;
    private int totalItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        Intent intent = getIntent();
        content = intent.getStringExtra("content");

        actionBar = getSupportActionBar();

        final TextView TextViewNewFont = new TextView(ResultListActivity.this);
        FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        TextViewNewFont.setLayoutParams(layoutparams);
        TextViewNewFont.setText("검색결과 : " + content);

        TextViewNewFont.setTextColor(getResources().getColor(R.color.colorWhite));
        TextViewNewFont.setTextSize(18);

        Typeface FontLoaderTypeface = Typeface.createFromAsset(getAssets(), getString(R.string.naum_square_bold));
        TextViewNewFont.setTypeface(FontLoaderTypeface);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(TextViewNewFont);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.placeholder);
        mPlaceHolderTitle = (CustomFontTextView) findViewById(R.id.textView2);
        mPlaceHolderContent = (CustomFontTextView) findViewById(R.id.textView3);
        resultItemList = new ArrayList<>();
        resultItemList.clear();

        getResultList();
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DataAdapter(resultItemList, mRecyclerView, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                if(page < totalItemCount / loadedItemCount) {
                    // Page Increase;
                    page++;
                    new GetHtmlText().execute();
                }

            }
        });
    }

    private void getResultList() {
        new GetHtmlText().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                String url = "http://oasis.ssu.ac.kr/search/Search.Result.ax?sid=2&mf=true&q=ALL%3A" + content + "%3A1&eq=&qt=&qf=" + content + "&f=&br=&cl=1+2+60&gr=1&rl=&page=" + page + "pageSize=&h=&cr=&py=&subj=&facet=&nd=&tabID=&formaction=true&item=ALL&value=" + content;
                //String url = "http://oasis.ssu.ac.kr/search/Search.Result.ax?sid=2&q=ALL%3A" + content + "%3A1&mf=true&qf=" + content + "&cl=1%202%2060&gr=1&vid=1&page=" + page;
                //String url = "http://oasis.ssu.ac.kr/search/Search.Result.ax?sid=1&mf=true&q=ALL%3A" + content + "&eq=&qt=&qf=" + content + "&f=&br=&cl=1+2+60+3+4+5+6+9+10+50+51+54+33+34+55+11+12+13+14+15+16+17+18+21+22+53+52+57+58&gr=1+2+3+4+8+7+5+9+14+15&rl=&page=" + page + "&pageSize=&h=&cr=&py=&subj=&facet=&nd=&tabID=&formaction=true&item=ALL&value=" + content;
                doc = Jsoup.connect(url).timeout(3000).get();
                text = doc.select("span.title a");
                hrefs = doc.select("span.title a[href]");
                textImgUrl = doc.select("dd.thumb img");
                circstats = doc.select("span.circstat");
                countText = doc.select("h3.stitle");
                author = doc.select("div.search_result_brief_contents");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException ne) {
                Toast.makeText(ResultListActivity.this, "서버 요청 시간 초과", Toast.LENGTH_SHORT).show();
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            for(Element e : countText) {
                StringTokenizer st = new StringTokenizer(e.text().toString().replaceAll("\\D+"," "));

                totalItemCount = Integer.parseInt(st.nextToken());
                loadedItemCount = Integer.parseInt(st.nextToken());
            }

            for(Element e : circstats)
                statusList.add(e.text().toString());

            for(Element e : text)
                titleList.add(e.text().toString());

            for(Element e : author) {
                StringTokenizer st = new StringTokenizer(e.text().toString(), "/");
                // First Token is Title
                String tempStr = st.nextToken();

                if(st.hasMoreTokens()) {
                    StringTokenizer st2 = new StringTokenizer(st.nextToken(), ",");
                    String authorString = "";
                    int tokenizerCount = 0;
                    while (st2.hasMoreTokens()) {
                        if (tokenizerCount < 3) {
                            authorString += st2.nextToken();
                            tokenizerCount++;
                        } else {
                            break;
                        }
                    }
                    authorList.add(authorString);
                } else {
                    authorList.add(tempStr);
                }
            }

            for(Element e : hrefs)
                linkList.add("http://oasis.ssu.ac.kr/search/" + e.attr("href"));

            for(Element e : textImgUrl)
                imageUrlList.add(e.absUrl("src").toString());

            if(titleList.size() < 1) {
                mPlaceHolderTitle.setText("검색결과가 없습니다!");
                mPlaceHolderContent.setText("검색어를 확인해주세요.");
                mRecyclerView.setVisibility(View.GONE);
                mRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                mPlaceHolderTitle.setText("검색중입니다!");
                mPlaceHolderContent.setText("잠시만 기다려주세요.");
                mRecyclerView.setVisibility(View.VISIBLE);
                mRelativeLayout.setVisibility(View.GONE);

                for (int i = 0; i < titleList.size(); ++i) {
                    resultItemList.add(new ResultItem(imageUrlList.get(i), titleList.get(i), authorList.get(i), statusList.get(i), linkList.get(i)));
                    mAdapter.notifyItemInserted(resultItemList.size());
                }

                mAdapter.notifyDataSetChanged();
                mAdapter.setLoaded();
            }
        }
    }
}
