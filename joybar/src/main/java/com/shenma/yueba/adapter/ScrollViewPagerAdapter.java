package com.shenma.yueba.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyj
 * @version 创建时间：2015-6-26 下午5:25:12
 *          程序的简单说明   无线循环滑动适配器
 */

public class ScrollViewPagerAdapter extends PagerAdapter {
    List<View> imageViewlist = new ArrayList<View>();
    Context context;

    public ScrollViewPagerAdapter(Context context, List<View> imageViewlist) {
        this.context = context;
        this.imageViewlist = imageViewlist;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
    }

    @Override
    public int getCount() {

        if (imageViewlist.size() < 1) {
            return 0;
        } else if (imageViewlist.size() <= 2) {
            return imageViewlist.size();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int a = position % imageViewlist.size();
        Log.i("TAG", "A:" + a);
        View imageview = imageViewlist.get(a);

        if (imageview.getParent() != null) {
            ((ViewGroup) imageview.getParent()).removeView(imageview);
        }
        //imageview.setImageResource(R.drawable.default_pic);
        imageview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(imageview, 0);
        imageview.invalidate();
        return imageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //ImageView imageview=imageViewlist.get(position%imageViewlist.size());
        //container.removeView(imageview);
    }
}
