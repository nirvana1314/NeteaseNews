package com.lst.neteasenews;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lst.neteasenews.splash.model.AdDetail;
import com.lst.neteasenews.splash.model.Ads;
import com.lst.neteasenews.utils.Md5Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by lisongtao on 2017/10/27.
 */

public class DownloadImageService extends IntentService {
    public static final String ADS_NAME = "ads";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */
    public DownloadImageService() {
        super("DownloadImageService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v("lst", "Log-onHandleIntent");
        Ads ads = (Ads) intent.getSerializableExtra(ADS_NAME);
        List<AdDetail> list = ads.getAds();
        for (AdDetail detail:list) {
            List<String> imgs = detail.getRes_url();
            downloadImage(imgs.get(0), Md5Helper.toMD5(imgs.get(0)));
        }
    }

    private void downloadImage(String imageUrl, String imageName) {
        if (checkImageExits(imageName)) {
            Log.v("lst", "图片已存在");
            return;
        }
        URL url = null;
        try {
            url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            if (bitmap != null) {
                saveToSDCard(bitmap, imageName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void saveToSDCard(Bitmap bitmap, String imageName) {

//        File sdCard = Environment.getExternalStorageDirectory();
        File rom = this.getCacheDir();
        File cacheFolder = new File(rom,".lstCache");
        if (!cacheFolder.exists()){
            cacheFolder.mkdirs();
        }

        File image = new File(cacheFolder,imageName+".jpg");
        if (image.exists()) {
            return;
        }

        try  {
            FileOutputStream out = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e){

        }

    }

    private boolean checkImageExits(String imageName) {
        String name = this.getCacheDir()+"/.lstCache/"+imageName+".jpg";
        File file = new File(name);
        if (file != null && file.exists()) {
            return true;
        }
        return false;
    }
}
