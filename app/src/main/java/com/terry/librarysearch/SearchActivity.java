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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.terry.librarysearch.CustomView.CustomFontButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.terry.librarysearch.LoginActivity.isLogin;

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
        } else {
            Intent intent = new Intent(SearchActivity.this, ResultListActivity.class);
            intent.putExtra("content", searchContent.getText().toString());
            searchContent.setText("");
            startActivity(intent);
        }
    }

    @OnClick(R.id.reserveButton)
    public void onReserveProcess() {
        isLogin(new LoginActivity.ResponseCallback() {
            @Override
            public void onSuccess(boolean success) {
                if (success) {
                } else {
                    startActivity(new Intent(SearchActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onResponse(String response) {
            }
        });
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
