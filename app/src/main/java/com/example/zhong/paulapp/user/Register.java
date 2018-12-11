package com.example.zhong.paulapp.user;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhong.paulapp.R;
import com.example.zhong.paulapp.util.Msg;
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

public class Register extends Activity implements View.OnClickListener {

    private EditText A;
    private EditText AA;
    private EditText AAA;
    private Button button;

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
        setContentView(R.layout.activity_register);
        initView();
        initViewListent();
    }

    private void initView() {
       A = (EditText) findViewById(R.id.A);
       AA = (EditText) findViewById(R.id.AA);
       AAA = (EditText) findViewById(R.id.AAA);
       button = (Button) findViewById(R.id.button);
    }

    private void initViewListent() {
       button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                register();
                break;
        }
    }

    private void register() {
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
                    String phone = A.getText().toString().trim();
                    String password = AA.getText().toString().trim();
                    String affirmPassword = AAA.getText().toString().trim();

                    if (affirmPassword == null || affirmPassword.trim().length() == 0) {
                        Toast.makeText(Register.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!affirmPassword.equals(password)) {
                        Toast.makeText(Register.this, "密码不一致", Toast.LENGTH_SHORT).show();
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

    class Post1 extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                RequestBody requestBody = new FormBody.Builder()
                        .add("phone", strings[0])
                        .add("password", strings[1])
                        .build();

                Request request = new Request.Builder().url("http://10.0.2.2:8086/api/user/register").post(requestBody).build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            }catch (Exception e) {
                Toast.makeText(Register.this, "操作出错", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                Msg msg = new Gson().fromJson(s, Msg.class);
                if (msg.getCode() == 200) {
                    Toast.makeText(Register.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
