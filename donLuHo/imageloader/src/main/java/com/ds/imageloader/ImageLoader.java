package com.ds.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * author : Sai
 * time   : 2019/06/01
 * desc   : 图片加载
 */
public class ImageLoader {
    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void displayImageNoPlaceHolder(Context context, String url, ImageView imageView) {
        if(!TextUtils.isEmpty(url))
        Glide.with(context)
                .load( url)
                .into(imageView);
    }
    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void displayImage(Context context, String url, ImageView imageView) {
        if(!TextUtils.isEmpty(url))
        Glide.with(context)
                .load( url).placeholder(R.drawable.ic_zhanwei).error(R.drawable.ic_zhanwei)
                .into(imageView);
    }
    /**
     * 加载头像图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void displayAvatar(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load( url).placeholder(R.drawable.ic_wd_portrait).error(R.drawable.ic_wd_portrait)
                .into(imageView);
    }

    /**
     * 加载头像图片
     *
     * @param context
     * @param bitmap
     * @param imageView
     */
    public static void displayAvatar(Context context, Bitmap bitmap, ImageView imageView) {
        Glide.with(context)
                .load(bitmap).placeholder(R.drawable.ic_wd_portrait).error(R.drawable.ic_wd_portrait)
                .into(imageView);
    }

    /**
     * 加载头像图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void displayImage(Context context, String url, ImageView imageView, int placeholderId) {
        Glide.with(context)
                .load( url).placeholder(placeholderId).error(placeholderId)
                .into(imageView);
    }
    /**
     * 下载图片
     *
     * @param context
     * @param url
     */
    public static void downloadImage(Context context, String url, RequestListener<File> listener) {

        Glide.with(context).downloadOnly().load(url).addListener(listener).submit();

    }

}
