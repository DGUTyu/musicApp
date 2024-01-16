package cn.rxjava.musicapp.imooc_voice.model.user;

import cn.rxjava.musicapp.imooc_voice.model.BaseModel;

/**
 * 用户真正实体数据
 */
public class UserContent extends BaseModel {
    //用户唯一标识符
    public String userId;
    public String photoUrl;
    public String name;
    public String tick;
    public String mobile;
    public String platform;
}