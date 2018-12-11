package com.example.zhong.paulapp.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhong.paulapp.MainActivity;
import com.example.zhong.paulapp.mine.HeadPortrait;
import com.example.zhong.paulapp.util.LoadingActivity;
import com.example.zhong.paulapp.util.Msg;
import com.example.zhong.paulapp.R;
import com.example.zhong.paulapp.util.RsaHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Login extends Activity implements View.OnClickListener {

    private ImageView get_back;
    private EditText username;
    private EditText pass;
    private TextView forget_the_password;
    private Button login;
    private TextView register;

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    private OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    cookieStore.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initViewListent();
    }

    private void initView() {
        get_back = (ImageView) findViewById(R.id.login_btn_cancel);
        username = (EditText) findViewById(R.id.login_edit_account);
        pass = (EditText) findViewById(R.id.login_edit_pwd);
        forget_the_password = (TextView) findViewById(R.id.login_text_change_pwd);
        login = (Button) findViewById(R.id.login_btn_login);
        register = (TextView) findViewById(R.id.login_btn_register);
    }

    private void initViewListent() {
        get_back.setOnClickListener(this);
        username.setOnClickListener(this);
        pass.setOnClickListener(this);
        forget_the_password.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                login();
                break;

            case R.id.login_btn_cancel:
                finish();
                break;

            case R.id.login_text_change_pwd:
                Intent intent1 = new Intent(Login.this, HeadPortrait.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.login_btn_register:
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void login() {
        new Post().execute();
    }

    class Post extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                RequestBody requestBody = new FormBody.Builder()
                        .build();
                Request request = new Request.Builder().url("http://10.0.2.2:8086/api/user/pubkey").post(requestBody).build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    return response.body().string();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){

                Msg msg = new Gson().fromJson(s,Msg.class);
                if (msg.getCode() == 200){
                    String phone = username.getText().toString().trim();
                    String password = pass.getText().toString().trim();

                    if (phone == null || phone.trim().length() == 0) {
                        Toast.makeText(Login.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password == null || password.trim().length() == 0) {
                        Toast.makeText(Login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        PublicKey pubKey = RsaHelper.getPublicKey(msg.getData());

                        new Post1().execute(phone, RsaHelper.encryptData(password, "utf-8", pubKey));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class Post1 extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                RequestBody requestBody = new FormBody.Builder()
                        .add("phone",strings[0])
                        .add("password",strings[1])
                        .build();

                Request request = new Request.Builder().url("http://10.0.2.2:8086/api/user/login").post(requestBody).build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    return response.body().string();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                final Msg msg = new Gson().fromJson(s,Msg.class);
                if (msg.getCode() == 200){

                    final LoadingActivity dialog = new LoadingActivity(Login.this);
                    dialog.show();
                    // 两秒后关闭后dialog
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent login = new Intent(Login.this, MainActivity.class);
                            startActivity(login);
                            Toast.makeText(Login.this, msg.getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, 1000 * 2);
                } else {
                    Toast.makeText(Login.this, msg.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
