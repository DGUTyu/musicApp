package cn.rxjava.lib_network.okhttp.listener;

/**
 * 包装类
 */
public class DisposeDataHandle {
    /**
     * listener
     */
    public DisposeDataListener mListener = null;
    /**
     * 类型
     */
    public Class<?> mClass = null;
    /**
     * 文件保存路径
     */
    public String mSource = null;

    public DisposeDataHandle(DisposeDataListener listener) {
        this.mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz) {
        this.mListener = listener;
        this.mClass = clazz;
    }

    public DisposeDataHandle(DisposeDataListener listener, String source) {
        this.mListener = listener;
        this.mSource = source;
    }
}
