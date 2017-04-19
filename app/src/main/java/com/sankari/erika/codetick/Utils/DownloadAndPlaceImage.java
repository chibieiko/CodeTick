package com.sankari.erika.codetick.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by erika on 4/16/2017.
 */

public class DownloadAndPlaceImage extends AsyncTask<String, Void, Bitmap> {
    private ImageView image;

    public DownloadAndPlaceImage(ImageView image) {
        this.image = image;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap userIcon = null;
        try (InputStream in = new java.net.URL(urlDisplay).openStream()){
            userIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userIcon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }
}
