
package com.shenma.yueba.baijia.adapter;
import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.newmodel.BuyerInfo;
import com.shenma.yueba.view.CircularImageView;
import com.shenma.yueba.view.CustomerImageView;
import com.shenma.yueba.view.RoundImageView;

public class GalleryAdapter extends BaseAdapterWithUtil {
    private int selectItem;
    private List<BuyerInfo> mList;
    public GalleryAdapter(Context mContext,List<BuyerInfo> mList) {
        super(mContext);
        this.mList = mList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub  
        return mList.size();          //
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub  
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub  
        return position;
    }

    public void setSelectItem(int selectItem) {

        if (this.selectItem != selectItem) {
            this.selectItem = selectItem;
            notifyDataSetChanged();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = View.inflate(ctx, R.layout.gallery_item, null);
            holder.riv_item = (CircularImageView)convertView.findViewById(R.id.riv_item);
            holder.tv_item = (TextView)convertView.findViewById(R.id.tv_item);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        //MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getLogo(),holder.riv_item);
        bitmapUtils.display(holder.riv_item, mList.get(position).getLogo());
        holder.tv_item.setText(mList.get(position).getNickName());
        LayoutParams params = holder.riv_item.getLayoutParams();
      //取余，让图片循环浏览
        if (selectItem == position) {
            // Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.scale);    //实现动画效果
            // animation.setFillAfter(true);
            params.height = getDisplayWidth(ctx) / 3 ;
            params.width = getDisplayWidth(ctx) / 3;
            holder.riv_item .setLayoutParams(params);
            holder.tv_item.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.tv_item.setTextSize(15);
            //  imageView.startAnimation(animation);  //选中时，这是设置的比较大
        } else {
            params.height = getDisplayWidth(ctx) / 5;
            params.width = getDisplayWidth(ctx) / 5;
            holder.riv_item .setLayoutParams(params);
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.alpha);    //实现动画效果
            animation.setFillAfter(true);
            holder.riv_item .startAnimation(animation);
            holder.tv_item.setTextColor(ctx.getResources().getColor(R.color.text_gray_color));
            holder.tv_item.setTextSize(12);
//未选中
        }
        return convertView;
    }



    class Holder{
        CircularImageView riv_item;
        TextView tv_item;
    }

    /**
     * 获取当前页面的屏幕宽度
     *
     * @param cx
     * @return
     */
    public static int getDisplayWidth(Context cx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }
}  