package com.sankari.erika.codetick.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Downloads user's icon image and places it to navigation drawer.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class DownloadAndPlaceImage extends AsyncTask<String, Void, Bitmap> {

    /**
     * Image view inside navigation drawer, where user's icon will be placed.
     */
    private ImageView image;

    /**
     * Receives the icon image.
     *
     * @param image icon image
     */
    public DownloadAndPlaceImage(ImageView image) {
        this.image = image;
    }

    /**
     * Loads user's icon in background.
     *
     * @param urls url of the icon
     * @return bitmap icon
     */
    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap userIcon = null;
        try (InputStream in = new java.net.URL(urlDisplay).openStream()) {
            userIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userIcon;
    }

    /**
     * Sets loaded bitmap icon to image view.
     *
     * @param bitmap bitmap icon
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }
}
