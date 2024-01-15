package cn.rxjava.lib_network.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于构建 HTTP 请求参数
 */
public class RequestParams {
    /**
     * 用于存储键值对形式的字符串参数
     */
    public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    /**
     * 用于存储键值对形式的文件参数
     */
    public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<String, Object>();

    /**
     * 无参构造方法，创建一个空的 RequestParams 实例
     * Constructs a new empty {@code RequestParams} instance.
     */
    public RequestParams() {
        this((Map<String, String>) null);
    }

    /**
     * 通过给定的 Map 构造 RequestParams 实例，将其中的键值对添加到 urlParams 中
     * Constructs a new RequestParams instance containing the key/value string
     * params from the specified map.
     *
     * @param source the source key/value string map to add.
     */
    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 构造一个包含单个键值对的 RequestParams 实例
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string param.
     *
     * @param key   the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }

    /**
     * 添加一个键值对到 urlParams 中。如果键或值为 null，则不会添加
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * 添加一个键值对到 fileParams 中。这个方法专门用于处理文件参数，允许将不同类型的文件对象添加到请求中。
     * 如果键为 null，则不会添加。
     *
     * @param key
     * @param object
     * @throws FileNotFoundException
     */
    public void put(String key, Object object) throws FileNotFoundException {
        if (key != null) {
            fileParams.put(key, object);
        }
    }

    /**
     * 判断是否存在参数。如果 urlParams 或 fileParams 中有参数，返回 true，否则返回 false
     *
     * @return
     */
    public boolean hasParams() {
        if (urlParams.size() > 0 || fileParams.size() > 0) {
            return true;
        }
        return false;
    }
}