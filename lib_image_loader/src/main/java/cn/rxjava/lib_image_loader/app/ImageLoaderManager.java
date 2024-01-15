package cn.rxjava.lib_image_loader.app;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import cn.rxjava.lib_image_loader.R;
import cn.rxjava.lib_image_loader.image.CustomRequestListener;
import cn.rxjava.lib_image_loader.image.Utils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图处加载类，外界唯一调用类
 * 直持为 view，notifaication，appwidget，viewgroup 加载图片
 */
public class ImageLoaderManager {
    private ImageLoaderManager() {
    }

    private static class SingletonHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    public static ImageLoaderManager getInstance() {
        return ImageLoaderManager.SingletonHolder.instance;
    }


    /**
     * 不带回调的加载图片
     *
     * @param imageView
     * @param url
     */
    public void displayImageForView(ImageView imageView, String url) {
        this.displayImageForView(imageView, url, null);
    }


    /**
     * 带回调的加载图片方法
     *
     * @param imageView
     * @param url
     * @param requestListener
     */
    public void displayImageForView(ImageView imageView, String url, CustomRequestListener requestListener) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .listener(requestListener)
                .into(imageView);
    }

    /**
     * 初始化选项
     *
     * @return
     */
    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.b4y)
                .error(R.mipmap.b4y)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
        return options;
    }

    /**
     * 加载圆形图片方法
     *
     * @param imageView
     * @param url
     */
    public void displayImageForCircle(final ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new BitmapImageViewTarget(imageView) {
                    //将imageView包装成target
                    @Override
                    protected void setResource(final Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 完成为ViewGroup设置背景并模糊处理
     *
     * @param group
     * @param url
     */
    public void displayImageForViewGroup(final ViewGroup group, String url) {
        Glide.with(group.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                //设置宽高
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        final Bitmap res = resource;
                        Observable.just(resource)
                                //转换成Drawable
                                .map(new Function<Bitmap, Drawable>() {
                                    @Override
                                    public Drawable apply(Bitmap bitmap) {
                                        //将进行bitmap模糊处理并转换为Drawable
                                        Drawable drawable = new BitmapDrawable(Utils.doBlur(res, 100, true));
                                        return drawable;
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Drawable>() {
                                    @Override
                                    public void accept(Drawable drawable) throws Exception {
                                        group.setBackground(drawable);
                                    }
                                });
                    }
                });
    }

    /**
     * 为非view加载图片
     *
     * @param context
     * @param target
     * @param url
     */
    private void displayImageForTarget(Context context, Target target, String url) {
        this.displayImageForTarget(context, target, url, null);
    }

    /**
     * 为非view加载图片
     *
     * @param context
     * @param target
     * @param url
     * @param requestListener
     */
    private void displayImageForTarget(Context context, Target target, String url,
                                       CustomRequestListener requestListener) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .fitCenter()
                .listener(requestListener)
                .into(target);
    }

    /**
     * 为notification中的id控件加载图片
     *
     * @param context
     * @param rv
     * @param id
     * @param notification
     * @param NOTIFICATION_ID
     * @param url
     */
    public void displayImageForNotification(Context context, RemoteViews rv, int id,
                                            Notification notification, int NOTIFICATION_ID, String url) {
        this.displayImageForTarget(context,
                initNotificationTarget(context, id, rv, notification, NOTIFICATION_ID), url);
    }

    /**
     * 初始化Notification Target
     *
     * @param context
     * @param id
     * @param rv
     * @param notification
     * @param NOTIFICATION_ID
     * @return
     */
    private NotificationTarget initNotificationTarget(Context context, int id, RemoteViews rv,
                                                      Notification notification, int NOTIFICATION_ID) {
        NotificationTarget notificationTarget =
                new NotificationTarget(context, id, rv, notification, NOTIFICATION_ID);
        return notificationTarget;
    }
}
