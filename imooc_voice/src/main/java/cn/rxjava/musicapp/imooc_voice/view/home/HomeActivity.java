package cn.rxjava.musicapp.imooc_voice.view.home;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import cn.rxjava.lib_commin_ui.base.BaseActivity;
import cn.rxjava.lib_commin_ui.pager_indictor.ScaleTransitionPagerTitleView;
import cn.rxjava.lib_network.okhttp.listener.DisposeDataListener;
import cn.rxjava.musicapp.R;
import cn.rxjava.musicapp.imooc_voice.api.RequestCenter;
import cn.rxjava.musicapp.imooc_voice.model.CHANNEL;
import cn.rxjava.musicapp.imooc_voice.view.home.adpater.HomePagerAdapter;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 指定首页要出现的卡片
     */
    private static final CHANNEL[] CHANNELS =
            new CHANNEL[]{CHANNEL.MY, CHANNEL.DISCORY, CHANNEL.FRIEND};

    private DrawerLayout mDrawerLayout;
    private View mToggleView;
    private View mSearchView;
    private ViewPager mViewPager;
    private HomePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggleView = findViewById(R.id.toggle_view);
        mSearchView = findViewById(R.id.search_view);
        mViewPager = findViewById(R.id.view_pager);

        mToggleView.setOnClickListener(this);
        mSearchView.setOnClickListener(this);
        mAdapter = new HomePagerAdapter(getSupportFragmentManager(), CHANNELS);
        mViewPager.setAdapter(mAdapter);
        initMagicIndicator();
    }

    private void initData() {

    }

    //初始化指示器
    private void initMagicIndicator() {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELS == null ? 0 : CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                //设置文字、颜色等
                simplePagerTitleView.setText(CHANNELS[index].getKey());
                simplePagerTitleView.setTextSize(19);
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(v -> {
                    mViewPager.setCurrentItem(index);
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_view:
                RequestCenter.testGetWithP(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.e("TAG", "onSuccess: ");
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.e("TAG", "onFailure: ");
                    }
                });


                RequestCenter.login(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        //{"data":null,"errorCode":-1,"errorMsg":"用户名已经被注册！"}
                        //{"data":null,"errorCode":-1,"errorMsg":"两次输入的密码不一致！"}
                        Log.e("TAG", "onSuccess: ");
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        Log.e("TAG", "onFailure: ");
                    }
                });
                break;
        }
    }
}