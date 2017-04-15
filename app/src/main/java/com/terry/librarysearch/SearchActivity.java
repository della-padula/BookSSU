package com.terry.librarysearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    EditText searchContent;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchContent = (EditText) findViewById(R.id.searchContent);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchContent.getText().toString().equals("")) {
                    Toast.makeText(SearchActivity.this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SearchActivity.this, ResultListActivity.class);
                    intent.putExtra("content", searchContent.getText().toString().replaceAll(" ", "+"));
                    searchContent.setText("");
                    startActivity(intent);
                }
            }
        });
    }
}
