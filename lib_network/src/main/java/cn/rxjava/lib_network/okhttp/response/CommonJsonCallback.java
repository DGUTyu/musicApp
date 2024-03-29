package cn.rxjava.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import cn.rxjava.lib_network.okhttp.exception.OkHttpException;
import cn.rxjava.lib_network.okhttp.listener.DisposeDataHandle;
import cn.rxjava.lib_network.okhttp.listener.DisposeDataListener;
import cn.rxjava.lib_network.okhttp.listener.DisposeDownloadListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 处理json类型的响应回调
 */
public class CommonJsonCallback implements Callback {
    /**
     * 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
     */
    protected final String RESULT_CODE = "ecode";
    /**
     *
     */
    protected final int RESULT_CODE_VALUE = 0;
    /**
     *
     */
    protected final String ERROR_MSG = "emsg";
    /**
     *
     */
    protected final String EMPTY_MSG = "";

    /**
     * 网络异常
     */
    protected final int NETWORK_ERROR = -1;
    /**
     * json解析异常
     */
    protected final int JSON_ERROR = -2;
    /**
     * 未知异常
     */
    protected final int OTHER_ERROR = -3;

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    /**
     * 处理响应
     *
     * @param responseObj
     */
    private void handleResponse(Object responseObj) {
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            /**
             * 协议确定后看这里如何修改
             */
            JSONObject result = new JSONObject(responseObj.toString());
            if (mClass == null) {
                mListener.onSuccess(result);
            } else {
                Object obj = new Gson().fromJson(responseObj.toString(), mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}
