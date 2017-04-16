package com.terry.librarysearch;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.terry.librarysearch.CustomView.CustomFontButton;
import com.terry.librarysearch.CustomView.CustomFontEditText;
import com.terry.librarysearch.utils.AsteriskPasswordTransformationMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @BindView(R.id.idContent)
    CustomFontEditText idEditText;
    @BindView(R.id.pwContent)
    CustomFontEditText pwEditText;
    @BindView(R.id.loginButton)
    CustomFontButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        pwEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    @OnClick(R.id.loginButton)
    public void loginValidate() {

    }
}
