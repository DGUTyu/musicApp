package cn.rxjava.lib_network.okhttp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.rxjava.lib_network.okhttp.cookie.SimpleCookieJar;
import cn.rxjava.lib_network.okhttp.https.HttpsUtils;
import cn.rxjava.lib_network.okhttp.listener.DisposeDataHandle;
import cn.rxjava.lib_network.okhttp.response.CommonFileCallback;
import cn.rxjava.lib_network.okhttp.response.CommonJsonCallback;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用来发送get, post请求的工具类，包括设置一些请求的共用参数
 */
public class CommonOkHttpClient {
    /**
     * 超时时间
     */
    private static final int TIME_OUT = 30;
    /**
     * 单例对象
     */
    private static OkHttpClient mOkHttpClient;

    //完成对OkHttpClient的初始化
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //默认信任域名
                return true;
            }
        });

        /**
         *  为所有请求添加请求头，看个人需求
         */
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        // 标明发送本次请求的客户端
                        .addHeader("User-Agent", "Imooc-Mobile")
                        .build();
                return chain.proceed(request);
            }
        });
        //持久化cookie
        okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        //设置时间
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        // 是否允许重定向
        okHttpClientBuilder.followRedirects(true);
        /**
         * trust all the https point
         */
        //okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    /**
     * get请求
     */
    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * post请求
     */
    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * 下载请求
     */
    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }
}
