package com.lst.neteasenews.splash.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lst.neteasenews.DownloadImageService;
import com.lst.neteasenews.R;
import com.lst.neteasenews.splash.model.Ads;
import com.lst.neteasenews.utils.Const;
import com.lst.neteasenews.utils.JsonUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by lisongtao on 2017/10/27.
 */

public class SplashActivity extends Activity {

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getADs();
    }

    public void getADs() {
        Request request = new Request.Builder()
                .url(Const.AD_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    String reponseStr = responseBody.string();// 只能调用一次
                    System.out.println(reponseStr);

                    Ads ads = JsonUtils.parse(reponseStr, Ads.class);

                    System.out.println(ads);

                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, DownloadImageService.class);
                    intent.putExtra(DownloadImageService.ADS_NAME,ads);
                    startService(intent);
                }
            }
        });
    }
}
