package cn.rxjava.musicapp.imooc_voice.api;


import cn.rxjava.lib_network.okhttp.CommonOkHttpClient;
import cn.rxjava.lib_network.okhttp.listener.DisposeDataHandle;
import cn.rxjava.lib_network.okhttp.listener.DisposeDataListener;
import cn.rxjava.lib_network.okhttp.request.CommonRequest;
import cn.rxjava.lib_network.okhttp.request.RequestParams;

/**
 * 请求中心
 */
public class RequestCenter {

    /**
     * 常量内部类，存放请求地址
     */
    static class HttpConstants {
        private static final String ROOT_URL = "http://localhost:8000/api";
//        private static final String ROOT_URL = "http://39.97.122.129";

        /**
         * 首页请求接口
         */
        private static String HOME_RECOMMAND = ROOT_URL + "/module_voice/home_recommand";

        private static String HOME_RECOMMAND_MORE = ROOT_URL + "/module_voice/home_recommand_more";

        private static String HOME_FRIEND = ROOT_URL + "/module_voice/home_friend";

        /**
         * 登陆接口
         */
        public static String LOGIN = ROOT_URL + "/module_voice/login_phone";
    }

    public static void getRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.getRequest(HttpConstants.HOME_RECOMMAND, null, listener, null);
    }

    public static void requestRecommandMore(DisposeDataListener listener) {
        RequestCenter.getRequest(HttpConstants.HOME_RECOMMAND_MORE, null, listener, null);
    }

    public static void requestFriendData(DisposeDataListener listener) {
        RequestCenter.getRequest(HttpConstants.HOME_FRIEND, null, listener, null);
    }

    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("account", "18734924592");
        params.put("pwd", "999999q");
        RequestCenter.getRequest(HttpConstants.LOGIN, params, listener, null);
    }

    public static void testGetWithP(DisposeDataListener listener) {
        String url = "https://www.wanandroid.com/article/list/0/json";
        RequestParams params = new RequestParams();
        params.put("cid", "60");
        RequestCenter.getRequest(url, params, listener, null);
    }

    public static void testGet(DisposeDataListener listener) {
        String url = "https://www.wanandroid.com/hotkey/json";
        RequestCenter.getRequest(url, null, listener, null);
    }

    public static void testPost(DisposeDataListener listener) {
        String url = "https://www.wanandroid.com/user/register";
        RequestParams params = new RequestParams();
        params.put("username", "luyao");
        params.put("password", "luyaopw");
        //params.put("repassword", "luyaopw");
        params.put("repassword", "luyaopw666");
        RequestCenter.postRequest(url, params, listener, null);
    }
}
