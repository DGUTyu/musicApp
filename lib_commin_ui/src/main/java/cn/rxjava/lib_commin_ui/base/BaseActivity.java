package cn.rxjava.lib_commin_ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import cn.rxjava.lib_commin_ui.utils.StatusBarUtil;

public class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.fixSystemUI(this);
    }
}
