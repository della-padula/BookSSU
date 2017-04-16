package com.terry.librarysearch;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.terry.librarysearch.CustomView.CustomFontButton;
import com.terry.librarysearch.CustomView.CustomFontEditText;
import com.terry.librarysearch.utils.AsteriskPasswordTransformationMethod;
import com.terry.librarysearch.utils.VolleySingleton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {
    private static final String STUDYROOM_URL = "http://oasis.ssu.ac.kr/service/StudyRoom.Cal.ax";
    private static final String LOGIN_URL = "http://oasis.ssu.ac.kr/m/identity/Login.ax";
    private static final String REDIRECT_URL = "http://oasis.ssu.ac.kr/m/PyxisRedirect.ax?home=http://oasis.ssu.ac.kr&target=/service/StudyRoom.Cal.ax";
    private static final String SPLITTER = "location.href=\"";

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

    static protected StringRequest getStringRequest(int method, String url, Response.Listener<String> listener, final Map<String, String> params) {
        return new StringRequest(method, url, listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    static protected void get(String url, Response.Listener<String> listener) {
        StringRequest stringRequest = getStringRequest(Request.Method.GET, url, listener, null);
        VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
    }

    static protected void post(String url, Response.Listener<String> listener, final Map<String, String> params) {
        StringRequest stringRequest = getStringRequest(Request.Method.POST, url, listener, params);
        VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
    }

    static public void isLogin(final ResponseCallback responseCallback) {
        get(STUDYROOM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Element element = document.select("table.tbl_diary").first();
                responseCallback.onSuccess(element != null);
            }
        });
    }

    public interface ResponseCallback {
        void onSuccess(boolean success);
        void onResponse(String response);
    }

    protected class LoginException extends Exception {
        private EditText editText;

        public LoginException(String message, EditText editText) {
            super(message);
            this.editText = editText;
        }

        public EditText getEditText() {
            return this.editText;
        }
    }

    @OnClick(R.id.loginButton)
    public void loginValidate() {
        String username = this.idEditText.getText().toString();
        String password = this.pwEditText.getText().toString();

        try {
            if (username.length() == 0) throw new LoginException("학번을 입력해주세요.", this.idEditText);
            if (password.length() == 0) throw new LoginException("도서관 비밀번호를 입력해주세요.", this.pwEditText);
        } catch (final LoginException e) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage(e.getMessage())
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            e.getEditText().requestFocus();
                        }
                    }).setCancelable(false).show();
        }

        this.login(username, password, new ResponseCallback() {
            @Override
            public void onSuccess(boolean success) {
                if (success) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage("로그인이 완료되었습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).show();
                }
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }

    private void login(String username, String password, final ResponseCallback responseCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("userID", username);
        params.put("password", password);
        params.put("LOGIN_MODE", "LOGIN");

        post(LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Element form = document.select("form[name=frmDamonmedia]").first();
                String action = form.attr("action");
                final Elements inputs = form.select("input");
                Map<String, String> params = new HashMap<String, String>();
                for (Element input : inputs) {
                    params.put(input.attr("name"), input.attr("value"));
                }

                post(action, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        get(REDIRECT_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                int start = response.indexOf(SPLITTER) + SPLITTER.length();
                                int end = response.indexOf("\"", start);
                                String url = response.substring(start, end);
                                get(url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        responseCallback.onSuccess(true);
                                    }
                                });
                            }
                        });
                    }
                }, params);
            }
        }, params);
    }
}
