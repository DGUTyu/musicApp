package cn.rxjava.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.rxjava.lib_network.okhttp.exception.OkHttpException;
import cn.rxjava.lib_network.okhttp.listener.DisposeDataHandle;
import cn.rxjava.lib_network.okhttp.listener.DisposeDownloadListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 专门处理文件下载回调
 */
public class CommonFileCallback implements Callback {
    /**
     * 网络异常
     */
    protected final int NETWORK_ERROR = -1;
    /**
     * io异常
     */
    protected final int IO_ERROR = -2;
    /**
     *
     */
    protected final String EMPTY_MSG = "";

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    /**
     * listener
     */
    private DisposeDownloadListener mListener;
    /**
     * 文件路径
     */
    private String mFilePath;
    /**
     * 进度标志
     */
    private static final int PROGRESS_MESSAGE = 0x01;
    /**
     * 当前进度
     */
    private int mProgress;

    public CommonFileCallback(DisposeDataHandle handle) {
        this.mListener = (DisposeDownloadListener) handle.mListener;
        this.mFilePath = handle.mSource;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PROGRESS_MESSAGE:
                        mListener.onProgress((int) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final File file = handleResponse(response);
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (file != null) {
                    mListener.onSuccess(file);
                } else {
                    mListener.onFailure(new OkHttpException(IO_ERROR, EMPTY_MSG));
                }
            }
        });
    }

    /**
     * 此时还在子线程中，不则调用回调接口
     *
     * @param response
     * @return
     */
    private File handleResponse(Response response) {
        if (response == null) {
            return null;
        }

        InputStream inputStream = null;
        File file;
        FileOutputStream fos = null;
        byte[] buffer = new byte[2048];
        int length;
        int currentLength = 0;
        double sumLength;
        try {
            checkLocalFilePath(mFilePath);
            file = new File(mFilePath);
            fos = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            sumLength = (double) response.body().contentLength();
            // 循环读取，将读取的字节数保存到变量 length 中，同时作为整个表达式的值
            // 在下一次循环迭代时，buffer 中的内容会被重新填充
            while ((length = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
                currentLength += length;
                mProgress = (int) (currentLength / sumLength * 100);
                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, mProgress).sendToTarget();
            }
            fos.flush();
        } catch (Exception e) {
            file = null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 检查文件路径
     *
     * @param localFilePath
     */
    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0,
                localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
