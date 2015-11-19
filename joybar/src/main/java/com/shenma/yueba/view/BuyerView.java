package com.shenma.yueba.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.shenma.yueba.R;
import com.shenma.yueba.util.ToolsUtil;

/**
 * Created by a on 2015/11/17.
 */
public class BuyerView extends LinearLayout{

    private  LinearLayout imagesLayout;
    private ImageView imageView;
    private int screenWith;
    public BuyerView(Context context) {
        super(context);
    }

    public BuyerView(Context context,AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.shopping_guide_layout, null);
        imagesLayout = (LinearLayout) findViewById(R.id.ll_images);
        imageView = new ImageView(context);
        screenWith = ToolsUtil.getDisplayWidth(context);
        setImages(1);
    }



    public void setImages(int count){
        switch (count){
            case 1://一个图片
               ViewGroup.LayoutParams params =  imageView.getLayoutParams();
                params.height = screenWith/2;
                params.width = screenWith/2;
                imageView.setLayoutParams(params);
                imageView.setBackgroundResource(R.drawable.ic_launcher);
                imagesLayout.addView(imageView);
                break;
            case 2://一个图片
                break;
            case 3://一个图片
                break;
            case 4://一个图片
                break;

        }
    }

}
