package com.terry.librarysearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BookDetailActivity extends Activity {

    private ArrayList<DetailItem> detailItemList;
    private String TAG = "TAG";
    private ImageView bookImage;
    private RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvBalheng;
    private TextView tvChunggu;
    private TextView tvISBN;

    private String bookCode;
    private String bookImageUrl;
    private String bookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_book_detail);

        bookImage = (ImageView) findViewById(R.id.book_image);
        tvTitle = (TextView) findViewById(R.id.tv_booktitle);
        tvAuthor = (TextView) findViewById(R.id.textView12);
        tvBalheng = (TextView) findViewById(R.id.textView11);
        tvChunggu = (TextView) findViewById(R.id.textView10);
        mRecyclerView = (RecyclerView) findViewById(R.id.detail_recyclerview);
        tvISBN = (TextView) findViewById(R.id.textView9);

        Intent intent = getIntent();
        bookCode = intent.getStringExtra("bookLinkUrl");
        bookImageUrl = intent.getStringExtra("bookImageUrl");
        bookTitle = intent.getStringExtra("bookTitle");

        Glide.with(this).load(bookImageUrl).into(bookImage);
        tvTitle.setText(bookTitle);
        detailItemList = new ArrayList<>();
        detailItemList.clear();

        getDetailData();
        initializeRecyclerView();
    }

    private void getDetailData() {
        new GetHtmlText().execute();
    }

    private void applyLandDetail(ArrayList<String> list) {
        int start = 0;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals("청구기호 :")) {
                i += 2;
                start = i;
                break;
            }
        }
        // 대출정보 받아오기
        for (int i = start; i < list.size(); ++i) {
            if (list.get(i).startsWith("E")) {
                Log.d(TAG, "applyLandDetail: " + list.get(i));
                detailItemList.add(new DetailItem(list.get(++i), list.get(++i), list.get(++i), list.get(++i)));
            }
        }
    }

    private void initializeRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(detailItemList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void applyToView(ArrayList<String> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals("개인저자 :")) {
                StringTokenizer tokenizer = new StringTokenizer(list.get(++i));
                tvAuthor.setText(tokenizer.nextToken().replaceAll(" ", ""));
            } else if (list.get(i).equals("발행사항 :")) {
                tvBalheng.setText(list.get(++i));
            } else if (list.get(i).equals("ISBN :")) {
                tvISBN.setText(list.get(++i));
            } else if (list.get(i).equals("청구기호 :")) {
                tvChunggu.setText(list.get(++i));
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private Context context;
        private ArrayList<DetailItem> mItems;
        private int lastPosition = -1;

        public MyAdapter(ArrayList<DetailItem> items, Context mContext) {
            mItems = items;
            context = mContext;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_list_row, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (mItems.get(position).getStatus().equals("이용가능")) {
                holder.imageView.setImageResource(R.drawable.icon_1);
            } else {
                holder.imageView.setImageResource(R.drawable.icon_2);
            }
            holder.textViewCodeLocation.setText(mItems.get(position).getBookCode() + " " + mItems.get(position).getLocation());
            holder.textViewChunggu.setText(mItems.get(position).getChunggu());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textViewCodeLocation;
            public TextView textViewChunggu;

            public ViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.ivStatus);
                textViewCodeLocation = (TextView) view.findViewById(R.id.tvCodeLocation);
                textViewChunggu = (TextView) view.findViewById(R.id.tvChunggu);
            }
        }
    }

    private class GetHtmlText extends AsyncTask<Void, Void, String> {
        Document doc;
        Elements text;
        ArrayList<String> bookDetail = new ArrayList<>();

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
            bookDetail.clear();
            for (Element e : text) {
                Log.d(TAG, "onPostExecute: " + e.text());
                bookDetail.add(e.text());
            }

            applyLandDetail(bookDetail);
            applyToView(bookDetail);
        }
    }
}
