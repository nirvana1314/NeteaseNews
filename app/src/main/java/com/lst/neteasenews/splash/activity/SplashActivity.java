package com.lst.neteasenews.splash.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lst.neteasenews.R;
import com.lst.neteasenews.Service.DownloadImageService;
import com.lst.neteasenews.splash.model.Action;
import com.lst.neteasenews.splash.model.AdDetail;
import com.lst.neteasenews.splash.model.Ads;
import com.lst.neteasenews.utils.Const;
import com.lst.neteasenews.utils.ImageUtils;
import com.lst.neteasenews.utils.JsonUtils;
import com.lst.neteasenews.utils.Md5Helper;
import com.lst.neteasenews.utils.SharePreferenceUtils;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static java.lang.System.currentTimeMillis;

/**
 * Created by lisongtao on 2017/10/27.
 */

public class SplashActivity extends Activity {

    private final OkHttpClient client = new OkHttpClient();
    private Ads ads = null;
    ImageView mTopView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mTopView = (ImageView) findViewById(R.id.iv_top);

        //  处理接口缓存
        String next_time = SharePreferenceUtils.getString(SplashActivity.this, SharePreferenceUtils.Next_Req_Time);
        String repStr = SharePreferenceUtils.getString(SplashActivity.this, SharePreferenceUtils.AD_RESPONSE_STRING);
        if (TextUtils.isEmpty(repStr)) { //  没有缓存
            getADs();
        }else {
            Long nextTime = Long.parseLong(next_time);
            Long curTime = System.currentTimeMillis();
            if (curTime > nextTime) {
                getADs();
            }else {
                Log.v(Const.LOG_PRE, "读取接口缓存");
                runDownService(repStr);
                showImage(repStr);
            }
        }


    }

    public void showImage(String repStr) {
        Ads ads = JsonUtils.parse(repStr, Ads.class);
        Random random = new Random();

        int index = random.nextInt(ads.getAds().size() - 1);
        AdDetail detail = ads.getAds().get(index);
        String imageName = Md5Helper.toMD5(detail.getRes_url().get(0));
        Bitmap bitmap = ImageUtils.getBitMapWithUrl(this, imageName);
        mTopView.setImageBitmap(bitmap);

        final Action params = detail.getAction_params();

        mTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到webview
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, WebViewActivity.class);
                intent.putExtra("params", params);
                startActivity(intent);
            }
        });
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

                    String responseStr = responseBody.string();// 只能调用一次
                    System.out.println(responseStr);

                    runDownService(responseStr);

                    //  处理接口缓存
                    long currentTime = currentTimeMillis();
                    long nextTime = currentTime + ads.getNext_req() * 1000;
                    String nextTimeStr = nextTime + "";
                    SharePreferenceUtils.saveString(SplashActivity.this, SharePreferenceUtils.Next_Req_Time, nextTimeStr);
                    SharePreferenceUtils.saveString(SplashActivity.this, SharePreferenceUtils.AD_RESPONSE_STRING, responseStr);
                }
            }
        });
    }

    private void runDownService(String responseStr) {
        ads = JsonUtils.parse(responseStr, Ads.class);

        System.out.println(ads);

        //  启动下载图片服务
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, DownloadImageService.class);
        intent.putExtra(DownloadImageService.ADS_NAME,ads);
        startService(intent);
    }


}
