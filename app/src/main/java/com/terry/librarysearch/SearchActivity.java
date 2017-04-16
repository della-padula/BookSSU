package com.terry.librarysearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.terry.librarysearch.CustomView.CustomFontButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    @BindView(R.id.searchContent)
    EditText searchContent;
    @BindView(R.id.tv_version)
    TextView versionTextView;
    @BindView(R.id.searchButton)
    CustomFontButton searchButton;
    @BindView(R.id.reserveButton)
    CustomFontButton reserveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        //Playstore Version Valid Check
        new MommooAsyncTask().execute();

        try {
            versionTextView.setText("Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName.toString());
            Log.d("TAG", "onCreate: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName.toString());
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.searchButton)
    public void onSearchProcess() {
        Log.d(TAG, "onSearchProcess: Start");
        if(searchContent.getText().toString().equals("")) {
            AlertDialog builder = new AlertDialog.Builder(SearchActivity.this)
                    .setMessage("검색어를 입력해주세요.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(false).show();

            TextView textView = (TextView) builder.findViewById(android.R.id.message);
            TextView textView2 = (TextView) builder.findViewById(android.R.id.button1);
            Typeface face= Typeface.createFromAsset(getAssets(), getString(R.string.naum_square_bold));
            textView.setTypeface(face);
            textView2.setTypeface(face);

            CookieManager.getInstance().setAcceptCookie(true);
            WebkitCookieManagerProxy webkitCookieManagerProxy = new WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(webkitCookieManagerProxy);

            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(new StringRequest(Request.Method.GET, "http://oasis.ssu.ac.kr/service/StudyRoom.Cal.ax", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Document document = Jsoup.parse(response);
                    Element table = document.select("table.tbl_diary").first();
                    if (table != null) {
                        // TODO: 얻어온 세미나룸 목록 처리
                    } else {
                        requestQueue.add(new StringRequest(Request.Method.POST, "http://oasis.ssu.ac.kr/m/identity/Login.ax", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Document document = Jsoup.parse(response);
                                Element form = document.select("form[name=frmDamonmedia]").first();
                                String action = form.attr("action");
                                final Elements inputs = form.select("input");
                                requestQueue.add(new StringRequest(Request.Method.POST, action, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        requestQueue.add(new StringRequest(Request.Method.GET, "http://oasis.ssu.ac.kr/m/PyxisRedirect.ax?home=http://oasis.ssu.ac.kr&target=/service/StudyRoom.Cal.ax", new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                final String SPLITTER = "location.href=\"";
                                                int start = response.indexOf(SPLITTER) + SPLITTER.length();
                                                int end = response.indexOf("\"", start);
                                                response = response.substring(start, end);

                                                requestQueue.add(new StringRequest(Request.Method.GET, response, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        // TODO: 로그인 완료 처리
                                                    }
                                                }, null));
                                            }
                                        }, null));
                                    }
                                }, null) {
                                    @Override
                                    protected Map<String,String> getParams(){
                                        Map<String, String> params = new HashMap<String, String>();
                                        for (Element input : inputs) {
                                            params.put(input.attr("name"), input.attr("value"));
                                        }
                                        return params;
                                    }
                                });
                            }
                        }, null) {
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("userID", "학번");
                                params.put("password", "비밀번호");
                                params.put("LOGIN_MODE", "LOGIN");
                                return params;
                            }
                        });
                    }
                }
            }, null));
        } else {
            Intent intent = new Intent(SearchActivity.this, ResultListActivity.class);
            intent.putExtra("content", searchContent.getText().toString());
            searchContent.setText("");
            startActivity(intent);
        }
    }

    @OnClick(R.id.reserveButton)
    public void onReserveProcess() {
        Log.d(TAG, "onReserveProcess: Start");
        //Dummy Code : Open Login Activity
        startActivity(new Intent(SearchActivity.this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateDialog() {
        AlertDialog builder = new AlertDialog.Builder(SearchActivity.this)
                .setMessage("업데이트가 필요합니다. 확인을 누르면 플레이 스토어로 이동합니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        finish();
                    }
                }).setCancelable(false).show();

        TextView textView = (TextView) builder.findViewById(android.R.id.message);
        TextView textView2 = (TextView) builder.findViewById(android.R.id.button1);
        Typeface face= Typeface.createFromAsset(getAssets(), getString(R.string.naum_square_bold));
        textView.setTypeface(face);
        textView2.setTypeface(face);
    }

    class MommooAsyncTask extends AsyncTask<String,Void,String> {
        public String device_version = "";
        public String store_version = "";
        public String result;

        @Override
        protected String doInBackground(String... params) {
            store_version = MarketVersionChecker.getMarketVersion(getPackageName());
            try {
                device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Log.d("TAG", "doInBackground: StoreVersion : " + store_version + ", DeviceVersion : " + device_version);
            Log.d("TAG", "doInBackground: " + store_version.compareTo(device_version));
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (store_version.compareTo(device_version) > 0) {
                showUpdateDialog();
            }
        }
    }

}
