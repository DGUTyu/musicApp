package cn.rxjava.musicapp.imooc_voice.view.home.adpater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import cn.rxjava.musicapp.imooc_voice.model.CHANNEL;
import cn.rxjava.musicapp.imooc_voice.view.VideoFragment;
import cn.rxjava.musicapp.imooc_voice.view.discory.DiscoryFragment;
import cn.rxjava.musicapp.imooc_voice.view.friend.FriendFragment;
import cn.rxjava.musicapp.imooc_voice.view.mine.MineFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private CHANNEL[] mList;

    public HomePagerAdapter(FragmentManager fm, CHANNEL[] datas) {
        super(fm);
        mList = datas;
    }


    /**
     * 这种方式，避免一次性创建所有的framgent
     *
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        int type = mList[position].getValue();
        switch (type) {
            case CHANNEL.MINE_ID:
                return MineFragment.newInstance();
            case CHANNEL.DISCORY_ID:
                return DiscoryFragment.newInstance();
            case CHANNEL.FRIEND_ID:
                return FriendFragment.newInstance();
            case CHANNEL.VIDEO_ID:
                return VideoFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.length;
    }
}
