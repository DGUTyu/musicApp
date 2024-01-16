package cn.rxjava.lib_audio.mediaplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;


/**
 * 播放器底部View
 */
public class BottomMusicView extends RelativeLayout {

    public BottomMusicView(Context context) {
        this(context, null);
    }

    public BottomMusicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomMusicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
