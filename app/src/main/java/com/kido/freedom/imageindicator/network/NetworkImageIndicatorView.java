package com.kido.freedom.imageindicator.network;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;

import com.kido.freedom.R;
import com.kido.freedom.imageindicator.ImageIndicatorView;
import com.kido.freedom.utils.CircularNetworkImageView;
import com.kido.freedom.utils.VolleySingleton;

import java.util.List;

/**
 * Network ImageIndicatorView, by urls
 *
 * @author steven-pan
 */
public class NetworkImageIndicatorView extends ImageIndicatorView {

    public NetworkImageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetworkImageIndicatorView(Context context) {
        super(context);
    }

    /**
     * 设置显示图片URL列表
     *
     * @param urlList URL列表
     */
    public void setupLayoutByImageUrl(Context mContext,final List<String> urlList) {
        if (urlList == null)
            throw new NullPointerException();

        final int len = urlList.size();
        if (len > 0) {
            for (int index = 0; index < len; index++) {
                final CircularNetworkImageView pageItem = new CircularNetworkImageView(getContext());
                pageItem.setCircled(false);
                pageItem.setScaleType(ScaleType.CENTER_INSIDE);
                pageItem.setDefaultImageResId(R.drawable.ic_launcher);
                pageItem.setImageUrl(urlList.get(index), VolleySingleton.getInstance(mContext).getImageLoader());
                addViewItem(pageItem);
            }
        }
    }

}
