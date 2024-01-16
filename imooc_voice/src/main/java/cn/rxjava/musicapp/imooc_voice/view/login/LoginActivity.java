package cn.rxjava.musicapp.imooc_voice.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import cn.rxjava.lib_commin_ui.base.BaseActivity;
import cn.rxjava.lib_network.okhttp.listener.DisposeDataListener;
import cn.rxjava.musicapp.R;
import cn.rxjava.musicapp.imooc_voice.api.RequestCenter;
import cn.rxjava.musicapp.imooc_voice.model.login.LoginEvent;
import cn.rxjava.musicapp.imooc_voice.model.user.User;
import cn.rxjava.musicapp.imooc_voice.utils.UserManager;

public class LoginActivity extends BaseActivity implements DisposeDataListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        //初始化P层
        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestCenter.login(LoginActivity.this);
            }
        });
    }

    @Override
    public void onSuccess(Object responseObj) {
        User user = (User) responseObj;
        UserManager.getInstance().setUser(user);
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }

    @Override
    public void onFailure(Object reasonObj) {

    }
}
