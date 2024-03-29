package cn.rxjava.musicapp.imooc_voice.utils;

import cn.rxjava.musicapp.imooc_voice.model.user.User;

/**
 * @description 单例管理登陆用户信息
 */
public class UserManager {

    private static UserManager userManager = null;
    private User user = null;

    public static UserManager getInstance() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
                return userManager;
            }
        } else {
            return userManager;
        }
    }

    /**
     * init the user
     */
    public void setUser(User user) {
        this.user = user;
        setLocal(user);
    }

    private void setLocal(User user) {

    }

    public boolean hasLogined() {
        return user != null;
    }

    /**
     * has user info
     */
    public User getUser() {
        return this.user;
    }

    private User getLocal() {
        return null;
    }


    /**
     * remove the user info
     */
    public void removeUser() {
        this.user = null;
        removeLocal();
    }

    private void removeLocal() {

    }
}
